/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  javax.servlet.http.HttpServletRequest
 *  org.apache.log4j.Logger
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Controller
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.ResponseBody
 *  org.springframework.web.servlet.ModelAndView
 */
package com.trade.controller;

import com.gy.system.SysParamUtil;
import com.gy.util.StringUtil;
import com.trade.bean.TradeDfbean;
import com.trade.bean.WxpayScanCode;
import com.trade.bean.own.QrcodeChannelInf;
import com.trade.bean.own.QrcodeMchtInfo;
import com.trade.bean.own.TradeParam;
import com.trade.bean.response.ResponseCode;
import com.trade.bean.response.ResponseQuery;
import com.trade.dao.WxNativeDao;
import com.trade.enums.ResponseEnum;
import com.trade.enums.TradeSourceEnum;
import com.trade.enums.TradeStateEnum;
import com.trade.service.CupeService;
import com.trade.service.DaydayPayService;
import com.trade.service.HuikaService;
import com.trade.service.MinshengService;
import com.trade.service.QrcodeMchtInfoService;
import com.trade.service.TradeService;
import com.trade.service.WxpayService;
import com.trade.service.impl.DefaultTradeService;
import com.trade.util.AesUtil;
import com.trade.util.GuangdaUtil;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = {"/unionPay"})
public class UnionPayController {
    private static Logger log = Logger.getLogger(UnionPayController.class);
    @Autowired
    private WxpayService wxpayServiceImpl;
    @Autowired
    private HuikaService huikaServiceImpl;
    @Autowired
    private QrcodeMchtInfoService qrcodeMchtInfoServiceImpl;
    @Autowired
    private DaydayPayService daydayPayServiceImpl;
    @Autowired
    private MinshengService minshengServiceImpl;
    @Autowired
    private CupeService cupeServiceImpl;
    @Autowired
    private TradeService ebusiServiceImpl;
    @Autowired
    private WxNativeDao wxNativeDao;
    @Autowired
    private TradeService tfbServiceImpl;
    @Autowired
    private DefaultTradeService defaultTradeService;

    @ResponseBody
    @RequestMapping(value = {"/{data}"})
    public ModelAndView getPayurl(@PathVariable(value = "data") String data, HttpServletRequest request) {
        ModelAndView view = null;
        String userAgent = request.getHeader("user-agent").toLowerCase();
        String outTradeNo = null;
        try {
            outTradeNo = new String(AesUtil.decodeAES("17A060CF293B4D54".getBytes(), StringUtil.hex2byte(data.getBytes())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info((Object) ("\u626b\u7801\u652f\u4ed8:" + outTradeNo));
        String browserSource = "";
        if (userAgent.indexOf("micromessenger") > 0) {
            browserSource = TradeSourceEnum.WEPAY.getCode();
        } else if (userAgent.indexOf("alipay") > 0) {
            browserSource = TradeSourceEnum.ALIPAY.getCode();
        } else if (userAgent.indexOf("mqqbrowser") > 0) {
            browserSource = TradeSourceEnum.QQPAY.getCode();
        } else {
            view = new ModelAndView("/trade/error_pay");
            return view;
        }
        request.setAttribute("browserSource", (Object) browserSource);
        request.setAttribute("outTradeNo", (Object) outTradeNo);
        ResponseCode responseCode = new ResponseCode();
        WxpayScanCode wxpayScanCode = this.wxNativeDao.getById(outTradeNo);
        request.setAttribute("orderAmount", (Object) wxpayScanCode.getTotal_fee());
        if (TradeStateEnum.SUCCESS.getCode().equals(wxpayScanCode.getTrade_state())) {
            view = new ModelAndView("/trade/qrcodeQuery");
            request.setAttribute("tradeState", (Object) wxpayScanCode.getTrade_state());
            return view;
        }
        responseCode = this.defaultTradeService.transactProcessing(wxpayScanCode, browserSource);
        if (ResponseEnum.SUCCESS.getCode().equals(responseCode.getResultCode())) {
            if (TradeSourceEnum.WEPAY.getCode().equals(browserSource)) {
                request.setAttribute("responseCode", (Object) responseCode);
                view = new ModelAndView("/trade/pay");
            } else {
                view = new ModelAndView("redirect:" + responseCode.getCode_url());
            }
        } else {
            request.setAttribute("responseCode", (Object) responseCode);
            view = new ModelAndView("/trade/error_pay");
        }
        return view;
    }

    @ResponseBody
    @RequestMapping(value = {"/queryNativePay.do"})
    public ResponseQuery queryNativePay(HttpServletRequest request) {
        TradeParam reqParam = this.buildTradeParam(request);
        log.info((Object) ("\u626b\u7801\u652f\u4ed8\u67e5\u8be2begin------sign:" + request.getParameter("sign") + "----" + reqParam));
        ResponseQuery response = new ResponseQuery();
        try {
            if (!reqParam.checkQueryParam()) {
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
            if (StringUtil.isEmpty(qrcodeMcht.getChannel_id()) || StringUtil.isEmpty(qrcodeMcht.getChannelMchtNo())) {
                response.setResultCode(ResponseEnum.UNAUTHOR_ERROR.getCode());
                response.setMessage(ResponseEnum.UNAUTHOR_ERROR.getMemo());
                return response;
            }
            QrcodeChannelInf qrChannelInf = this.qrcodeMchtInfoServiceImpl.getChannelInf(qrcodeMcht.getChannel_id(), qrcodeMcht.getChannelMchtNo());
            WxpayScanCode wxpayScanCode = this.wxNativeDao.getByTradesn(reqParam.getTradeSn(), reqParam.getGymchtId());
            if ("daydaypay".equals(wxpayScanCode.getChannel_id())) {
                wxpayScanCode = this.daydayPayServiceImpl.queryByTradesn(reqParam.getTradeSn(), reqParam.getGymchtId(), qrChannelInf);
            } else if ("guangda".equals(wxpayScanCode.getChannel_id())) {
                wxpayScanCode = this.wxpayServiceImpl.queryByTradesn(reqParam.getTradeSn(), reqParam.getGymchtId(), qrChannelInf);
            } else if ("huika".equals(wxpayScanCode.getChannel_id())) {
                wxpayScanCode = this.huikaServiceImpl.queryByTradesn(reqParam.getTradeSn(), reqParam.getGymchtId(), qrChannelInf);
            } else if ("tfb".equals(wxpayScanCode.getChannel_id())) {
                wxpayScanCode = this.tfbServiceImpl.queryByTradesn(reqParam.getTradeSn(), reqParam.getGymchtId(), qrChannelInf);
            } else if ("minsheng".equals(wxpayScanCode.getChannel_id())) {
                wxpayScanCode = this.minshengServiceImpl.queryByTradesn(reqParam.getTradeSn(), reqParam.getGymchtId(), qrChannelInf);
            } else if ("cupe".equals(wxpayScanCode.getChannel_id())) {
                wxpayScanCode = this.cupeServiceImpl.queryByTradesn(reqParam.getTradeSn(), reqParam.getGymchtId(), qrChannelInf);
            } else if ("ebusi".equals(wxpayScanCode.getChannel_id())) {
                wxpayScanCode = this.ebusiServiceImpl.queryByTradesn(reqParam.getTradeSn(), reqParam.getGymchtId(), qrChannelInf);
            }
            if (wxpayScanCode == null) {
                response.setResultCode(ResponseEnum.FAIL_ORDER_NOT_EXIST.getCode());
                response.setMessage(ResponseEnum.FAIL_ORDER_NOT_EXIST.getMemo());
                return response;
            }
            if (wxpayScanCode.getTrade_state() == null && wxpayScanCode.getCode_url() == null) {
                response.setResultCode(ResponseEnum.FAIL_INVALID_TRADE.getCode());
                response.setMessage(ResponseEnum.FAIL_ORDER_NOT_EXIST.getMemo());
                return response;
            }
            this.buildResponseQuery(response, wxpayScanCode);
            if ("1".equals(wxpayScanCode.getT0Flag())) {
                TradeDfbean tradeDfBean = this.daydayPayServiceImpl.getDfBean(wxpayScanCode.getOut_trade_no());
                if (response != null && tradeDfBean != null) {
                    response.setT0_status(tradeDfBean.getT0_status());
                    response.setT0RespCode(tradeDfBean.getT0RespCode());
                    response.setT0RespDesc(tradeDfBean.getT0RespDesc());
                }
            }
            response.setSign(GuangdaUtil.getMd5Sign(response, qrcodeMcht.getSecretKey()));
        } catch (Exception e) {
            response.setResultCode(ResponseEnum.FAIL_SYSTEM.getCode());
            response.setMessage(ResponseEnum.FAIL_SYSTEM.getMemo());
            e.printStackTrace();
            log.error((Object) ("\u626b\u7801\u652f\u4ed8\u67e5\u8be2begin------tradeSn:" + reqParam.getTradeSn() + e));
        }
        log.info((Object) ("\u626b\u7801\u652f\u4ed8\u67e5\u8be2end------" + response));
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
        String free = request.getParameter("free");
        if (free != null) {
            tradeParam.setFree(StringUtil.parseInt(free));
        }
        return tradeParam;
    }
}
