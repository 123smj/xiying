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
import com.trade.service.QuickpayService;
import com.trade.service.quickimpl.QuickpayServiceImpl;
import com.trade.util.MD5Util;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/***
 * 快捷支付接口
 */
@RequestMapping(value = {"/quickpay"}, method = {RequestMethod.POST})
@Controller
public class QuickPayController {
    private static Logger log = Logger.getLogger(QuickPayController.class);
    @Autowired
    private QuickpayService mobaoPayServiceImpl;
    @Autowired
    private MerchantInfService merchantInfServiceImpl;

    /***
     * 快捷支付 - 交易发起接口
     */
    @ResponseBody
    @RequestMapping(value = {"/prePay.do"})
    public BankPayResponse prePay(BankCardPayRequest reqParam, HttpServletRequest request) {
        BankPayResponse response = new BankPayResponse();
        try {
            log.info((Object) ("\u8bf7\u6c42\u4e8c\u7ef4\u7801begin----------" + reqParam));
            String checkValue = this.checkParam(reqParam);
            //检查参数有效性
            if (!"00".equals(checkValue)) {
                response.setResultCode(ResponseEnum.FAIL_PARAM.getCode());
                response.setMessage(checkValue);
                return response;
            }
            //检查商户是否存在
            MerchantInf qrcodeMcht = this.merchantInfServiceImpl.getMchtInfo(reqParam.getMerchantId());
            if (qrcodeMcht == null) {
                response.setResultCode(ResponseEnum.FAIL_MCHT_NOT_EXIST.getCode());
                response.setMessage(ResponseEnum.FAIL_MCHT_NOT_EXIST.getMemo());
                return response;
            }
            //检查商户是否冻结
            if (MchtStatusEnum.FREEZE.getCode().equals(qrcodeMcht.getStatus())) {
                response.setResultCode(ResponseEnum.FAIL_MCHT_FREEZE.getCode());
                response.setMessage(ResponseEnum.FAIL_MCHT_FREEZE.getMemo());
                return response;
            }
            String sign = reqParam.getSign();
            reqParam.setSign("");
            String checkSign = MD5Util.getMd5Sign(reqParam, qrcodeMcht.getSecretKey());
            //检查签名是否有效
            if (!checkSign.equals(sign)) {
                response.setResultCode(ResponseEnum.FAIL_SIGN.getCode());
                response.setMessage(ResponseEnum.FAIL_SIGN.getMemo());
                return response;
            }
            //检查是否给商户开放相应支付权限
            if (!CommonFunction.isTradeSourceOpen(qrcodeMcht.getTrade_source_list(), TradeSource.QUICKPAY) && !CommonFunction.isTradeSourceOpen(qrcodeMcht.getTrade_source_list(), TradeSource.DAIKOU)) {
                response.setResultCode(ResponseEnum.PAY_TYPE_UNSUPPORT.getCode());
                response.setMessage(ResponseEnum.PAY_TYPE_UNSUPPORT.getMemo());
                return response;
            }
            reqParam.setRemoteAddr(request.getRemoteAddr());
            //检查商户对应通道ID
            if (StringUtil.isEmpty(qrcodeMcht.getChannel_id()) || StringUtil.isEmpty(qrcodeMcht.getChannelMchtNo())) {
                response.setResultCode(ResponseEnum.UNAUTHOR_ERROR.getCode());
                response.setMessage(ResponseEnum.UNAUTHOR_ERROR.getMemo());
                return response;
            }
            PayChannelInf qrChannelInf = null;
            //通道设置 : 检查跳码设置，若是，跳码通道号，反之，获取对应通道号
            qrChannelInf = "1".equals(qrcodeMcht.getJump_flag())
                    ?
                    this.merchantInfServiceImpl.getChannelInfBalance(qrcodeMcht.getJump_group(), TradeSource.QUICKPAY.getCode())
                    :
                    this.merchantInfServiceImpl.getChannelInf(qrcodeMcht.getChannel_id(), qrcodeMcht.getChannelMchtNo());
            //检查通道是否存在
            if (qrChannelInf == null) {
                response.setResultCode(ResponseEnum.ERROR_CHANNEL.getCode());
                response.setMessage(ResponseEnum.ERROR_CHANNEL.getMemo());
                return response;
            }
            if (reqParam.getT0Flag() == null) {
                reqParam.setT0Flag("0");
            }
            log.info((Object) ("\u8bf7\u6c42\u5fae\u4fe1\u4e8c\u7ef4\u7801begin------tradeSn:" + reqParam.getTradeSn()));
            //获取快捷支付实现类
            QuickpayService quickpayServiceImpl = QuickpayServiceImpl.switchService(qrChannelInf.getChannel_id());
            if (quickpayServiceImpl == null) {
                response.setResultCode(ResponseEnum.ERROR_CHANNEL.getCode());
                response.setMessage(ResponseEnum.ERROR_CHANNEL.getMemo());
                return response;
            }
            //调用prePay方法,返回
            response = quickpayServiceImpl.prePay(reqParam, qrcodeMcht, qrChannelInf);
            response.setMerchantId(reqParam.getMerchantId());
            response.setSign(MD5Util.getMd5Sign(response, qrcodeMcht.getSecretKey()));
        } catch (Exception e) {
            response.setResultCode(ResponseEnum.FAIL_SYSTEM.getCode());
            response.setMessage(ResponseEnum.FAIL_SYSTEM.getMemo());
            e.printStackTrace();
            log.error((Object) ("\u8bf7\u6c42\u5fae\u4fe1\u4e8c\u7ef4\u7801exception------tradeSn:" + reqParam.getTradeSn() + e));
        }
        log.info((Object) "\u8bf7\u6c42\u4e8c\u7ef4\u7801end------");
        return response;
    }

    /***
     * 验证短信接口
     * @param reqParam
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/checkPay.do"})
    public BankPayResponse checkPay(BankCardPayRequest reqParam, HttpServletRequest request) {
        BankPayResponse response = new BankPayResponse();
        try {
            log.info((Object) ("\u8bf7\u6c42\u4e8c\u7ef4\u7801begin----------" + reqParam));
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
            String sign = reqParam.getSign();
            reqParam.setSign("");
            String checkSign = MD5Util.getMd5Sign(reqParam, qrcodeMcht.getSecretKey());
            if (!checkSign.equals(sign)) {
                response.setResultCode(ResponseEnum.FAIL_SIGN.getCode());
                response.setMessage(ResponseEnum.FAIL_SIGN.getMemo());
                return response;
            }
            if (!CommonFunction.isTradeSourceOpen(qrcodeMcht.getTrade_source_list(), TradeSource.QUICKPAY)) {
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
            if (reqParam.getT0Flag() == null) {
                reqParam.setT0Flag("0");
            }
            response.setMerchantId(reqParam.getMerchantId());
            log.info((Object) ("\u8bf7\u6c42\u5fae\u4fe1\u4e8c\u7ef4\u7801begin------tradeSn:" + reqParam.getTradeSn()));
            QuickpayService quickpayServiceImpl = this.mobaoPayServiceImpl;
            if (quickpayServiceImpl == null) {
                response.setResultCode(ResponseEnum.ERROR_CHANNEL.getCode());
                response.setMessage(ResponseEnum.ERROR_CHANNEL.getMemo());
                return response;
            }
            response = quickpayServiceImpl.checkPay(reqParam, null);
            response.setSign(MD5Util.getMd5Sign(response, qrcodeMcht.getSecretKey()));
        } catch (Exception e) {
            response.setResultCode(ResponseEnum.FAIL_SYSTEM.getCode());
            response.setMessage(ResponseEnum.FAIL_SYSTEM.getMemo());
            e.printStackTrace();
            log.error((Object) ("\u8bf7\u6c42\u5fae\u4fe1\u4e8c\u7ef4\u7801exception------tradeSn:" + reqParam.getTradeSn() + e));
        }
        log.info((Object) "\u8bf7\u6c42\u4e8c\u7ef4\u7801end------");
        return response;
    }

    @ResponseBody
    @RequestMapping(value = {"/queryPay.do"})
    public BankPayResponse queryPay(BankCardPayRequest reqParam, HttpServletRequest request) {
        BankPayResponse response = new BankPayResponse();
        try {
            log.info((Object) ("\u8bf7\u6c42\u4e8c\u7ef4\u7801begin----------" + reqParam));
            String checkValue = this.checkQueryParam(reqParam);
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
            String sign = reqParam.getSign();
            reqParam.setSign("");
            String checkSign = MD5Util.getMd5Sign(reqParam, qrcodeMcht.getSecretKey());
            if (!checkSign.equals(sign)) {
                response.setResultCode(ResponseEnum.FAIL_SIGN.getCode());
                response.setMessage(ResponseEnum.FAIL_SIGN.getMemo());
                return response;
            }
            reqParam.setRemoteAddr(request.getRemoteAddr());
            if (StringUtil.isEmpty(qrcodeMcht.getChannel_id()) || StringUtil.isEmpty(qrcodeMcht.getChannelMchtNo())) {
                response.setResultCode(ResponseEnum.UNAUTHOR_ERROR.getCode());
                response.setMessage(ResponseEnum.UNAUTHOR_ERROR.getMemo());
                return response;
            }
            PayChannelInf qrChannelInf = this.merchantInfServiceImpl.getChannelInf(qrcodeMcht.getChannel_id(), qrcodeMcht.getChannelMchtNo());
            if (qrChannelInf == null) {
                response.setResultCode(ResponseEnum.ERROR_CHANNEL.getCode());
                response.setMessage(ResponseEnum.ERROR_CHANNEL.getMemo());
                return response;
            }
            if (reqParam.getT0Flag() == null) {
                reqParam.setT0Flag("0");
            }
            response.setMerchantId(reqParam.getMerchantId());
            log.info((Object) ("\u8bf7\u6c42\u5fae\u4fe1\u4e8c\u7ef4\u7801begin------tradeSn:" + reqParam.getTradeSn()));
            QuickpayService quickpayServiceImpl = this.mobaoPayServiceImpl;
            if (quickpayServiceImpl == null) {
                response.setResultCode(ResponseEnum.ERROR_CHANNEL.getCode());
                response.setMessage(ResponseEnum.ERROR_CHANNEL.getMemo());
                return response;
            }
            response = quickpayServiceImpl.queryPay(reqParam, qrChannelInf);
            response.setSign(MD5Util.getMd5Sign(response, qrcodeMcht.getSecretKey()));
        } catch (Exception e) {
            response.setResultCode(ResponseEnum.FAIL_SYSTEM.getCode());
            response.setMessage(ResponseEnum.FAIL_SYSTEM.getMemo());
            e.printStackTrace();
            log.error((Object) ("\u8bf7\u6c42\u5fae\u4fe1\u4e8c\u7ef4\u7801exception------tradeSn:" + reqParam.getTradeSn() + e));
        }
        log.info((Object) "\u8bf7\u6c42\u4e8c\u7ef4\u7801end------");
        return response;
    }

    private String checkParam(BankCardPayRequest reqParam) {
        if (StringUtil.isEmpty(reqParam.getMerchantId())) {
            return "\u5546\u6237\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(reqParam.getTradeSn())) {
            return "\u8ba2\u5355\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (reqParam.getOrderAmount() == null) {
            return "\u4ea4\u6613\u91d1\u989d\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(reqParam.getCardHolderName())) {
            return "\u6301\u5361\u4eba\u59d3\u540d\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(reqParam.getCardNo())) {
            return "\u5361\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(reqParam.getBankName())) {
            return "\u94f6\u884c\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(reqParam.getCardType())) {
            return "\u5361\u7c7b\u578b\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(reqParam.getCerType())) {
            return "\u8bc1\u4ef6\u7c7b\u578b\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(reqParam.getCerNumber())) {
            return "\u8bc1\u4ef6\u53f7\u7801\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(reqParam.getMobileNum())) {
            return "\u624b\u673a\u53f7\u7801\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(reqParam.getNonce())) {
            return "\u968f\u673a\u6570\u4e0d\u80fd\u4e3a\u7a7a";
        }
        return "00";
    }

    private String checkPayParam(BankCardPayRequest reqParam) {
        if (StringUtil.isEmpty(reqParam.getMerchantId())) {
            return "\u5546\u6237\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(reqParam.getTransaction_id())) {
            return "\u5e73\u53f0\u8ba2\u5355\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(reqParam.getYzm())) {
            return "\u9a8c\u8bc1\u7801\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(reqParam.getNonce())) {
            return "\u968f\u673a\u6570\u4e0d\u80fd\u4e3a\u7a7a";
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
        if (reqParam.getOrderAmount() == 0) {
            return "\u4ea4\u6613\u91d1\u989d\u6709\u8bef";
        }
        if (StringUtil.isEmpty(reqParam.getNonce())) {
            return "\u968f\u673a\u6570\u4e0d\u80fd\u4e3a\u7a7a";
        }
        return "00";
    }
}
