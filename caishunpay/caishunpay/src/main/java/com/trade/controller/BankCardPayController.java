/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  javax.servlet.http.HttpServletRequest
 *  org.apache.log4j.Logger
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Controller
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestMethod
 *  org.springframework.web.bind.annotation.ResponseBody
 */
package com.trade.controller;

import com.gy.util.CommonFunction;
import com.gy.util.StringUtil;
import com.trade.bean.own.BankCardPayRequest;
import com.trade.bean.own.MerchantInf;
import com.trade.bean.own.PayChannelInf;
import com.trade.bean.response.BankPayResponse;
import com.trade.enums.MchtStatusEnum;
import com.trade.enums.ResponseEnum;
import com.trade.enums.TradeSource;
import com.trade.service.MerchantInfService;
import com.trade.service.netpayimpl.BankCardPayDispatcherService;
import com.trade.util.MD5Util;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/***
 *  网银网关支付接口
 */
@RequestMapping(value={"/bankCardPay"}, method={RequestMethod.POST})
@Controller
public class BankCardPayController {
    private static Logger log = Logger.getLogger(BankCardPayController.class);
    @Autowired
    private BankCardPayDispatcherService bankCardPayDispatcherService;
    @Autowired
    private MerchantInfService merchantInfServiceImpl;

    @ResponseBody
    @RequestMapping(value={"/createOrder"})
    public BankPayResponse createOrder(BankCardPayRequest reqParam, HttpServletRequest request) {
        BankPayResponse response = new BankPayResponse();
        try {
            log.info((Object)("\u8bf7\u6c42\u7f51\u94f6\u652f\u4ed8begin----------" + reqParam));
            String checkValue = this.checkPayParam(reqParam);
            if (!"00".equals(checkValue)) {
                response.setResultCode(ResponseEnum.FAIL_PARAM.getCode());
                response.setMessage(checkValue);
                return response;
            }
            MerchantInf qrcodeMcht = this.merchantInfServiceImpl.getMchtInfo(reqParam.getMerchantId());
            if (qrcodeMcht == null) {
                response.setResultCode(ResponseEnum.FAIL_MCHT_NOT_EXIST.getCode());
                response.setMessage(ResponseEnum.FAIL_MCHT_NOT_EXIST.getMemo());
                return response;
            }
            if (MchtStatusEnum.FREEZE.getCode().equals(qrcodeMcht.getStatus())) {
                response.setResultCode(ResponseEnum.FAIL_MCHT_FREEZE.getCode());
                response.setMessage(ResponseEnum.FAIL_MCHT_FREEZE.getMemo());
                return response;
            }
            String sign = reqParam.getSign();
            reqParam.setSign("");
            String checkSign = MD5Util.getMd5SignByMap(MD5Util.mappingParameterMap(request.getParameterMap()), qrcodeMcht.getSecretKey(),"UTF-8");
            if (!checkSign.equals(sign)) {
                response.setResultCode(ResponseEnum.FAIL_SIGN.getCode());
                response.setMessage(ResponseEnum.FAIL_SIGN.getMemo());
                return response;
            }
            if (!CommonFunction.isTradeSourceOpen(qrcodeMcht.getTrade_source_list(), TradeSource.NETPAY)) {
                response.setResultCode(ResponseEnum.PAY_TYPE_UNSUPPORT.getCode());
                response.setMessage(ResponseEnum.PAY_TYPE_UNSUPPORT.getMemo());
                return response;
            }
            reqParam.setRemoteAddr(request.getRemoteAddr());
            if (StringUtil.isEmpty(qrcodeMcht.getChannel_id()) || StringUtil.isEmpty(qrcodeMcht.getChannelMchtNo())) {
                response.setResultCode(ResponseEnum.UNAUTHOR_ERROR.getCode());
                response.setMessage(ResponseEnum.UNAUTHOR_ERROR.getMemo());
                return response;
            }
            PayChannelInf qrChannelInf = null;
            qrChannelInf = "1".equals(qrcodeMcht.getJump_flag()) ? this.merchantInfServiceImpl.getChannelInfBalance(qrcodeMcht.getJump_group(), TradeSource.NETPAY.getCode()) : this.merchantInfServiceImpl.getChannelInf(qrcodeMcht.getChannel_id(), qrcodeMcht.getChannelMchtNo());
            if (qrChannelInf == null) {
                response.setResultCode(ResponseEnum.ERROR_CHANNEL.getCode());
                response.setMessage(ResponseEnum.ERROR_CHANNEL.getMemo());
                return response;
            }
            if (reqParam.getT0Flag() == null) {
                reqParam.setT0Flag("0");
            }
            log.info((Object)("\u8bf7\u6c42\u7f51\u94f6\u652f\u4ed8begin------tradeSn:" + reqParam.getTradeSn()));
            response = this.bankCardPayDispatcherService.doOrderCreate(reqParam, qrcodeMcht, qrChannelInf);
            response.setNonce(StringUtil.getRandom(32));
            response.setMerchantId(reqParam.getMerchantId());
            response.setSign(MD5Util.getMd5Sign(response, qrcodeMcht.getSecretKey()));
        }
        catch (Exception e) {
            response.setResultCode(ResponseEnum.FAIL_SYSTEM.getCode());
            response.setMessage(ResponseEnum.FAIL_SYSTEM.getMemo());
            e.printStackTrace();
            log.error((Object)("\u8bf7\u6c42\u7f51\u94f6\u652f\u4ed8exception------tradeSn:" + reqParam.getTradeSn() + e));
        }
        log.info((Object)"\u8bf7\u6c42\u4e8c\u7ef4\u7801end------");
        return response;
    }


    @ResponseBody
    @RequestMapping(value={"/queryOrder"})
    public BankPayResponse queryOrder(BankCardPayRequest payRequest, HttpServletRequest request) {
        BankPayResponse response = new BankPayResponse();
        try {
            log.info((Object)("\u8bf7\u6c42\u4e8c\u7ef4\u7801begin----------" + payRequest));
            String checkValue = this.checkQueryParam(payRequest);
            if (!"00".equals(checkValue)) {
                response.setResultCode(ResponseEnum.FAIL_PARAM.getCode());
                response.setMessage(checkValue);
                return response;
            }
            MerchantInf qrcodeMcht = this.merchantInfServiceImpl.getMchtInfo(payRequest.getMerchantId());
            if (qrcodeMcht == null) {
                response.setResultCode(ResponseEnum.FAIL_MCHT_NOT_EXIST.getCode());
                response.setMessage(ResponseEnum.FAIL_MCHT_NOT_EXIST.getMemo());
                return response;
            }
            String sign = payRequest.getSign();
            payRequest.setSign("");
            String checkSign = MD5Util.getMd5Sign(payRequest, qrcodeMcht.getSecretKey());
            if (!checkSign.equals(sign)) {
                response.setResultCode(ResponseEnum.FAIL_SIGN.getCode());
                response.setMessage(ResponseEnum.FAIL_SIGN.getMemo());
                return response;
            }
            payRequest.setRemoteAddr(request.getRemoteAddr());
            if (StringUtil.isEmpty(qrcodeMcht.getChannel_id()) || StringUtil.isEmpty(qrcodeMcht.getChannelMchtNo())) {
                response.setResultCode(ResponseEnum.UNAUTHOR_ERROR.getCode());
                response.setMessage(ResponseEnum.UNAUTHOR_ERROR.getMemo());
                return response;
            }
            if (payRequest.getT0Flag() == null) {
                payRequest.setT0Flag("0");
            }
            log.info((Object)("\u8bf7\u6c42\u7f51\u94f6\u652f\u4ed8begin------tradeSn:" + payRequest.getTradeSn()));
            response = this.bankCardPayDispatcherService.doOrderQuery(payRequest);
            response.setNonce(StringUtil.getRandom(32));
            response.setMerchantId(payRequest.getMerchantId());
            response.setSign(MD5Util.getMd5Sign(response, qrcodeMcht.getSecretKey()));
        }
        catch (Exception e) {
            response.setResultCode(ResponseEnum.FAIL_SYSTEM.getCode());
            response.setMessage(ResponseEnum.FAIL_SYSTEM.getMemo());
            e.printStackTrace();
            log.error((Object)("\u8bf7\u6c42\u7f51\u94f6\u652f\u4ed8exception------tradeSn:" + payRequest.getTradeSn() + e));
        }
        log.info((Object)"\u8bf7\u6c42\u4e8c\u7ef4\u7801end------");
        return response;
    }

    private String checkPayParam(BankCardPayRequest reqParam) {
        if (StringUtil.isEmpty(reqParam.getMerchantId())) {
            return "\u5546\u6237\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(reqParam.getGoodsName())) {
            return "\u5546\u54c1\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(reqParam.getCardType())) {
            return "\u5361\u7c7b\u578b\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(reqParam.getBankSegment())) {
            return "\u94f6\u884c\u4ee3\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(reqParam.getTradeSn())) {
            return "\u8ba2\u5355\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (reqParam.getOrderAmount() == 0) {
            return "\u4ea4\u6613\u91d1\u989d\u6709\u8bef";
        }
        if (reqParam.getOrderAmount() < 13) {
            return "\u4ea4\u6613\u91d1\u989d\u4e0d\u80fd\u4f4e\u4e8e13\u5206";
        }
        if (StringUtil.isEmpty(reqParam.getChannelType())) {
            return "\u6e20\u9053\u7c7b\u578b\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(reqParam.getCallbackUrl())) {
            return "\u9875\u9762\u8df3\u8f6c\u5730\u5740\u4e0d\u80fd\u4e3a\u7a7a";
        }
        return "00";
    }

    private String checkApplyQuickpay(BankCardPayRequest reqParam) {
        if (StringUtil.isEmpty(reqParam.getMerchantId())) {
            return "\u5546\u6237\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(reqParam.getGoodsName())) {
            return "\u5546\u54c1\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(reqParam.getTradeSn())) {
            return "\u8ba2\u5355\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (reqParam.getOrderAmount() == 0) {
            return "\u4ea4\u6613\u91d1\u989d\u6709\u8bef";
        }
        if (StringUtil.isEmpty(reqParam.getCallbackUrl())) {
            return "\u9875\u9762\u8df3\u8f6c\u5730\u5740\u4e0d\u80fd\u4e3a\u7a7a";
        }
        return "00";
    }

    private String checkQueryParam(BankCardPayRequest reqParam) {
        if (StringUtil.isEmpty(reqParam.getMerchantId())) {
            return "\u5546\u6237\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(reqParam.getTradeSn())) {
            return "\u8ba2\u5355\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(reqParam.getNonce())) {
            return "\u968f\u673a\u6570\u4e0d\u80fd\u4e3a\u7a7a";
        }
        return "00";
    }
}
