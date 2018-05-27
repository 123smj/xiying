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
import com.trade.bean.own.TradeParam;
import com.trade.bean.response.ResponseQuery;
import com.trade.enums.ResponseEnum;
import com.trade.service.TradeService;
import com.trade.service.impl.DefaultTradeService;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = {"/unionDf"})
public class UnionDfController {
    private static Logger log = Logger.getLogger(UnionDfController.class);
    @Autowired
    private TradeService tfbServiceImpl;
    @Autowired
    private DefaultTradeService defaultTradeService;

    @ResponseBody
    @RequestMapping(value = {"/singlePay.do"})
    public String singlePay(String businessContext, HttpServletRequest request) {
        return "";
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
        String free = request.getParameter("free");
        if (free != null) {
            tradeParam.setFree(StringUtil.parseInt(free));
        }
        return tradeParam;
    }
}
