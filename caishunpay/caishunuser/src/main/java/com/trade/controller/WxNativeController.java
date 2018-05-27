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

import com.gy.cache.SysparamCache;
import com.gy.system.SysParamUtil;
import com.gy.util.CommonFunction;
import com.gy.util.DateUtil;
import com.gy.util.StringUtil;
import com.trade.bean.TradeDfbean;
import com.trade.bean.WxpayScanCode;
import com.trade.bean.own.QrcodeChannelInf;
import com.trade.bean.own.QrcodeMchtInfo;
import com.trade.bean.own.TradeParam;
import com.trade.bean.response.ResponseCode;
import com.trade.bean.response.ResponseQuery;
import com.trade.enums.MchtStatusEnum;
import com.trade.enums.ResponseEnum;
import com.trade.enums.TradeSourceEnum;
import com.trade.service.CupeService;
import com.trade.service.DaydayPayService;
import com.trade.service.HuikaService;
import com.trade.service.MinshengService;
import com.trade.service.QrcodeMchtInfoService;
import com.trade.service.TradeService;
import com.trade.service.WxpayService;
import com.trade.service.impl.DefaultTradeService;
import com.trade.util.GuangdaUtil;

import java.io.PrintStream;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WxNativeController {
    private static Logger log = Logger.getLogger(WxNativeController.class);
    @Autowired
    private WxpayService wxpayServiceImpl;
    @Autowired
    private HuikaService huikaServiceImpl;
    @Autowired
    private TradeService tfbServiceImpl;
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
    private TradeService wljrServiceImpl;
    @Autowired
    private TradeService wezbankServiceImpl;
    @Autowired
    private TradeService helibaoServiceImpl;
    @Autowired
    private TradeService hfbankServiceImpl;
    @Autowired
    private TradeService qiyepayServiceImpl;
    @Autowired
    private DefaultTradeService defaultTradeService;
    @Autowired
    private SysparamCache sysparamCache;

    @ResponseBody
    @RequestMapping(value = {"/getNativeUrl.do"})
    public ResponseCode getPayurl(HttpServletRequest request) {
        ResponseCode response = new ResponseCode();
        TradeParam reqParam = this.buildTradeParam(request);
        try {
            log.info((Object) ("\u8bf7\u6c42\u4e8c\u7ef4\u7801begin------sign:" + request.getParameter("sign") + "----" + reqParam));
            System.out.println(reqParam);
            String checkValue = reqParam.checkPram();
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
            String checkSign = GuangdaUtil.getMd5Sign(reqParam, qrcodeMcht.getSecretKey());
            if (!checkSign.equals(request.getParameter("sign"))) {
                response.setResultCode(ResponseEnum.FAIL_SIGN.getCode());
                response.setMessage(ResponseEnum.FAIL_SIGN.getMemo());
                return response;
            }
            if (reqParam.getTradeSource() == null) {
                reqParam.setTradeSource(TradeSourceEnum.WEPAY.getCode());
            }
            if (!CommonFunction.isTradeSourceOpen(qrcodeMcht.getTrade_source_list(), TradeSourceEnum.get(reqParam.getTradeSource()))) {
                response.setResultCode(ResponseEnum.PAY_TYPE_UNSUPPORT.getCode());
                response.setMessage(ResponseEnum.PAY_TYPE_UNSUPPORT.getMemo());
                return response;
            }
            if (TradeSourceEnum.ALIPAY.getCode().equals(reqParam.getTradeSource())) {
                ArrayList<String> timeLimit = this.sysparamCache.getAlipayTimeLimit();
                String nowTime = DateUtil.getCurrentTime();
                if (nowTime.compareTo(timeLimit.get(0)) < 0 || nowTime.compareTo(timeLimit.get(1)) > 0) {
                    response.setResultCode(ResponseEnum.PAY_TIME_ERROR.getCode());
                    response.setMessage(ResponseEnum.PAY_TIME_ERROR.getMemo());
                    return response;
                }
            }
            StringBuffer url = request.getRequestURL();
            String rootUrl = url.substring(0, url.indexOf(request.getContextPath()) + request.getContextPath().length());
            reqParam.setRootUrl(rootUrl);
            reqParam.setRemoteAddr(request.getRemoteAddr());
            if (StringUtil.isEmpty(qrcodeMcht.getChannel_id()) || StringUtil.isEmpty(qrcodeMcht.getChannelMchtNo())) {
                response.setResultCode(ResponseEnum.UNAUTHOR_ERROR.getCode());
                response.setMessage(ResponseEnum.UNAUTHOR_ERROR.getMemo());
                return response;
            }
            QrcodeChannelInf qrChannelInf = null;
            qrChannelInf = "1".equals(qrcodeMcht.getJump_flag()) ? this.qrcodeMchtInfoServiceImpl.getChannelInfBalance(qrcodeMcht.getJump_group(), reqParam.getTradeSource()) : this.qrcodeMchtInfoServiceImpl.getChannelInf(qrcodeMcht.getChannel_id(), qrcodeMcht.getChannelMchtNo());
            if (qrChannelInf == null) {
                response.setResultCode(ResponseEnum.ERROR_CHANNEL.getCode());
                response.setMessage(ResponseEnum.ERROR_CHANNEL.getMemo());
                return response;
            }
            if (reqParam.getT0Flag() == null || !"1".equals(reqParam.getT0Flag())) {
                reqParam.setT0Flag("0");
            }
            log.info((Object) ("\u8bf7\u6c42\u5fae\u4fe1\u4e8c\u7ef4\u7801begin------tradeSn:" + reqParam.getTradeSn()));
            if ("0".equals(qrcodeMcht.getReturn_native_qrcode())) {
                response = this.defaultTradeService.prepareTrade(reqParam, qrChannelInf);
            } else if ("daydaypay".equals(qrChannelInf.getChannel_id())) {
                response = this.daydayPayServiceImpl.getDaypayQrCode(reqParam, qrChannelInf);
            } else if ("guangda".equals(qrChannelInf.getChannel_id())) {
                response = this.wxpayServiceImpl.doTrade(reqParam, qrChannelInf);
            } else if ("huika".equals(qrChannelInf.getChannel_id())) {
                response = this.huikaServiceImpl.doTrade(reqParam, qrChannelInf);
            } else if ("tfb".equals(qrChannelInf.getChannel_id())) {
                response = this.tfbServiceImpl.doTrade(reqParam, qrChannelInf);
            } else if ("minsheng".equals(qrChannelInf.getChannel_id())) {
                response = this.minshengServiceImpl.doTrade(reqParam, qrChannelInf);
            } else if ("cupe".equals(qrChannelInf.getChannel_id())) {
                response = this.cupeServiceImpl.doTrade(reqParam, qrChannelInf);
            } else if ("ebusi".equals(qrChannelInf.getChannel_id())) {
                response = this.ebusiServiceImpl.doTrade(reqParam, qrChannelInf);
            } else if ("wljr".equals(qrChannelInf.getChannel_id())) {
                if (TradeSourceEnum.QQPAY.getCode().equals(reqParam.getTradeSource())) {
                    response.setResultCode(ResponseEnum.ERROR_UNSUPPORT.getCode());
                    response.setMessage(ResponseEnum.ERROR_UNSUPPORT.getMemo());
                    return response;
                }
                response = this.wljrServiceImpl.doTrade(reqParam, qrChannelInf);
            } else if ("wezbank".equals(qrChannelInf.getChannel_id())) {
                response = this.wezbankServiceImpl.doTrade(reqParam, qrChannelInf);
            } else if ("helibao".equals(qrChannelInf.getChannel_id())) {
                response = this.helibaoServiceImpl.doTrade(reqParam, qrChannelInf);
            } else if ("hfbank".equals(qrChannelInf.getChannel_id())) {
                response = this.hfbankServiceImpl.doTrade(reqParam, qrChannelInf);
            } else if ("qiyepay".equals(qrChannelInf.getChannel_id())) {
                response = this.qiyepayServiceImpl.doTrade(reqParam, qrChannelInf);
            } else {
                response.setResultCode(ResponseEnum.ERROR_CHANNEL.getCode());
                response.setMessage(ResponseEnum.ERROR_CHANNEL.getMemo());
                return response;
            }
            response.setGymchtId(reqParam.getGymchtId());
            if (ResponseEnum.SUCCESS.getCode().equals(response.getResultCode())) {
                response.setMessage(ResponseEnum.SUCCESS.getMemo());
            }
            response.setSign(GuangdaUtil.getMd5Sign(response, qrcodeMcht.getSecretKey()));
        } catch (Exception e) {
            response.setResultCode(ResponseEnum.FAIL_SYSTEM.getCode());
            response.setMessage(ResponseEnum.FAIL_SYSTEM.getMemo());
            e.printStackTrace();
            log.error((Object) ("\u8bf7\u6c42\u5fae\u4fe1\u4e8c\u7ef4\u7801exception------tradeSn:" + reqParam.getTradeSn() + e));
        }
        log.info((Object) ("\u8bf7\u6c42\u4e8c\u7ef4\u7801end------" + reqParam));
        return response;
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
            WxpayScanCode wxpayScanCode = this.defaultTradeService.queryProcessing(reqParam.getTradeSn(), reqParam.getGymchtId());
            if (wxpayScanCode == null) {
                response.setResultCode(ResponseEnum.FAIL_ORDER_NOT_EXIST.getCode());
                response.setMessage(ResponseEnum.FAIL_ORDER_NOT_EXIST.getMemo());
                return response;
            }
            if (wxpayScanCode.getTrade_state() == null && wxpayScanCode.getCode_url() == null) {
                response.setResultCode(ResponseEnum.FAIL_INVALID_TRADE.getCode());
                response.setMessage(ResponseEnum.FAIL_INVALID_TRADE.getMemo());
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
