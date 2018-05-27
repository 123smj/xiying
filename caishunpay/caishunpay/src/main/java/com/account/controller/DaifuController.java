/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  javax.servlet.http.HttpServletRequest
 *  org.apache.log4j.Logger
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Controller
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.ResponseBody
 */
package com.account.controller;

import com.account.bean.TradeMchtAccount;
import com.account.bean.TradeMchtAccountDetail;
import com.account.service.DaifuService;
import com.account.service.TradeMchtAccountService;
import com.gy.system.SysParamUtil;
import com.gy.util.CommonFunction;
import com.gy.util.StringUtil;
import com.gy.util.UUIDGenerator;
import com.trade.bean.own.DfParam;
import com.trade.bean.own.MerchantInf;
import com.trade.bean.response.DfResponse;
import com.trade.enums.ResponseEnum;
import com.trade.enums.TradeSource;
import com.trade.service.MerchantInfService;
import com.trade.util.BeanUtil;
import com.trade.util.MD5Util;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/***
 * 代付接口
 */
@RequestMapping(value={"/daifu"})
@Controller
public class DaifuController {
    private static Logger log = Logger.getLogger(DaifuController.class);
    @Autowired
    private TradeMchtAccountService tradeMchtAccountServiceImpl;//第三方贸易管理服务
    @Autowired
    private MerchantInfService merchantInfServiceImpl;//商人接口服务
    @Autowired
    private DaifuService daifuServiceImpl;

    @ResponseBody
    @RequestMapping(value={"/singlePay.do"})
    public DfResponse singlePay(DfParam dfParam, HttpServletRequest request) {
        DfResponse response = new DfResponse();
        try {
            log.info((Object)("\u8bf7\u6c42\u4ee3\u4ed8\u63a5\u53e3----------" + dfParam));
            String checkValue = this.checkDfParam(dfParam);
            if (!"00".equals(checkValue)) {
                response.setResultCode(ResponseEnum.FAIL_PARAM.getCode());
                response.setMessage(checkValue);
                return response;
            }
            MerchantInf qrcodeMcht = this.merchantInfServiceImpl.getMchtInfo(dfParam.getGymchtId());
            if (qrcodeMcht == null) {
                response.setResultCode(ResponseEnum.FAIL_MCHT_NOT_EXIST.getCode());
                response.setMessage(ResponseEnum.FAIL_MCHT_NOT_EXIST.getMemo());
                return response;
            }
            if ("true".equals(SysParamUtil.getParam("ip_forbid_switch")) && qrcodeMcht.getIp_addr().contains(request.getRemoteAddr())) {
                response.setResultCode(ResponseEnum.IP_FORBIDDEN.getCode());
                response.setMessage(ResponseEnum.IP_FORBIDDEN.getMemo());
                return response;
            }
            String sign = dfParam.getSign();
            dfParam.setSign("");
            String checkSign = MD5Util.getMd5Sign(dfParam, qrcodeMcht.getSecretKey());
            if (!checkSign.equals(sign)) {
                response.setResultCode(ResponseEnum.FAIL_SIGN.getCode());
                response.setMessage(ResponseEnum.FAIL_SIGN.getMemo());
                return response;
            }
            if (!CommonFunction.isTradeSourceOpen(qrcodeMcht.getTrade_source_list(), TradeSource.BANLANCE_DAIFU)) {
                response.setResultCode(ResponseEnum.PAY_TYPE_UNSUPPORT.getCode());
                response.setMessage(ResponseEnum.PAY_TYPE_UNSUPPORT.getMemo());
                return response;
            }
            if (StringUtil.isEmpty(dfParam.getPayType())) {
                dfParam.setPayType("1");
            }
            if ("2".equals(dfParam.getPayType())) {
                response.setResultCode(ResponseEnum.PAY_TYPE_UNSUPPORT.getCode());
                response.setMessage(ResponseEnum.PAY_TYPE_UNSUPPORT.getMemo());
                return response;
            }
            response.setGymchtId(dfParam.getGymchtId());
            response.setDfSn(dfParam.getDfSn());
            TradeMchtAccountDetail mchtAccountDetail = new TradeMchtAccountDetail();
            BeanUtil.copyProperties((Object)dfParam, (Object)mchtAccountDetail);
            mchtAccountDetail.setAccountOrderNo(UUIDGenerator.getOrderIdByUUId("bjcz"));
            mchtAccountDetail.setMchtNo(dfParam.getGymchtId());
            mchtAccountDetail.setMchtFeeValue(0);
            mchtAccountDetail.setSingle_extra_fee(qrcodeMcht.getSingle_extra_fee());
            mchtAccountDetail.setTotalAmount(qrcodeMcht.getSingle_extra_fee() + dfParam.getReceiptAmount());
            mchtAccountDetail.setMchtIncome(qrcodeMcht.getSingle_extra_fee() + dfParam.getReceiptAmount());
            ResponseEnum responseEnum = this.daifuServiceImpl.singlePay(mchtAccountDetail, qrcodeMcht);
            if (ResponseEnum.SUCCESS.equals((Object)responseEnum)) {
                responseEnum = this.daifuServiceImpl.doSinglePay(mchtAccountDetail, qrcodeMcht);
            }
            response.setResultCode(responseEnum.getCode());
            response.setMessage(mchtAccountDetail.getDfStatus() != null ? String.valueOf(mchtAccountDetail.getDfStatus()) + "-" + mchtAccountDetail.getDfDesc() : responseEnum.getMemo());
            if (responseEnum == null) {
                response.setResultCode(ResponseEnum.FAIL_SYSTEM.getCode());
                response.setMessage(ResponseEnum.FAIL_SYSTEM.getMemo());
            } else if (ResponseEnum.SUCCESS.equals((Object)responseEnum)) {
                response = this.buildSinglepayResp(mchtAccountDetail, response);
            }
            response.setSign(MD5Util.getMd5Sign(response, qrcodeMcht.getSecretKey()));
        }
        catch (Exception e) {
            response.setResultCode(ResponseEnum.FAIL_SYSTEM.getCode());
            response.setMessage(ResponseEnum.FAIL_SYSTEM.getMemo());
            e.printStackTrace();
            log.error((Object)("\u8bf7\u6c42\u4ee3\u4ed8\u63a5\u53e3exception------dfSn:" + dfParam.getDfSn() + e));
        }
        log.info((Object)"\u8bf7\u6c42\u4ee3\u4ed8\u63a5\u53e3end------");
        return response;
    }

    @ResponseBody
    @RequestMapping(value={"/querySinglePay.do"})
    public DfResponse querySinglePay(DfParam dfParam, HttpServletRequest request) {
        DfResponse response = new DfResponse();
        try {
            TradeMchtAccountDetail mchtAccountDetail;
            log.info((Object)("\u8bf7\u6c42\u4ee3\u4ed8\u67e5\u8be2\u63a5\u53e3----------" + dfParam));
            String checkValue = this.checkQuerySinglepay(dfParam);
            response.setGymchtId(dfParam.getGymchtId());
            if (!"00".equals(checkValue)) {
                response.setResultCode(ResponseEnum.FAIL_PARAM.getCode());
                response.setMessage(checkValue);
                return response;
            }
            MerchantInf qrcodeMcht = this.merchantInfServiceImpl.getMchtInfo(dfParam.getGymchtId());
            if (qrcodeMcht == null) {
                response.setResultCode(ResponseEnum.FAIL_MCHT_NOT_EXIST.getCode());
                response.setMessage(ResponseEnum.FAIL_MCHT_NOT_EXIST.getMemo());
                return response;
            }
            if ("true".equals(SysParamUtil.getParam("ip_forbid_switch")) && qrcodeMcht.getIp_addr().contains(request.getRemoteAddr())) {
                response.setResultCode(ResponseEnum.IP_FORBIDDEN.getCode());
                response.setMessage(ResponseEnum.IP_FORBIDDEN.getMemo());
                return response;
            }
            String sign = dfParam.getSign();
            dfParam.setSign("");
            String checkSign = MD5Util.getMd5Sign(dfParam, qrcodeMcht.getSecretKey());
            if (!checkSign.equals(sign)) {
                response.setResultCode(ResponseEnum.FAIL_SIGN.getCode());
                response.setMessage(ResponseEnum.FAIL_SIGN.getMemo());
                return response;
            }
            if (StringUtil.isEmpty(dfParam.getPayType())) {
                dfParam.setPayType("1");
            }
            if ((mchtAccountDetail = this.tradeMchtAccountServiceImpl.querySinglePay(dfParam, qrcodeMcht)) == null) {
                response.setResultCode(ResponseEnum.FAIL_ORDER_NOT_EXIST.getCode());
                response.setMessage(ResponseEnum.FAIL_ORDER_NOT_EXIST.getMemo());
                return response;
            }
            response = this.buildQuerySinglepayResp(mchtAccountDetail, response);
            response.setNonce(StringUtil.getRandom(32));
            response.setSign(MD5Util.getMd5Sign(response, qrcodeMcht.getSecretKey()));
        }
        catch (Exception e) {
            response.setResultCode(ResponseEnum.FAIL_SYSTEM.getCode());
            response.setMessage(ResponseEnum.FAIL_SYSTEM.getMemo());
            e.printStackTrace();
            log.error((Object)("\u8bf7\u6c42\u4ee3\u4ed8\u67e5\u8be2\u63a5\u53e3exception------dfSn:" + dfParam.getDfSn() + e));
        }
        log.info((Object)"\u8bf7\u6c42\u4ee3\u4ed8\u67e5\u8be2\u63a5\u53e3end------");
        return response;
    }

    @ResponseBody
    @RequestMapping(value={"/queryAccount.do"})
    public DfResponse queryAccount(DfParam dfParam, HttpServletRequest request) {
        DfResponse response = new DfResponse();
        try {
            TradeMchtAccount tradeMchtAccount;
            log.info((Object)("\u8bf7\u6c42\u8d26\u6237\u67e5\u8be2------" + dfParam));
            String checkValue = this.checkQueryAccount(dfParam);
            if (!"00".equals(checkValue)) {
                response.setResultCode(ResponseEnum.FAIL_PARAM.getCode());
                response.setMessage(checkValue);
                return response;
            }
            MerchantInf qrcodeMcht = this.merchantInfServiceImpl.getMchtInfo(dfParam.getGymchtId());
            if (qrcodeMcht == null) {
                response.setResultCode(ResponseEnum.FAIL_MCHT_NOT_EXIST.getCode());
                response.setMessage(ResponseEnum.FAIL_MCHT_NOT_EXIST.getMemo());
                return response;
            }
            String sign = dfParam.getSign();
            dfParam.setSign("");
            String checkSign = MD5Util.getMd5Sign(dfParam, qrcodeMcht.getSecretKey());
            if (!checkSign.equals(sign)) {
                response.setResultCode(ResponseEnum.FAIL_SIGN.getCode());
                response.setMessage(ResponseEnum.FAIL_SIGN.getMemo());
                return response;
            }
            if (StringUtil.isEmpty(dfParam.getQryType())) {
                dfParam.setQryType("1");
            }
            if ((tradeMchtAccount = this.tradeMchtAccountServiceImpl.queryMchtAccount(dfParam.getGymchtId())) == null) {
                response.setResultCode(ResponseEnum.BALANCE_EXCEPTION.getCode());
                response.setMessage(ResponseEnum.BALANCE_EXCEPTION.getMemo());
                return response;
            }
            response.setResultCode(ResponseEnum.SUCCESS.getCode());
            response.setMessage(ResponseEnum.SUCCESS.getMemo());
            response.setGymchtId(dfParam.getGymchtId());
            response.setBalance(tradeMchtAccount.getBalance());
            response.setAccountStatus(tradeMchtAccount.getStatus());
            response.setNonce(StringUtil.getRandom(32));
            response.setSign(MD5Util.getMd5Sign(response, qrcodeMcht.getSecretKey()));
        }
        catch (Exception e) {
            response.setResultCode(ResponseEnum.FAIL_SYSTEM.getCode());
            response.setMessage(ResponseEnum.FAIL_SYSTEM.getMemo());
            e.printStackTrace();
            log.error((Object)("\u8bf7\u6c42\u8d26\u6237\u67e5\u8be2------gymchtId:" + dfParam.getGymchtId() + e));
        }
        log.info((Object)"\u8d26\u6237\u67e5\u8be2end------");
        return response;
    }

    //审核代付的接口
    private String checkDfParam(DfParam dfParam) {
        if (StringUtil.isEmpty(dfParam.getGymchtId())) {
            return "\u5546\u6237\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (dfParam.getReceiptAmount() == 0) {
            return "\u4ee3\u4ed8\u91d1\u989d\u6709\u8bef";
        }
        if (!"1".equals(dfParam.getCurType())) {
            return "\u91d1\u989d\u7c7b\u578b\u6709\u8bef";
        }
        if (StringUtil.isEmpty(dfParam.getDfSn())) {
            return "\u4ee3\u4ed8\u8ba2\u5355\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(dfParam.getReceiptName())) {
            return "\u6536\u6b3e\u4eba\u59d3\u540d\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(dfParam.getReceiptPan())) {
            return "\u6536\u6b3e\u4eba\u5361\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(dfParam.getReceiptBankNm())) {
            return "\u6536\u6b3e\u94f6\u884c\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (!"0".equals(dfParam.getAcctType())) {
            return "\u5e10\u53f7\u7c7b\u578b\u6709\u8bef";
        }
        if (StringUtil.isEmpty(dfParam.getNonce())) {
            return "\u968f\u673a\u5b57\u7b26\u4e32\u4e0d\u80fd\u4e3a\u7a7a";
        }
        return "00";
    }
     //审核疑问代付的接口
    private String checkQueryAccount(DfParam dfParam) {
        if (StringUtil.isEmpty(dfParam.getGymchtId())) {
            return "\u5546\u6237\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(dfParam.getQryTime())) {
            return "\u67e5\u8be2\u65f6\u95f4\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(dfParam.getNonce())) {
            return "\u968f\u673a\u5b57\u7b26\u4e32\u4e0d\u80fd\u4e3a\u7a7a";
        }
        return "00";
    }
       //审核单笔代付接口
    private String checkQuerySinglepay(DfParam dfParam) {
        if (StringUtil.isEmpty(dfParam.getGymchtId())) {
            return "\u5546\u6237\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(dfParam.getDfSn()) && StringUtil.isEmpty(dfParam.getDfTransactionId())) {
            return "\u5546\u6237\u4ee3\u4ed8\u8ba2\u5355\u53f7\u548c\u56fd\u94f6\u4ee3\u4ed8\u5355\u53f7\u4e0d\u80fd\u540c\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(dfParam.getNonce())) {
            return "\u968f\u673a\u5b57\u7b26\u4e32\u4e0d\u80fd\u4e3a\u7a7a";
        }
        return "00";
    }

    private DfResponse buildQuerySinglepayResp(TradeMchtAccountDetail mchtAccountDetail, DfResponse response) {
        response.setResultCode(ResponseEnum.SUCCESS.getCode());
        response.setMessage(ResponseEnum.SUCCESS.getMemo());
        response.setDfTransactionId(mchtAccountDetail.getAccountOrderNo());
        response.setDfSn(mchtAccountDetail.getDfSn());
        response.setDfState(mchtAccountDetail.getStatus());
        response.setReceiptAmount(mchtAccountDetail.getReceiptAmount());
        response.setDfDesc(mchtAccountDetail.getDfDesc());
        response.setReceiptName(mchtAccountDetail.getReceiptName());
        response.setReceiptPan(mchtAccountDetail.getReceiptPan());
        response.setReceiptBankNm(mchtAccountDetail.getReceiptBankNm());
        response.setMobile(mchtAccountDetail.getMobile());
        response.setTimeEnd(mchtAccountDetail.getTimeEnd());
        return response;
    }

    private DfResponse buildSinglepayResp(TradeMchtAccountDetail mchtAccountDetail, DfResponse response) {
        response.setResultCode(ResponseEnum.SUCCESS.getCode());
        response.setMessage(ResponseEnum.SUCCESS.getMemo());
        response.setDfSn(mchtAccountDetail.getDfSn());
        response.setDfTransactionId(mchtAccountDetail.getAccountOrderNo());
        response.setDfState(mchtAccountDetail.getStatus());
        response.setDfDesc(mchtAccountDetail.getDfDesc());
        response.setTimeEnd(mchtAccountDetail.getTimeEnd());
        response.setNonce(StringUtil.getRandom(32));
        return response;
    }
}
