/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.log4j.Logger
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.trade.service.impl;

import com.gy.system.Environment;
import com.gy.system.SysParamUtil;
import com.gy.util.DateUtil;
import com.gy.util.HttpUtility;
import com.gy.util.StringUtil;
import com.gy.util.UUIDGenerator;
import com.trade.bean.TradeDfbean;
import com.trade.bean.WxpayScanCode;
import com.trade.bean.dayday.DayPayqrcodeParamNew;
import com.trade.bean.dayday.DaypaySign;
import com.trade.bean.own.QrcodeChannelInf;
import com.trade.bean.own.QrcodeMchtInfo;
import com.trade.bean.own.TradeParam;
import com.trade.bean.response.ResponseCode;
import com.trade.bean.response.ResponseQuery;
import com.trade.dao.QrcodeMchtInfoDao;
import com.trade.dao.TradeDfDao;
import com.trade.dao.WxNativeDao;
import com.trade.enums.ResponseEnum;
import com.trade.enums.TradeStateEnum;
import com.trade.service.DaydayPayService;
import com.trade.service.impl.CommonService;
import com.trade.util.GuangdaUtil;
import com.trade.util.JsonUtil;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DaydayPayServiceImpl
        extends CommonService
        implements DaydayPayService {
    @Autowired
    private WxNativeDao wxNativeDao;
    @Autowired
    private TradeDfDao tradeDfDaoImpl;
    @Autowired
    private QrcodeMchtInfoDao qrcodeMchtInfoDaoImpl;
    private static Logger log = Logger.getLogger(DaydayPayServiceImpl.class);

    @Override
    public ResponseCode getDaypayQrCode(TradeParam reqParam, QrcodeChannelInf qrChannelInf) {
        ResponseCode response = new ResponseCode();
        response.setGymchtId(reqParam.getGymchtId());
        if (this.wxNativeDao.getByTradesn(reqParam.getTradeSn(), reqParam.getGymchtId()) != null) {
            response.setResultCode(ResponseEnum.FAIL_ORDER_NO_REPEAT.getCode());
            response.setMessage(ResponseEnum.FAIL_ORDER_NO_REPEAT.getMemo());
            return response;
        }
        WxpayScanCode wxpay = new WxpayScanCode();
        String tradeNo = UUIDGenerator.getOrderIdByUUId();
        String day = DateUtil.getCurrentDay();
        String time = DateUtil.getCurrentTime();
        DayPayqrcodeParamNew daypayParam = new DayPayqrcodeParamNew();
        daypayParam.setAgtId(qrChannelInf.getAgtId());
        daypayParam.setPayType("wcpay");
        daypayParam.setMerId(qrChannelInf.getChannel_mcht_no());
        daypayParam.setOrderAmt(String.valueOf(reqParam.getOrderAmount()));
        daypayParam.setOrderId(tradeNo);
        daypayParam.setProductName(StringUtil.encode(reqParam.getGoodsName(), "utf-8"));
        daypayParam.setNonceStr(StringUtil.getRandom(16));
        daypayParam.setPrealname(StringUtil.encode(reqParam.getRealname(), "utf-8"));
        daypayParam.setPidcard(reqParam.getIdcard());
        daypayParam.setPbankcardnum(reqParam.getBankcardnum());
        daypayParam.setPbankname(StringUtil.encode(reqParam.getBankname(), "utf-8"));
        daypayParam.setPsubbranch(StringUtil.encode(reqParam.getSubbranch(), "utf-8"));
        daypayParam.setPcnaps(reqParam.getPcnaps());
        daypayParam.setStlType("T0");
        daypayParam.setPfee((String) ((Object) reqParam.getFree()));
        daypayParam.setNotifyUrl(SysParamUtil.getParam("DAYPAY_NOTIFY_URL"));
        DaypaySign sign = new DaypaySign(GuangdaUtil.getMd5Sign(daypayParam, qrChannelInf.getSecret_key()));
        HashMap<String, Object> reqMap = new HashMap<String, Object>();
        reqMap.put("REQ_HEAD", sign);
        reqMap.put("REQ_BODY", daypayParam);
        String jsonStr = JsonUtil.buildJson(reqMap);
        String resultJson = HttpUtility.postData(Environment.DAYDAYPAY_NEW_GETQRCODE_URL, jsonStr);
        wxpay.setService(daypayParam.getPayType());
        wxpay.setMch_id(daypayParam.getMerId());
        wxpay.setMch_create_ip(reqParam.getRemoteAddr());
        wxpay.setChannel_id(qrChannelInf.getChannel_id());
        wxpay.setTrade_type(daypayParam.getPayType());
        wxpay.setTime_start(String.valueOf(day) + time);
        wxpay.setOut_trade_no(tradeNo);
        wxpay.setTradeSn(reqParam.getTradeSn());
        wxpay.setMch_create_ip(reqParam.getRemoteAddr());
        wxpay.setTotal_fee(reqParam.getOrderAmount());
        wxpay.setBody(reqParam.getGoodsName());
        wxpay.setNotify_url(daypayParam.getNotifyUrl());
        wxpay.setGymchtId(reqParam.getGymchtId());
        wxpay.setGy_notifyUrl(reqParam.getNotifyUrl());
        wxpay.setT0Flag(reqParam.getT0Flag());
        Map returnMap = (Map) JsonUtil.parseJson(resultJson);
        Map resultMap = (Map) JsonUtil.parseJson(returnMap.get("REP_BODY").toString());
        if (returnMap != null && resultMap != null) {
            String message;
            String resultCode = resultMap.get("rspcode") == null ? "" : resultMap.get("rspcode").toString();
            String string = message = resultMap.get("rspmsg") == null ? "" : StringUtil.decode(resultMap.get("rspmsg").toString(), "utf-8");
            if ("000000".equals(resultCode)) {
                response.setResultCode(ResponseEnum.SUCCESS.getCode());
                response.setMessage(ResponseEnum.SUCCESS.getMemo());
                response.setCode_url(resultMap.get("codeUrl").toString());
                String transactionId = resultMap.get("transactionId").toString();
                wxpay.setTransaction_id(transactionId);
                wxpay.setCode_url(resultMap.get("codeUrl").toString());
                wxpay.setTrade_state(TradeStateEnum.NOTPAY.getCode());
                TradeDfbean tradeDfBean = new TradeDfbean();
                tradeDfBean.setOut_trade_no(tradeNo);
                tradeDfBean.setTradeSn(reqParam.getTradeSn());
                tradeDfBean.setTransaction_id(transactionId);
                tradeDfBean.setRealname(reqParam.getRealname());
                tradeDfBean.setIdcard(reqParam.getIdcard());
                tradeDfBean.setBankcardnum(reqParam.getBankcardnum());
                tradeDfBean.setBankname(reqParam.getBankname());
                tradeDfBean.setSubbranch(reqParam.getSubbranch());
                tradeDfBean.setPcnaps(reqParam.getPcnaps());
                tradeDfBean.setStlType("T0");
                tradeDfBean.setMoney(reqParam.getOrderAmount());
                tradeDfBean.setFree(reqParam.getFree());
                tradeDfBean.setTime_start(String.valueOf(day) + time);
                tradeDfBean.setT0_status("0");
                this.tradeDfDaoImpl.save(tradeDfBean);
            } else {
                response.setResultCode(resultCode);
                response.setMessage(message);
            }
            wxpay.setResult_code(resultCode);
            wxpay.setMessage(message);
        } else {
            wxpay.setResult_code(ResponseEnum.BACK_EXCEPTION.getCode());
            wxpay.setMessage(ResponseEnum.BACK_EXCEPTION.getMemo());
            response.setResultCode(ResponseEnum.BACK_EXCEPTION.getCode());
            response.setMessage(ResponseEnum.BACK_EXCEPTION.getMemo());
        }
        this.wxNativeDao.save(wxpay);
        return response;
    }

    @Override
    public WxpayScanCode queryByTradesn(String tradeSn, String gyMchtId, QrcodeChannelInf qrChannelInf) {
        WxpayScanCode wxpayScanCode = this.wxNativeDao.getByTradesn(tradeSn, gyMchtId);
        if (wxpayScanCode != null && StringUtil.isNotEmpty(wxpayScanCode.getCode_url()) && !TradeStateEnum.SUCCESS.getCode().equals(wxpayScanCode.getTrade_state())) {
            String backSign;
            String day = DateUtil.getCurrentDay();
            String time = DateUtil.getCurrentTime();
            HashMap<String, String> queryParam = new HashMap<String, String>();
            queryParam.put("agtId", qrChannelInf.getAgtId());
            queryParam.put("merId", qrChannelInf.getChannel_mcht_no());
            queryParam.put("orderId", wxpayScanCode.getOut_trade_no());
            queryParam.put("nonceStr", StringUtil.getRandom(16));
            queryParam.put("payType", "wcpay");
            DaypaySign sign = new DaypaySign(GuangdaUtil.getMd5SignByMap(queryParam, qrChannelInf.getSecret_key(), new String[0]));
            HashMap<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("REQ_HEAD", sign);
            reqMap.put("REQ_BODY", queryParam);
            String jsonStr = JsonUtil.buildJson(reqMap);
            String resultJson = HttpUtility.postData(Environment.DAYDAYPAY_NEW_QUERY_URL, jsonStr);
            Map returnMap = (Map) JsonUtil.parseJson(resultJson);
            Map resultMap = (Map) JsonUtil.parseJson(StringUtil.trans2Str(returnMap.get("REP_BODY")));
            Map headMap = (Map) JsonUtil.parseJson(StringUtil.trans2Str(returnMap.get("REP_HEAD")));
            String string = backSign = headMap == null ? "" : StringUtil.trans2Str(headMap.get("sign"));
            if (backSign == null || !backSign.equals(GuangdaUtil.getMd5SignByMap(resultMap, qrChannelInf.getSecret_key(), new String[0]))) {
                return wxpayScanCode;
            }
            if (resultMap != null && resultMap != null && "000000".equals(resultMap.get("rspcode"))) {
                String currTime = DateUtil.getCurrTime();
                if ("01".equals(resultMap.get("orderState"))) {
                    wxpayScanCode.setPay_result("0");
                    wxpayScanCode.setTime_end(currTime);
                    wxpayScanCode.setTrade_state(TradeStateEnum.SUCCESS.getCode());
                    this.wxNativeDao.update(wxpayScanCode);
                } else if ("02".equals(resultMap.get("orderState"))) {
                    wxpayScanCode.setTrade_state(TradeStateEnum.PAYERROR.getCode());
                    this.wxNativeDao.update(wxpayScanCode);
                }
                TradeDfbean tradeDfBean = this.tradeDfDaoImpl.get(resultMap.get("orderId").toString());
                if (tradeDfBean != null) {
                    tradeDfBean.setT0RespCode(StringUtil.trans2Str(resultMap.get("t0PayforRep")));
                    tradeDfBean.setT0RespDesc(StringUtil.decode(StringUtil.trans2Str(resultMap.get("t0PayforMsg")), "utf-8"));
                    tradeDfBean.setFee(StringUtil.parseInt(StringUtil.trans2Str(resultMap.get("fee"))));
                    if ("00".equals(tradeDfBean.getT0RespCode())) {
                        tradeDfBean.setT0_status("2");
                        tradeDfBean.setTime_end(currTime);
                    }
                    this.tradeDfDaoImpl.update(tradeDfBean);
                }
            }
        }
        return wxpayScanCode;
    }

    @Override
    public String saveResultNotify(Map returnMap) {
        Map resultMap = (Map) JsonUtil.parseJson(StringUtil.trans2Str(returnMap.get("REP_BODY")));
        WxpayScanCode notifyBeanTemp = this.wxNativeDao.getById(resultMap.get("orderId").toString());
        TradeDfbean tradeDfBean = this.tradeDfDaoImpl.get(resultMap.get("orderId").toString());
        HashMap<String, String> replyNotify = new HashMap<String, String>();
        if (notifyBeanTemp == null) {
            replyNotify.put("state", "notexist");
            return JsonUtil.buildJson4Map(replyNotify);
        }
        String notifyBack = "";
        String currTime = DateUtil.getCurrTime();
        String t0RespCode = StringUtil.trans2Str(resultMap.get("t0PayforRep"));
        String t0RespDesc = StringUtil.decode(StringUtil.trans2Str(resultMap.get("t0PayforMsg")), "utf-8");
        if (!TradeStateEnum.SUCCESS.getCode().equals(notifyBeanTemp.getTrade_state())) {
            if (tradeDfBean != null) {
                tradeDfBean.setT0RespCode(t0RespCode);
                tradeDfBean.setT0RespDesc(t0RespDesc);
                tradeDfBean.setFee(StringUtil.parseInt(StringUtil.trans2Str(resultMap.get("fee"))));
                if ("00".equals(tradeDfBean.getT0RespCode())) {
                    tradeDfBean.setT0_status("2");
                    tradeDfBean.setTime_end(currTime);
                }
                this.tradeDfDaoImpl.update(tradeDfBean);
            }
            if ("01".equals(resultMap.get("orderState"))) {
                notifyBeanTemp.setPay_result("0");
                notifyBeanTemp.setTime_end(currTime);
                notifyBeanTemp.setTrade_state(TradeStateEnum.SUCCESS.getCode());
            } else if ("02".equals(resultMap.get("orderState"))) {
                notifyBeanTemp.setTrade_state(TradeStateEnum.PAYERROR.getCode());
            }
        }
        if (notifyBeanTemp.getGy_notifyUrl() != null && !"success".equals(notifyBeanTemp.getGynotify_back())) {
            Environment evn = Environment.createEnvironment(notifyBeanTemp.getGy_notifyUrl(), "utf-8", "application/json");
            QrcodeMchtInfo qrcodeMcht = this.qrcodeMchtInfoDaoImpl.getMchtInfo(notifyBeanTemp.getGymchtId());
            ResponseQuery backNotify = this.buildResponseQuery(notifyBeanTemp);
            backNotify.setT0RespCode(t0RespCode);
            backNotify.setT0RespDesc(t0RespDesc);
            if ("00".equals(t0RespCode)) {
                backNotify.setT0_status("2");
            } else {
                backNotify.setT0_status("0");
            }
            if ("01".equals(resultMap.get("orderState"))) {
                backNotify.setPay_result("0");
                backNotify.setTimeEnd(currTime);
            } else {
                backNotify.setPay_result(StringUtil.trans2Str(resultMap.get("orderState")));
            }
            backNotify.setSign(GuangdaUtil.getMd5Sign(backNotify, qrcodeMcht.getSecretKey()));
            String jsonStr = JsonUtil.buildJson(backNotify);
            log.info((Object) ("\u56de\u8c03\u63a5\u5165\u65b9:" + notifyBeanTemp.getGy_notifyUrl() + "----" + jsonStr));
            notifyBack = HttpUtility.postData(evn, jsonStr);
            log.info((Object) ("\u56de\u8c03\u63a5\u5165\u65b9\u8fd4\u56de:" + notifyBack));
        }
        notifyBeanTemp.setGynotify_back(notifyBack != null && notifyBack.length() > 20 ? notifyBack.substring(0, 20) : notifyBack);
        this.wxNativeDao.update(notifyBeanTemp);
        if (!"success".equals(notifyBack)) {
            replyNotify.put("state", "channel_fail");
            return JsonUtil.buildJson4Map(replyNotify);
        }
        replyNotify.put("state", "OK");
        return JsonUtil.buildJson4Map(replyNotify);
    }

    private String bakSaveResultNotifyy(Map resultMap) {
        return "";
    }

    @Override
    public TradeDfbean getDfBean(String outTradeNo) {
        return this.tradeDfDaoImpl.get(outTradeNo);
    }
}
