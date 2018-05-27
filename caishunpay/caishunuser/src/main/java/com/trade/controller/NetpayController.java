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
import com.trade.service.NetpayService;
import com.trade.service.QrcodeMchtInfoService;
import com.trade.service.netpayimpl.NetpayHandlerService;
import com.trade.util.GuangdaUtil;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value = {"/netpay"}, method = {RequestMethod.POST})
@Controller
public class NetpayController {
    private static Logger log = Logger.getLogger(NetpayController.class);
    @Autowired
    private NetpayService tfbServiceImpl;
    @Autowired
    private NetpayHandlerService netpayHandlerService;
    @Autowired
    private QrcodeMchtInfoService qrcodeMchtInfoServiceImpl;

    @ResponseBody
    @RequestMapping(value = {"/applyPay.do"})
    public QuickpayResponse applyPay(QuickpayParam reqParam, HttpServletRequest request) {
        QuickpayResponse response = new QuickpayResponse();
        try {
            log.info((Object) ("\u8bf7\u6c42\u7f51\u94f6\u652f\u4ed8begin----------" + reqParam));
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
            if (!CommonFunction.isTradeSourceOpen(qrcodeMcht.getTrade_source_list(), TradeSourceEnum.NETPAY)) {
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
            qrChannelInf = "1".equals(qrcodeMcht.getJump_flag()) ? this.qrcodeMchtInfoServiceImpl.getChannelInfBalance(qrcodeMcht.getJump_group(), TradeSourceEnum.NETPAY.getCode()) : this.qrcodeMchtInfoServiceImpl.getChannelInf(qrcodeMcht.getChannel_id(), qrcodeMcht.getChannelMchtNo());
            if (qrChannelInf == null) {
                response.setResultCode(ResponseEnum.ERROR_CHANNEL.getCode());
                response.setMessage(ResponseEnum.ERROR_CHANNEL.getMemo());
                return response;
            }
            if (reqParam.getT0Flag() == null) {
                reqParam.setT0Flag("0");
            }
            log.info((Object) ("\u8bf7\u6c42\u7f51\u94f6\u652f\u4ed8begin------tradeSn:" + reqParam.getTradeSn()));
            if (!"tfb".equals(qrcodeMcht.getChannel_id())) {
                response.setResultCode(ResponseEnum.ERROR_CHANNEL.getCode());
                response.setMessage(ResponseEnum.ERROR_CHANNEL.getMemo());
                return response;
            }
            response = this.netpayHandlerService.netpayApply(reqParam, qrChannelInf);
            response.setGymchtId(reqParam.getGymchtId());
            response.setSign(GuangdaUtil.getMd5Sign(response, qrcodeMcht.getSecretKey()));
        } catch (Exception e) {
            response.setResultCode(ResponseEnum.FAIL_SYSTEM.getCode());
            response.setMessage(ResponseEnum.FAIL_SYSTEM.getMemo());
            e.printStackTrace();
            log.error((Object) ("\u8bf7\u6c42\u7f51\u94f6\u652f\u4ed8exception------tradeSn:" + reqParam.getTradeSn() + e));
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
            if (reqParam.getT0Flag() == null) {
                reqParam.setT0Flag("0");
            }
            log.info((Object) ("\u8bf7\u6c42\u7f51\u94f6\u652f\u4ed8begin------tradeSn:" + reqParam.getTradeSn()));
            response = this.netpayHandlerService.netpayQuery(reqParam, null);
            response.setGymchtId(reqParam.getGymchtId());
            response.setSign(GuangdaUtil.getMd5Sign(response, qrcodeMcht.getSecretKey()));
        } catch (Exception e) {
            response.setResultCode(ResponseEnum.FAIL_SYSTEM.getCode());
            response.setMessage(ResponseEnum.FAIL_SYSTEM.getMemo());
            e.printStackTrace();
            log.error((Object) ("\u8bf7\u6c42\u7f51\u94f6\u652f\u4ed8exception------tradeSn:" + reqParam.getTradeSn() + e));
        }
        log.info((Object) "\u8bf7\u6c42\u4e8c\u7ef4\u7801end------");
        return response;
    }

    private String checkPayParam(QuickpayParam reqParam) {
        if (StringUtil.isEmpty(reqParam.getGymchtId())) {
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
