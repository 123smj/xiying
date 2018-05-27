/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.log4j.Logger
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.trade.service.impl;

import com.account.service.TradeMchtAccountService;
import com.gy.system.Environment;
import com.gy.system.SysParamUtil;
import com.gy.util.ContextUtil;
import com.gy.util.DateUtil;
import com.gy.util.HttpUtility;
import com.gy.util.StringUtil;
import com.gy.util.UUIDGenerator;
import com.trade.bean.WxpayScanCode;
import com.trade.bean.own.QrcodeChannelInf;
import com.trade.bean.own.QrcodeMchtInfo;
import com.trade.bean.own.TradeParam;
import com.trade.bean.response.ResponseCode;
import com.trade.bean.response.ResponseQuery;
import com.trade.dao.QrcodeMchtInfoDao;
import com.trade.dao.WxNativeDao;
import com.trade.enums.ResponseEnum;
import com.trade.enums.TradeSourceEnum;
import com.trade.enums.TradeStateEnum;
import com.trade.service.TradeService;
import com.trade.util.AesUtil;
import com.trade.util.GuangdaUtil;
import com.trade.util.JsonUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultTradeService {
    @Autowired
    private WxNativeDao wxNativeDao;
    private TradeService tradeService;
    @Autowired
    private QrcodeMchtInfoDao qrcodeMchtInfoDaoImpl;
    @Autowired
    protected TradeMchtAccountService tradeMchtAccountService;
    private static Logger log = Logger.getLogger(DefaultTradeService.class);
    private Map<String, String[]> tradeMap = new ConcurrentHashMap<String, String[]>();
    private static final String NEW_TRADE = "1";
    private static final String PROCESSING = "9";
    private static final String NOT_NEW_TRADE = "0";

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public ResponseCode transactProcessing(WxpayScanCode wxpayScanCode, String browserSource) {
        ResponseCode response = new ResponseCode();
        if (wxpayScanCode == null) {
            response.setResultCode(ResponseEnum.FAIL_INVALID_TRADE.getCode());
            response.setMessage(ResponseEnum.FAIL_INVALID_TRADE.getMemo());
            return response;
        }
        this.tradeService = this.switchService(wxpayScanCode.getChannel_id());
        if (this.tradeService == null) {
            response.setResultCode(ResponseEnum.ERROR_CHANNEL.getCode());
            response.setMessage(ResponseEnum.ERROR_CHANNEL.getMemo());
            return response;
        }
        if ("0".equals(wxpayScanCode.getIs_new_trade())) {
            if (!wxpayScanCode.getTrade_source().equals(browserSource)) {
                response.setResultCode(ResponseEnum.PAY_TYPE_ERROR.getCode());
                response.setMessage("\u8bf7\u4f7f\u7528" + (TradeSourceEnum.get(wxpayScanCode.getTrade_source()) == null ? wxpayScanCode.getTrade_source() : TradeSourceEnum.get(wxpayScanCode.getTrade_source()).getMemo()));
                return response;
            }
            response.setResultCode(ResponseEnum.SUCCESS.getCode());
            response.setMessage(ResponseEnum.SUCCESS.getMemo());
            response.setCode_url(wxpayScanCode.getCode_url());
            return response;
        }
        boolean wait = false;
        Map<String, String[]> map = this.tradeMap;
        synchronized (map) {
            String[] tradeProcessState;
            if (this.tradeMap.size() > 500) {
                this.tradeMap.clear();
            }
            if ((tradeProcessState = this.tradeMap.get(wxpayScanCode.getOut_trade_no())) == null) {
                this.tradeMap.put(wxpayScanCode.getOut_trade_no(), new String[]{"9", ""});
            } else {
                wait = true;
            }
        }
        if (wait) {
            long t1;
            long t2 = t1 = System.currentTimeMillis();
            do {
                if (!this.tradeMap.get(wxpayScanCode.getOut_trade_no())[0].equals("0")) continue;
                response.setCode_url(this.tradeMap.get(wxpayScanCode.getOut_trade_no())[1]);
                response.setResultCode(ResponseEnum.SUCCESS.getCode());
                response.setMessage(ResponseEnum.SUCCESS.getMemo());
                return response;
            } while ((t2 = System.currentTimeMillis()) - t1 <= 30000L);
            response.setResultCode(ResponseEnum.SUCCESS_BUT_TIME_OUT.getCode());
            response.setMessage(ResponseEnum.SUCCESS_BUT_TIME_OUT.getMemo());
            return response;
        }
        wxpayScanCode.setTrade_source(browserSource);
        wxpayScanCode.setIs_new_trade("0");
        response = this.tradeService.doTrade(wxpayScanCode);
        this.tradeMap.put(wxpayScanCode.getOut_trade_no(), new String[]{"0", response.getCode_url()});
        return response;
    }

    public WxpayScanCode queryProcessing(String tradeSn, String gyMchtId) {
        WxpayScanCode wxpayScanCode = this.wxNativeDao.getByTradesn(tradeSn, gyMchtId);
        if (wxpayScanCode == null) {
            return null;
        }
        if (!TradeStateEnum.SUCCESS.getCode().equals(wxpayScanCode.getTrade_state())) {
            this.tradeService = this.switchService(wxpayScanCode.getChannel_id());
            QrcodeChannelInf qrChannelInf = this.qrcodeMchtInfoDaoImpl.getChannelInf(wxpayScanCode.getChannel_id(), wxpayScanCode.getMch_id());
            wxpayScanCode = this.tradeService.queryFromChannel(wxpayScanCode, qrChannelInf);
            if (TradeStateEnum.SUCCESS.getCode().equals(wxpayScanCode.getTrade_state())) {
                this.tradeMchtAccountService.notifySuccess(wxpayScanCode);
            }
        }
        return wxpayScanCode;
    }

    public ResponseCode prepareTrade(TradeParam reqParam, QrcodeChannelInf qrChannelInf) throws Exception {
        WxpayScanCode wxpay = new WxpayScanCode();
        String tradeNo = UUIDGenerator.getOrderIdByUUId();
        ResponseCode response = new ResponseCode();
        response.setGymchtId(reqParam.getGymchtId());
        if (this.wxNativeDao.getByTradesn(reqParam.getTradeSn(), reqParam.getGymchtId()) != null) {
            response.setResultCode(ResponseEnum.FAIL_ORDER_NO_REPEAT.getCode());
            response.setMessage(ResponseEnum.FAIL_ORDER_NO_REPEAT.getMemo());
            return response;
        }
        String currTime = DateUtil.getCurrTime();
        wxpay.setMch_id(qrChannelInf.getChannel_mcht_no());
        wxpay.setMch_create_ip(reqParam.getRemoteAddr());
        wxpay.setChannel_id(qrChannelInf.getChannel_id());
        if (TradeSourceEnum.ALIPAY.getCode().equals(reqParam.getTradeSource())) {
            wxpay.setTrade_source(TradeSourceEnum.ALIPAY.getCode());
        } else if (TradeSourceEnum.QQPAY.getCode().equals(reqParam.getTradeSource())) {
            wxpay.setTrade_source(TradeSourceEnum.QQPAY.getCode());
        } else {
            wxpay.setTrade_source(TradeSourceEnum.WEPAY.getCode());
        }
        wxpay.setTime_start(currTime);
        wxpay.setOut_trade_no(tradeNo);
        wxpay.setTradeSn(reqParam.getTradeSn());
        wxpay.setTotal_fee(reqParam.getOrderAmount());
        wxpay.setBody(reqParam.getGoodsName());
        wxpay.setT0Flag(reqParam.getT0Flag());
        wxpay.setGymchtId(reqParam.getGymchtId());
        wxpay.setGy_notifyUrl(reqParam.getNotifyUrl());
        byte[] byteSecret = AesUtil.encodeAES("17A060CF293B4D54".getBytes(), tradeNo.getBytes());
        String secretData = StringUtil.byte2hex(byteSecret);
        String codeUrl = String.valueOf(reqParam.getRootUrl()) + "/unionPay/" + secretData;
        response.setCode_url(codeUrl);
        response.setResultCode(ResponseEnum.SUCCESS.getCode());
        response.setMessage(ResponseEnum.SUCCESS.getMemo());
        this.wxNativeDao.save(wxpay);
        return response;
    }

    private TradeService switchService(String channelId) {
        if (channelId == null) {
            return null;
        }
        TradeService service = null;
        if ("wljr".equals(channelId)) {
            service = (TradeService) ContextUtil.getBean("wljrServiceImpl");
        } else if ("tfb".equals(channelId)) {
            service = (TradeService) ContextUtil.getBean("tfbServiceImpl");
        } else if ("daydaypay".equals(channelId)) {
            service = (TradeService) ContextUtil.getBean("daydayPayServiceImpl");
        } else if ("guangda".equals(channelId)) {
            service = (TradeService) ContextUtil.getBean("wxpayServiceImpl");
        } else if ("huika".equals(channelId)) {
            service = (TradeService) ContextUtil.getBean("huikaServiceImpl");
        } else if ("minsheng".equals(channelId)) {
            service = (TradeService) ContextUtil.getBean("minshengServiceImpl");
        } else if ("cupe".equals(channelId)) {
            service = (TradeService) ContextUtil.getBean("cupeServiceImpl");
        } else if ("ebusi".equals(channelId)) {
            service = (TradeService) ContextUtil.getBean("ebusiServiceImpl");
        } else if ("wezbank".equals(channelId)) {
            service = (TradeService) ContextUtil.getBean("wezbankServiceImpl");
        } else if ("helibao".equals(channelId)) {
            service = (TradeService) ContextUtil.getBean("helibaoServiceImpl");
        } else if ("hfbank".equals(channelId)) {
            service = (TradeService) ContextUtil.getBean("hfbankServiceImpl");
        } else if ("qiyepay".equals(channelId)) {
            service = (TradeService) ContextUtil.getBean("qiyepayServiceImpl");
        }
        return service;
    }

    protected ResponseQuery buildResponseQuery(WxpayScanCode wxpayScanCode) {
        ResponseQuery response = new ResponseQuery();
        response.setGymchtId(wxpayScanCode.getGymchtId());
        response.setTradeSn(wxpayScanCode.getTradeSn());
        response.setTransaction_id(wxpayScanCode.getOut_trade_no());
        if ("1".equals(SysParamUtil.getParam("out_transaction_id_return"))) {
            response.setOut_transaction_id(wxpayScanCode.getTransaction_id());
        }
        response.setOrderAmount(wxpayScanCode.getTotal_fee());
        response.setCoupon_fee(wxpayScanCode.getCoupon_fee());
        response.setBankType(wxpayScanCode.getBank_type());
        response.setT0Flag(wxpayScanCode.getT0Flag());
        response.setTimeEnd(wxpayScanCode.getTime_end());
        if (TradeStateEnum.SUCCESS.getCode().equals(wxpayScanCode.getTrade_state())) {
            response.setPay_result("0");
        } else if (!TradeStateEnum.NOTPAY.getCode().equals(wxpayScanCode.getTrade_state())) {
            response.setPay_result("1");
            response.setPay_info(wxpayScanCode.getTrade_state());
        }
        return response;
    }

    protected String tradeNotify(WxpayScanCode wxpayScanCode) {
        QrcodeMchtInfo qrcodeMcht = this.qrcodeMchtInfoDaoImpl.getMchtInfo(wxpayScanCode.getGymchtId());
        ResponseQuery backNotify = this.buildResponseQuery(wxpayScanCode);
        backNotify.setSign(GuangdaUtil.getMd5Sign(backNotify, qrcodeMcht.getSecretKey()));
        String jsonStr = JsonUtil.buildJson(backNotify);
        Environment evn = Environment.createEnvironment(wxpayScanCode.getGy_notifyUrl(), "utf-8", "application/json");
        log.info((Object) ("\u7b56\u7565\u56de\u8c03\u901a\u77e5:" + wxpayScanCode.getGy_notifyUrl() + "----" + jsonStr));
        String notifyBack = HttpUtility.postData(evn, jsonStr);
        log.info((Object) ("\u7b56\u7565\u56de\u8c03\u901a\u77e5:" + notifyBack));
        return notifyBack;
    }

    public String notifyUnsuccessTrade() {
        List<WxpayScanCode> list = this.wxNativeDao.listNotifyFailTrade();
        for (WxpayScanCode tradeBean : list) {
            this.tradeNotify(tradeBean);
        }
        return null;
    }
}
