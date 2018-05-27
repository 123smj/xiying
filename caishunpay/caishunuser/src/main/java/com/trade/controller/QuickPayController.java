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
import com.trade.bean.own.QrcodeChannelInf;
import com.trade.bean.own.QrcodeMchtInfo;
import com.trade.bean.own.QuickpayParam;
import com.trade.bean.response.QuickpayResponse;
import com.trade.enums.MchtStatusEnum;
import com.trade.enums.ResponseEnum;
import com.trade.enums.TradeSourceEnum;
import com.trade.service.QrcodeMchtInfoService;
import com.trade.service.QuickpayService;
import com.trade.service.quickimpl.QuickpayServiceImpl;
import com.trade.util.GuangdaUtil;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value = {"/quickpay"}, method = {RequestMethod.POST})
@Controller
public class QuickPayController {
    private static Logger log = Logger.getLogger(QuickPayController.class);
    @Autowired
    private QuickpayService mobaoPayServiceImpl;
    @Autowired
    private QrcodeMchtInfoService qrcodeMchtInfoServiceImpl;

    @ResponseBody
    @RequestMapping(value = {"/prePay.do"})
    public QuickpayResponse prePay(QuickpayParam reqParam, HttpServletRequest request) {
        QuickpayResponse response = new QuickpayResponse();
        try {
            log.info((Object) ("\u8bf7\u6c42\u4e8c\u7ef4\u7801begin----------" + reqParam));
            String checkValue = this.checkParam(reqParam);
            if (!"00".equals(checkValue)) {
                response.setResultCode(ResponseEnum.FAIL_PARAM.getCode());
                response.setMessage(checkValue);
                return response;
            }
            QrcodeMchtInfo qrcodeMcht = this.qrcodeMchtInfoServiceImpl.getMchtInfo(reqParam.getGymchtId());
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
            String checkSign = GuangdaUtil.getMd5Sign(reqParam, qrcodeMcht.getSecretKey());
            if (!checkSign.equals(sign)) {
                response.setResultCode(ResponseEnum.FAIL_SIGN.getCode());
                response.setMessage(ResponseEnum.FAIL_SIGN.getMemo());
                return response;
            }
            if (!CommonFunction.isTradeSourceOpen(qrcodeMcht.getTrade_source_list(), TradeSourceEnum.QUICKPAY)) {
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
            QrcodeChannelInf qrChannelInf = null;
            qrChannelInf = "1".equals(qrcodeMcht.getJump_flag()) ? this.qrcodeMchtInfoServiceImpl.getChannelInfBalance(qrcodeMcht.getJump_group(), TradeSourceEnum.QUICKPAY.getCode()) : this.qrcodeMchtInfoServiceImpl.getChannelInf(qrcodeMcht.getChannel_id(), qrcodeMcht.getChannelMchtNo());
            if (qrChannelInf == null) {
                response.setResultCode(ResponseEnum.ERROR_CHANNEL.getCode());
                response.setMessage(ResponseEnum.ERROR_CHANNEL.getMemo());
                return response;
            }
            if (reqParam.getT0Flag() == null) {
                reqParam.setT0Flag("0");
            }
            log.info((Object) ("\u8bf7\u6c42\u5fae\u4fe1\u4e8c\u7ef4\u7801begin------tradeSn:" + reqParam.getTradeSn()));
            QuickpayService quickpayServiceImpl = QuickpayServiceImpl.switchService(qrChannelInf.getChannel_id());
            if (quickpayServiceImpl == null) {
                response.setResultCode(ResponseEnum.ERROR_CHANNEL.getCode());
                response.setMessage(ResponseEnum.ERROR_CHANNEL.getMemo());
                return response;
            }
            response = quickpayServiceImpl.prePay(reqParam, qrChannelInf);
            response.setGymchtId(reqParam.getGymchtId());
            response.setSign(GuangdaUtil.getMd5Sign(response, qrcodeMcht.getSecretKey()));
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
    @RequestMapping(value = {"/checkPay.do"})
    public QuickpayResponse checkPay(QuickpayParam reqParam, HttpServletRequest request) {
        QuickpayResponse response = new QuickpayResponse();
        try {
            log.info((Object) ("\u8bf7\u6c42\u4e8c\u7ef4\u7801begin----------" + reqParam));
            String checkValue = this.checkPayParam(reqParam);
            if (!"00".equals(checkValue)) {
                response.setResultCode(ResponseEnum.FAIL_PARAM.getCode());
                response.setMessage(checkValue);
                return response;
            }
            QrcodeMchtInfo qrcodeMcht = this.qrcodeMchtInfoServiceImpl.getMchtInfo(reqParam.getGymchtId());
            if (qrcodeMcht == null) {
                response.setResultCode(ResponseEnum.FAIL_MCHT_NOT_EXIST.getCode());
                response.setMessage(ResponseEnum.FAIL_MCHT_NOT_EXIST.getMemo());
                return response;
            }
            String sign = reqParam.getSign();
            reqParam.setSign("");
            String checkSign = GuangdaUtil.getMd5Sign(reqParam, qrcodeMcht.getSecretKey());
            if (!checkSign.equals(sign)) {
                response.setResultCode(ResponseEnum.FAIL_SIGN.getCode());
                response.setMessage(ResponseEnum.FAIL_SIGN.getMemo());
                return response;
            }
            if (!CommonFunction.isTradeSourceOpen(qrcodeMcht.getTrade_source_list(), TradeSourceEnum.QUICKPAY)) {
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
            response.setGymchtId(reqParam.getGymchtId());
            log.info((Object) ("\u8bf7\u6c42\u5fae\u4fe1\u4e8c\u7ef4\u7801begin------tradeSn:" + reqParam.getTradeSn()));
            QuickpayService quickpayServiceImpl = this.mobaoPayServiceImpl;
            if (quickpayServiceImpl == null) {
                response.setResultCode(ResponseEnum.ERROR_CHANNEL.getCode());
                response.setMessage(ResponseEnum.ERROR_CHANNEL.getMemo());
                return response;
            }
            response = quickpayServiceImpl.checkPay(reqParam, null);
            response.setSign(GuangdaUtil.getMd5Sign(response, qrcodeMcht.getSecretKey()));
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
    public QuickpayResponse queryPay(QuickpayParam reqParam, HttpServletRequest request) {
        QuickpayResponse response = new QuickpayResponse();
        try {
            log.info((Object) ("\u8bf7\u6c42\u4e8c\u7ef4\u7801begin----------" + reqParam));
            String checkValue = this.checkQueryParam(reqParam);
            if (!"00".equals(checkValue)) {
                response.setResultCode(ResponseEnum.FAIL_PARAM.getCode());
                response.setMessage(checkValue);
                return response;
            }
            QrcodeMchtInfo qrcodeMcht = this.qrcodeMchtInfoServiceImpl.getMchtInfo(reqParam.getGymchtId());
            if (qrcodeMcht == null) {
                response.setResultCode(ResponseEnum.FAIL_MCHT_NOT_EXIST.getCode());
                response.setMessage(ResponseEnum.FAIL_MCHT_NOT_EXIST.getMemo());
                return response;
            }
            String sign = reqParam.getSign();
            reqParam.setSign("");
            String checkSign = GuangdaUtil.getMd5Sign(reqParam, qrcodeMcht.getSecretKey());
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
            QrcodeChannelInf qrChannelInf = this.qrcodeMchtInfoServiceImpl.getChannelInf(qrcodeMcht.getChannel_id(), qrcodeMcht.getChannelMchtNo());
            if (qrChannelInf == null) {
                response.setResultCode(ResponseEnum.ERROR_CHANNEL.getCode());
                response.setMessage(ResponseEnum.ERROR_CHANNEL.getMemo());
                return response;
            }
            if (reqParam.getT0Flag() == null) {
                reqParam.setT0Flag("0");
            }
            response.setGymchtId(reqParam.getGymchtId());
            log.info((Object) ("\u8bf7\u6c42\u5fae\u4fe1\u4e8c\u7ef4\u7801begin------tradeSn:" + reqParam.getTradeSn()));
            QuickpayService quickpayServiceImpl = this.mobaoPayServiceImpl;
            if (quickpayServiceImpl == null) {
                response.setResultCode(ResponseEnum.ERROR_CHANNEL.getCode());
                response.setMessage(ResponseEnum.ERROR_CHANNEL.getMemo());
                return response;
            }
            response = quickpayServiceImpl.queryPay(reqParam, qrChannelInf);
            response.setSign(GuangdaUtil.getMd5Sign(response, qrcodeMcht.getSecretKey()));
        } catch (Exception e) {
            response.setResultCode(ResponseEnum.FAIL_SYSTEM.getCode());
            response.setMessage(ResponseEnum.FAIL_SYSTEM.getMemo());
            e.printStackTrace();
            log.error((Object) ("\u8bf7\u6c42\u5fae\u4fe1\u4e8c\u7ef4\u7801exception------tradeSn:" + reqParam.getTradeSn() + e));
        }
        log.info((Object) "\u8bf7\u6c42\u4e8c\u7ef4\u7801end------");
        return response;
    }

    private String checkParam(QuickpayParam reqParam) {
        if (StringUtil.isEmpty(reqParam.getGymchtId())) {
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

    private String checkPayParam(QuickpayParam reqParam) {
        if (StringUtil.isEmpty(reqParam.getGymchtId())) {
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

    private String checkQueryParam(QuickpayParam reqParam) {
        if (StringUtil.isEmpty(reqParam.getGymchtId())) {
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
