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
package com.trade.controller;

import com.gy.system.SysParamUtil;
import com.gy.util.StringUtil;
import com.trade.bean.WxpayScanCode;
import com.trade.bean.own.QrcodeChannelInf;
import com.trade.bean.own.TradeParam;
import com.trade.bean.response.ResponseCode;
import com.trade.bean.response.ResponseQuery;
import com.trade.enums.ResponseEnum;
import com.trade.service.QrcodeMchtInfoService;
import com.trade.service.TradeService;

import java.io.PrintStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = {"/webankRegmcht"})
public class WeBankRegmchtController {
    private static Logger log = Logger.getLogger(WeBankRegmchtController.class);
    @Autowired
    private TradeService wezbankServiceImpl;
    @Autowired
    private QrcodeMchtInfoService qrcodeMchtInfoServiceImpl;

    @ResponseBody
    @RequestMapping(value = {"/regAlimcht.do"})
    public ResponseCode getPayurl(HttpServletRequest request) {
        ResponseCode response = new ResponseCode();
        TradeParam reqParam = this.buildTradeParam(request);
        try {
            log.info((Object) ("\u8bf7\u6c42\u4e8c\u7ef4\u7801begin------sign:" + request.getParameter("sign") + "----" + reqParam));
            System.out.println(reqParam);
            response = this.wezbankServiceImpl.getWxjspay(null, null);
            if (ResponseEnum.SUCCESS.getCode().equals(response.getResultCode())) {
                response.setMessage(ResponseEnum.SUCCESS.getMemo());
            }
        } catch (Exception e) {
            response.setResultCode(ResponseEnum.FAIL_SYSTEM.getCode());
            response.setMessage(ResponseEnum.FAIL_SYSTEM.getMemo());
            e.printStackTrace();
            log.error((Object) ("\u8bf7\u6c42\u5fae\u4fe1\u4e8c\u7ef4\u7801exception------tradeSn:" + reqParam.getTradeSn() + e));
        }
        log.info((Object) ("\u8bf7\u6c42\u4e8c\u7ef4\u7801end------" + reqParam));
        return response;
    }

    private void buildResponseQuery(ResponseQuery response, WxpayScanCode wxpayScanCode) {
        response.setGymchtId(wxpayScanCode.getGymchtId());
        response.setTradeSn(wxpayScanCode.getTradeSn());
        response.setTransaction_id(wxpayScanCode.getOut_trade_no());
        if ("1".equals(SysParamUtil.getParam("out_transaction_id_return"))) {
            response.setOut_transaction_id(wxpayScanCode.getTransaction_id());
        }
        response.setOrderAmount(wxpayScanCode.getTotal_fee());
        response.setCoupon_fee(wxpayScanCode.getCoupon_fee());
        response.setTradeState(wxpayScanCode.getTrade_state());
        response.setBankType(wxpayScanCode.getBank_type());
        response.setTimeEnd(wxpayScanCode.getTime_end());
        response.setT0Flag(wxpayScanCode.getT0Flag());
        response.setResultCode(ResponseEnum.SUCCESS.getCode());
        response.setMessage(ResponseEnum.SUCCESS.getMemo());
    }

    private TradeParam buildTradeParam(HttpServletRequest request) {
        TradeParam tradeParam = new TradeParam();
        tradeParam.setGymchtId(request.getParameter("gymchtId"));
        tradeParam.setChanelId(request.getParameter("chanelId"));
        tradeParam.setNotifyUrl(request.getParameter("notifyUrl"));
        tradeParam.setTradeSn(request.getParameter("tradeSn"));
        tradeParam.setOrderAmount(StringUtil.parseInt(request.getParameter("orderAmount")));
        tradeParam.setGoodsName(request.getParameter("goodsName"));
        tradeParam.setExpirySecond(request.getParameter("expirySecond"));
        tradeParam.setT0Flag(request.getParameter("t0Flag"));
        tradeParam.setIdcard(request.getParameter("idcard"));
        tradeParam.setRealname(request.getParameter("realname"));
        tradeParam.setBankcardnum(request.getParameter("bankcardnum"));
        tradeParam.setBankname(request.getParameter("bankname"));
        tradeParam.setSubbranch(request.getParameter("subbranch"));
        tradeParam.setPcnaps(request.getParameter("pcnaps"));
        tradeParam.setTradeSource(request.getParameter("tradeSource"));
        StringBuffer url = request.getRequestURL();
        String rootUrl = url.substring(0, url.indexOf(request.getContextPath()) + request.getContextPath().length());
        tradeParam.setRootUrl(rootUrl);
        String free = request.getParameter("free");
        if (free != null) {
            tradeParam.setFree(StringUtil.parseInt(free));
        }
        return tradeParam;
    }
}
