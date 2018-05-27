/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  javax.servlet.http.HttpServletRequest
 *  org.apache.log4j.Logger
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Controller
 *  org.springframework.ui.Model
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.ResponseBody
 */
package com.trade.controller;

import com.gy.util.CommonFunction;
import com.gy.util.StringUtil;
import com.trade.bean.own.QrcodeChannelInf;
import com.trade.bean.own.QrcodeMchtInfo;
import com.trade.bean.own.TradeParam;
import com.trade.bean.response.Response;
import com.trade.bean.response.ResponseCode;
import com.trade.enums.ResponseEnum;
import com.trade.enums.TradeSourceEnum;
import com.trade.service.MinshengService;
import com.trade.service.QrcodeMchtInfoService;
import com.trade.service.TradeService;
import com.trade.service.WxpayService;
import com.trade.util.GuangdaUtil;

import java.io.PrintStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WxJspayController {
    private static Logger log = Logger.getLogger(WxJspayController.class);
    @Autowired
    private WxpayService wxpayServiceImpl;
    @Autowired
    private TradeService tfbServiceImpl;
    @Autowired
    private MinshengService minshengServiceImpl;
    @Autowired
    private QrcodeMchtInfoService qrcodeMchtInfoServiceImpl;

    @ResponseBody
    @RequestMapping(value = {"/getJspay.do"})
    public Response getPayurl(HttpServletRequest request, Model model) {
        ResponseCode response = new ResponseCode();
        TradeParam reqParam = this.buildTradeParam(request);
        try {
            log.info((Object) ("\u8bf7\u6c42\u516c\u4f17\u53f7\u652f\u4ed8begin------sign:" + request.getParameter("sign") + "----" + reqParam));
            System.out.println(reqParam);
            if (!reqParam.checkJspay()) {
                response.setResultCode(ResponseEnum.FAIL_PARAM.getCode());
                response.setMessage(ResponseEnum.FAIL_PARAM.getMemo());
                return response;
            }
            QrcodeMchtInfo qrcodeMcht = this.qrcodeMchtInfoServiceImpl.getMchtInfo(reqParam.getGymchtId());
            if (qrcodeMcht == null) {
                response.setResultCode(ResponseEnum.FAIL_MCHT_NOT_EXIST.getCode());
                response.setMessage(ResponseEnum.FAIL_MCHT_NOT_EXIST.getMemo());
                return response;
            }
            String checkSign = GuangdaUtil.getMd5Sign(reqParam, qrcodeMcht.getSecretKey());
            if (!checkSign.equals(request.getParameter("sign"))) {
                response.setResultCode(ResponseEnum.FAIL_SIGN.getCode());
                response.setMessage(ResponseEnum.FAIL_SIGN.getMemo());
                return response;
            }
            if (!CommonFunction.isTradeSourceOpen(qrcodeMcht.getTrade_source_list(), TradeSourceEnum.WEJSPAY)) {
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
            qrChannelInf = "1".equals(qrcodeMcht.getJump_flag()) ? this.qrcodeMchtInfoServiceImpl.getChannelInfBalance(qrcodeMcht.getJump_group(), TradeSourceEnum.WEJSPAY.getCode()) : this.qrcodeMchtInfoServiceImpl.getChannelInf(qrcodeMcht.getChannel_id(), qrcodeMcht.getChannelMchtNo());
            if (qrChannelInf == null) {
                response.setResultCode(ResponseEnum.ERROR_CHANNEL.getCode());
                response.setMessage(ResponseEnum.ERROR_CHANNEL.getMemo());
                return response;
            }
            if (reqParam.getT0Flag() == null || !"1".equals(reqParam.getT0Flag())) {
                reqParam.setT0Flag("0");
            }
            response.setGymchtId(reqParam.getGymchtId());
            log.info((Object) ("\u8bf7\u6c42\u516c\u4f17\u53f7\u652f\u4ed8begin------tradeSn:" + reqParam.getTradeSn()));
            if ("daydaypay".equals(qrcodeMcht.getChannel_id())) {
                response.setResultCode(ResponseEnum.ERROR_UNSUPPORT.getCode());
                response.setMessage(ResponseEnum.ERROR_UNSUPPORT.getMemo());
            } else if ("guangda".equals(qrcodeMcht.getChannel_id())) {
                response = this.wxpayServiceImpl.getWxjspay(reqParam, qrChannelInf);
            } else if ("tfb".equals(qrcodeMcht.getChannel_id())) {
                response = this.tfbServiceImpl.getWxjspay(reqParam, qrChannelInf);
            } else if ("minsheng".equals(qrcodeMcht.getChannel_id())) {
                response = this.minshengServiceImpl.getWxjspay(reqParam, qrChannelInf);
            } else {
                response.setResultCode(ResponseEnum.ERROR_CHANNEL.getCode());
                response.setMessage(ResponseEnum.ERROR_CHANNEL.getMemo());
                return response;
            }
            response.setSign(GuangdaUtil.getMd5Sign(response, qrcodeMcht.getSecretKey()));
        } catch (Exception e) {
            response.setResultCode(ResponseEnum.FAIL_SYSTEM.getCode());
            response.setMessage(ResponseEnum.FAIL_SYSTEM.getMemo());
            e.printStackTrace();
            log.error((Object) ("\u8bf7\u6c42\u516c\u4f17\u53f7\u652f\u4ed8exception------tradeSn:" + reqParam.getTradeSn() + e));
        }
        log.info((Object) ("\u8bf7\u6c42\u516c\u4f17\u53f7\u652f\u4ed8end------" + reqParam));
        return response;
    }

    private TradeParam buildTradeParam(HttpServletRequest request) {
        TradeParam tradeParam = new TradeParam();
        tradeParam.setGymchtId(request.getParameter("gymchtId"));
        tradeParam.setTradeSn(request.getParameter("tradeSn"));
        tradeParam.setOrderAmount(StringUtil.parseInt(request.getParameter("orderAmount")));
        tradeParam.setGoodsName(request.getParameter("goodsName"));
        tradeParam.setExpirySecond(request.getParameter("expirySecond"));
        tradeParam.setIs_raw(request.getParameter("is_raw"));
        tradeParam.setSub_openid(request.getParameter("sub_openid"));
        tradeParam.setNotifyUrl(request.getParameter("notifyUrl"));
        tradeParam.setCallback_url(request.getParameter("callback_url"));
        tradeParam.setSub_appid(request.getParameter("sub_appid"));
        return tradeParam;
    }
}
