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
import com.gy.util.DateUtil;
import com.gy.util.Dom4jUtil;
import com.gy.util.HttpUtility;
import com.gy.util.StringUtil;
import com.gy.util.UUIDGenerator;
import com.trade.bean.QuickpayBean;
import com.trade.bean.WxpayScanCode;
import com.trade.bean.own.QrcodeChannelInf;
import com.trade.bean.own.QrcodeMchtInfo;
import com.trade.bean.own.QuickpayParam;
import com.trade.bean.own.TradeParam;
import com.trade.bean.response.QuickpayResponse;
import com.trade.bean.response.ResponseCode;
import com.trade.bean.response.ResponseQuery;
import com.trade.dao.QrcodeMchtInfoDao;
import com.trade.dao.QuickpayDao;
import com.trade.dao.WxNativeDao;
import com.trade.enums.CardTypeEnum;
import com.trade.enums.ResponseEnum;
import com.trade.enums.TradeSourceEnum;
import com.trade.enums.TradeStateEnum;
import com.trade.service.NetpayService;
import com.trade.service.TradeService;
import com.trade.service.impl.CommonService;
import com.trade.service.netpayimpl.NetpayHandlerService;
import com.trade.util.GuangdaUtil;
import com.trade.util.JsonUtil;
import com.trade.util.RSAUtils;

import java.io.PrintStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TfbServiceImpl
        extends CommonService
        implements TradeService,
        NetpayService {
    @Autowired
    private WxNativeDao wxNativeDao;
    @Autowired
    private QrcodeMchtInfoDao qrcodeMchtInfoDaoImpl;
    @Autowired
    private QuickpayDao quickpayDao;
    private static Logger log = Logger.getLogger(TfbServiceImpl.class);

    @Override
    public ResponseCode doTrade(TradeParam reqParam, QrcodeChannelInf qrChannelInf) {
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
        String payType = "800201";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("spid", qrChannelInf.getChannel_mcht_no());
        params.put("notify_url", SysParamUtil.getParam("TFB_NOTIFY_URL"));
        params.put("sp_billno", tradeNo);
        params.put("spbill_create_ip", reqParam.getRemoteAddr());
        params.put("pay_type", payType);
        params.put("tran_time", currTime);
        params.put("tran_amt", String.valueOf(reqParam.getOrderAmount()));
        params.put("cur_type", "CNY");
        params.put("item_name", reqParam.getGoodsName());
        params.put("item_attach", "\u9644\u52a0\u5c5e\u6027");
        Environment env = Environment.TFB_REQUEST_PAY_URL;
        if (TradeSourceEnum.ALIPAY.getCode().equals(reqParam.getTradeSource())) {
            env = Environment.TFB_REQUEST_ALIPAY_URL;
            wxpay.setTrade_source(TradeSourceEnum.ALIPAY.getCode());
        } else {
            params.put("bank_mch_name", "\u5929\u5929\u652f\u4ed8");
            params.put("bank_mch_id", "8888888");
            wxpay.setTrade_source(TradeSourceEnum.WEPAY.getCode());
        }
        params.put("sign", GuangdaUtil.getMd5SignByMap(params, qrChannelInf.getSecret_key(), "GBK"));
        params.put("sign_type", "MD5");
        params.put("ver", "1");
        params.put("input_charset", "GBK");
        params.put("sign_key_index", "1");
        String keyValue = GuangdaUtil.map2HttpParam(params);
        wxpay.setService("tfb");
        wxpay.setMch_id(qrChannelInf.getChannel_mcht_no());
        wxpay.setMch_create_ip(reqParam.getRemoteAddr());
        wxpay.setChannel_id(qrChannelInf.getChannel_id());
        wxpay.setTrade_type(payType);
        wxpay.setTime_start(currTime);
        wxpay.setOut_trade_no(tradeNo);
        wxpay.setTradeSn(reqParam.getTradeSn());
        wxpay.setTotal_fee(reqParam.getOrderAmount());
        wxpay.setBody(reqParam.getGoodsName());
        wxpay.setNotify_url(SysParamUtil.getParam("TFB_NOTIFY_URL"));
        wxpay.setT0Flag(reqParam.getT0Flag());
        wxpay.setGymchtId(reqParam.getGymchtId());
        wxpay.setGy_notifyUrl(reqParam.getNotifyUrl());
        String resultXml = HttpUtility.postData(env, keyValue);
        Map<String, String> returnMap = Dom4jUtil.parseXml2Map(resultXml);
        String resultCode = StringUtil.trans2Str(returnMap.get("retcode"));
        String resultMsg = StringUtil.trans2Str(returnMap.get("retmsg"));
        if ("00".equals(resultCode)) {
            response.setResultCode(ResponseEnum.SUCCESS.getCode());
            response.setCode_url(StringUtil.trans2Str(returnMap.get("qrcode")));
            wxpay.setTransaction_id(StringUtil.trans2Str(returnMap.get("listid")));
            wxpay.setCode_url(response.getCode_url());
            wxpay.setTrade_state(TradeStateEnum.NOTPAY.getCode());
            wxpay.setResp_code("00");
        } else {
            response.setResultCode(resultCode);
            response.setMessage(resultMsg);
        }
        wxpay.setResult_code(resultCode);
        wxpay.setMessage(resultMsg);
        this.wxNativeDao.save(wxpay);
        return response;
    }

    @Override
    public ResponseCode doTrade(WxpayScanCode wxpayScanCode) {
        ResponseCode response = new ResponseCode();
        QrcodeChannelInf qrChannelInf = this.qrcodeMchtInfoDaoImpl.getChannelInf(wxpayScanCode.getChannel_id(), wxpayScanCode.getMch_id());
        if (qrChannelInf == null) {
            response.setResultCode(ResponseEnum.ERROR_CHANNEL.getCode());
            response.setMessage(ResponseEnum.ERROR_CHANNEL.getMemo());
            return response;
        }
        String currTime = DateUtil.getCurrTime();
        String payType = "800201";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("spid", qrChannelInf.getChannel_mcht_no());
        params.put("notify_url", SysParamUtil.getParam("TFB_NOTIFY_URL"));
        params.put("sp_billno", wxpayScanCode.getOut_trade_no());
        params.put("spbill_create_ip", wxpayScanCode.getMch_create_ip());
        params.put("pay_type", payType);
        params.put("tran_time", currTime);
        params.put("tran_amt", String.valueOf(wxpayScanCode.getTotal_fee()));
        params.put("cur_type", "CNY");
        params.put("item_name", wxpayScanCode.getBody());
        params.put("item_attach", "\u9644\u52a0\u5c5e\u6027");
        Environment env = Environment.TFB_REQUEST_PAY_URL;
        if (TradeSourceEnum.ALIPAY.getCode().equals(wxpayScanCode.getTrade_source())) {
            env = Environment.TFB_REQUEST_ALIPAY_URL;
        } else {
            params.put("bank_mch_name", "\u5929\u5929\u652f\u4ed8");
            params.put("bank_mch_id", "8888888");
        }
        params.put("sign", GuangdaUtil.getMd5SignByMap(params, qrChannelInf.getSecret_key(), "GBK"));
        params.put("sign_type", "MD5");
        params.put("ver", "1");
        params.put("input_charset", "GBK");
        params.put("sign_key_index", "1");
        String keyValue = GuangdaUtil.map2HttpParam(params);
        String resultXml = HttpUtility.postData(env, keyValue);
        Map<String, String> returnMap = Dom4jUtil.parseXml2Map(resultXml);
        String resultCode = StringUtil.trans2Str(returnMap.get("retcode"));
        String resultMsg = StringUtil.trans2Str(returnMap.get("retmsg"));
        if ("00".equals(resultCode)) {
            response.setResultCode(ResponseEnum.SUCCESS.getCode());
            response.setCode_url(StringUtil.trans2Str(returnMap.get("qrcode")));
            wxpayScanCode.setTransaction_id(StringUtil.trans2Str(returnMap.get("listid")));
            wxpayScanCode.setCode_url(response.getCode_url());
            wxpayScanCode.setTrade_state(TradeStateEnum.NOTPAY.getCode());
            wxpayScanCode.setResp_code("00");
        } else {
            response.setResultCode(resultCode);
            response.setMessage(resultMsg);
        }
        wxpayScanCode.setTrade_type(payType);
        wxpayScanCode.setService("tfb");
        wxpayScanCode.setResult_code(resultCode);
        wxpayScanCode.setMessage(resultMsg);
        this.wxNativeDao.update(wxpayScanCode);
        return response;
    }

    @Override
    public WxpayScanCode queryByTradesn(String tradeSn, String gyMchtId, QrcodeChannelInf qrChannelInf) {
        WxpayScanCode wxpayScanCode = this.wxNativeDao.getByTradesn(tradeSn, gyMchtId);
        return this.doQuery(wxpayScanCode, qrChannelInf);
    }

    @Override
    public WxpayScanCode queryFromChannel(WxpayScanCode wxpayScanCode, QrcodeChannelInf qrChannelInf) {
        return this.doQuery(wxpayScanCode, qrChannelInf);
    }

    private WxpayScanCode doQuery(WxpayScanCode wxpayScanCode, QrcodeChannelInf qrChannelInf) {
        if (wxpayScanCode != null && StringUtil.isNotEmpty(wxpayScanCode.getCode_url()) && !TradeStateEnum.SUCCESS.getCode().equals(wxpayScanCode.getTrade_state())) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("spid", qrChannelInf.getChannel_mcht_no());
            params.put("sp_billno", wxpayScanCode.getOut_trade_no());
            params.put("sign", GuangdaUtil.getMd5SignByMap(params, qrChannelInf.getSecret_key(), "GBK"));
            params.put("sign_type", "MD5");
            params.put("ver", "1");
            params.put("input_charset", "GBK");
            params.put("sign_key_index", "1");
            String keyValue = GuangdaUtil.map2HttpParam(params);
            String resultXml = HttpUtility.postData(Environment.TFB_QUERY_PAY_URL, keyValue);
            Map<String, String> returnMap = Dom4jUtil.parseXml2Map(resultXml);
            if (returnMap != null && "00".equals(returnMap.get("retcode"))) {
                Map<String, String> resultMap = null;
                if (returnMap.get("data") != null) {
                    Map<String, String> data = Dom4jUtil.parseXml2Map(returnMap.get("data"));
                    resultMap = Dom4jUtil.parseXml2Map(data.get("record"));
                }
                if (resultMap != null && "3".equals(resultMap.get("state"))) {
                    wxpayScanCode.setPay_result("0");
                    wxpayScanCode.setTime_end(resultMap.get("pay_time"));
                    wxpayScanCode.setTrade_state(TradeStateEnum.SUCCESS.getCode());
                    this.wxNativeDao.update(wxpayScanCode);
                } else if (resultMap != null && ("1".equals(resultMap.get("state")) || "2".equals(resultMap.get("state")))) {
                    wxpayScanCode.setTrade_state(TradeStateEnum.NOTPAY.getCode());
                    this.wxNativeDao.update(wxpayScanCode);
                } else {
                    wxpayScanCode.setTrade_state(TradeStateEnum.PAYERROR.getCode());
                    this.wxNativeDao.update(wxpayScanCode);
                }
            }
        }
        return wxpayScanCode;
    }

    @Override
    public String saveResultNotify(Map<String, String> resultMap) {
        WxpayScanCode notifyBeanTemp = this.wxNativeDao.getById(resultMap.get("sp_billno"));
        if (notifyBeanTemp == null || !String.valueOf(notifyBeanTemp.getTotal_fee()).equals(resultMap.get("tran_amt"))) {
            return "notexist";
        }
        String notifyBack = "";
        if (!TradeStateEnum.SUCCESS.getCode().equals(notifyBeanTemp.getTrade_state())) {
            if ("1".equals(resultMap.get("tran_state"))) {
                notifyBeanTemp.setTrade_state(TradeStateEnum.SUCCESS.getCode());
                this.tradeMchtAccountService.notifySuccess(notifyBeanTemp);
            } else {
                notifyBeanTemp.setTrade_state(TradeStateEnum.PAYERROR.getCode());
            }
            notifyBeanTemp.setTime_end(DateUtil.getCurrTime());
        }
        if (notifyBeanTemp.getGy_notifyUrl() != null && !"success".equals(notifyBeanTemp.getGynotify_back())) {
            Environment evn = Environment.createEnvironment(notifyBeanTemp.getGy_notifyUrl(), "utf-8", "application/json");
            QrcodeMchtInfo qrcodeMcht = this.qrcodeMchtInfoDaoImpl.getMchtInfo(notifyBeanTemp.getGymchtId());
            ResponseQuery backNotify = this.buildResponseQuery(notifyBeanTemp);
            if ("1".equals(resultMap.get("tran_state"))) {
                backNotify.setPay_result("0");
                backNotify.setTimeEnd(notifyBeanTemp.getTime_end());
            } else {
                backNotify.setPay_result(resultMap.get("retcode"));
                backNotify.setPay_info(resultMap.get("retmsg"));
            }
            backNotify.setSign(GuangdaUtil.getMd5Sign(backNotify, qrcodeMcht.getSecretKey()));
            String jsonStr = JsonUtil.buildJson(backNotify);
            log.info((Object) ("\u56de\u8c03\u63a5\u5165\u65b9:" + notifyBeanTemp.getGy_notifyUrl() + "----" + jsonStr));
            notifyBack = HttpUtility.postData(evn, jsonStr);
            log.info((Object) ("\u56de\u8c03\u63a5\u5165\u65b9\u8fd4\u56de:" + notifyBack));
            notifyBeanTemp.setNotify_data(jsonStr);
            this.buildNotifyBack(notifyBeanTemp, notifyBack);
        }
        this.wxNativeDao.update(notifyBeanTemp);
        return "SUCCESS";
    }

    @Override
    public ResponseCode getWxjspay(TradeParam reqParam, QrcodeChannelInf qrChannelInf) {
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
        String payType = "800207";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("spid", qrChannelInf.getChannel_mcht_no());
        params.put("notify_url", SysParamUtil.getParam("TFB_NOTIFY_URL"));
        params.put("pay_show_url", reqParam.getCallback_url());
        params.put("sp_billno", tradeNo);
        params.put("spbill_create_ip", reqParam.getRemoteAddr());
        params.put("pay_type", payType);
        params.put("tran_time", currTime);
        params.put("tran_amt", String.valueOf(reqParam.getOrderAmount()));
        params.put("cur_type", "CNY");
        params.put("item_name", reqParam.getGoodsName());
        params.put("item_attach", "\u9644\u52a0\u5c5e\u6027");
        params.put("bank_mch_name", "\u5929\u5929\u652f\u4ed8");
        params.put("bank_mch_id", "8888888");
        params.put("sign", GuangdaUtil.getMd5SignByMap(params, qrChannelInf.getSecret_key(), "utf-8"));
        params.put("sign_type", "MD5");
        params.put("ver", "1");
        params.put("input_charset", "GBK");
        params.put("sign_key_index", "1");
        String keyValue = GuangdaUtil.map2HttpParam(params);
        wxpay.setService("tfb");
        wxpay.setMch_id(qrChannelInf.getChannel_mcht_no());
        wxpay.setMch_create_ip(reqParam.getRemoteAddr());
        wxpay.setChannel_id(qrChannelInf.getChannel_id());
        wxpay.setTrade_type(payType);
        wxpay.setTime_start(currTime);
        wxpay.setOut_trade_no(tradeNo);
        wxpay.setTradeSn(reqParam.getTradeSn());
        wxpay.setMch_create_ip(reqParam.getRemoteAddr());
        wxpay.setTotal_fee(reqParam.getOrderAmount());
        wxpay.setBody(reqParam.getGoodsName());
        wxpay.setNotify_url(SysParamUtil.getParam("TFB_NOTIFY_URL"));
        wxpay.setT0Flag(reqParam.getT0Flag());
        wxpay.setGymchtId(reqParam.getGymchtId());
        wxpay.setGy_notifyUrl(reqParam.getNotifyUrl());
        response.setResultCode(ResponseEnum.SUCCESS.getCode());
        response.setMessage(ResponseEnum.SUCCESS.getMemo());
        response.setPay_info(String.valueOf(Environment.TFB_JS_PAY_URL.getBaseUrl()) + "?" + keyValue);
        return response;
    }

    public void df() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("version", "1.0");
        params.put("spid", "1800920573");
        params.put("sp_serialno", UUIDGenerator.getOrderIdByUUId());
        params.put("sp_reqtime", DateUtil.getCurrTime());
        params.put("tran_amt", "1");
        params.put("cur_type", "1");
        params.put("pay_type", "1");
        params.put("acct_name", "\u53f6\u5efa\u6587");
        params.put("acct_id", "6214830202948815");
        params.put("acct_type", "0");
        params.put("bank_name", "\u62db\u5546\u94f6\u884c");
        params.put("business_type", "20101");
        params.put("memo", "\u5355\u7b14\u4ee3\u4ed8");
        Environment env = Environment.TFB_SINGLE_PAY_URL;
        params.put("sign", GuangdaUtil.getMd5SignByMap(params, "h.Mk62HRF8", "UTF-8").toLowerCase());
        String keyValue = GuangdaUtil.map2HttpParam(params);
        String cipher_data = RSAUtils.encrypt(keyValue, new String[0]);
        System.out.println("keyValue:" + keyValue);
        System.out.println("cipher_data:" + cipher_data);
        String secretResult = HttpUtility.postData(env, "cipher_data=" + URLEncoder.encode(cipher_data));
        if (StringUtil.isEmpty(secretResult)) {
            System.out.println(String.valueOf(ResponseEnum.BACK_EXCEPTION.getCode()) + ResponseEnum.BACK_EXCEPTION.getMemo());
        }
        String resultXml = RSAUtils.decryptResponseData(secretResult, new String[0]);
        System.out.println(resultXml);
        Map<String, String> returnMap = Dom4jUtil.parseXml2Map(resultXml);
        String resultCode = StringUtil.trans2Str(returnMap.get("retcode"));
        String resultMsg = StringUtil.trans2Str(returnMap.get("retmsg"));
        "00".equals(resultCode);
    }

    @Override
    public QuickpayBean netpayApply(QuickpayParam reqParam, QrcodeChannelInf qrChannelInf) {
        QuickpayBean tradeInfo = new QuickpayBean();
        String tradeNo = UUIDGenerator.getOrderIdByUUId("4001");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("spid", qrChannelInf.getChannel_mcht_no());
        params.put("sp_userid", qrChannelInf.getChannel_mcht_no());
        params.put("spbillno", tradeNo);
        params.put("money", (String) ((Object) reqParam.getOrderAmount()));
        params.put("cur_type", "1");
        params.put("return_url", SysParamUtil.getParam("TFB_NETPAY_CALLBACK_URL"));
        params.put("notify_url", SysParamUtil.getParam("TFB_NETPAY_NOTIFY_URL"));
        params.put("memo", reqParam.getGoodsName());
        if (!StringUtil.isEmpty(reqParam.getExpirySecond())) {
            params.put("expire_time", reqParam.getExpirySecond());
        }
        if (CardTypeEnum.DEBIT.getCode().equals(reqParam.getCardType())) {
            params.put("card_type", "1");
        } else if (CardTypeEnum.CREDIT.getCode().equals(reqParam.getCardType())) {
            params.put("card_type", "2");
        }
        params.put("bank_segment", reqParam.getBankSegment());
        params.put("user_type", "1");
        params.put("channel", reqParam.getChannelType());
        params.put("encode_type", "MD5");
        params.put("sign", GuangdaUtil.getMd5SignByMap(params, qrChannelInf.getSecret_key(), "utf-8").toLowerCase());
        String keyValue = GuangdaUtil.map2HttpParam(params);
        String cipher_data = RSAUtils.encrypt(keyValue, new String[0]);
        String url = String.valueOf(Environment.TFB_EBANK_APPLY_URL.getBaseUrl()) + "?cipher_data=" + URLEncoder.encode(cipher_data);
        tradeInfo.setOut_trade_no(tradeNo);
        tradeInfo.setVersion("1.0.1");
        tradeInfo.setTime_start(DateUtil.getCurrTime());
        tradeInfo.setTrade_state(TradeStateEnum.NOTPAY.getCode());
        tradeInfo.setPayUrl(url);
        tradeInfo.setResp_code("00");
        return tradeInfo;
    }

    @Override
    public String saveNetpayNotify(Map<String, String> resultMap) {
        QuickpayBean quickpayBean = this.quickpayDao.getById(resultMap.get("spbillno"));
        if (quickpayBean == null || !String.valueOf(quickpayBean.getTotal_fee()).equals(resultMap.get("money"))) {
            return "notexist";
        }
        String notifyBack = "";
        if (!TradeStateEnum.SUCCESS.getCode().equals(quickpayBean.getTrade_state())) {
            if ("1".equals(resultMap.get("result"))) {
                quickpayBean.setTrade_state(TradeStateEnum.SUCCESS.getCode());
                String cardType = "";
                if ("2".equals(resultMap.get("pay_type"))) {
                    cardType = CardTypeEnum.DEBIT.getCode();
                } else if ("3".equals(resultMap.get("pay_type"))) {
                    cardType = CardTypeEnum.CREDIT.getCode();
                }
                quickpayBean.setCardType(cardType);
                this.tradeMchtAccountService.notifyQucikpaySuccess(quickpayBean);
            } else {
                quickpayBean.setTrade_state(TradeStateEnum.PAYERROR.getCode());
            }
            quickpayBean.setTime_end(DateUtil.getCurrTime());
            quickpayBean.setTransaction_id(resultMap.get("listid"));
        }
        if (quickpayBean.getGy_notifyUrl() != null && !"success".equals(quickpayBean.getGynotify_back())) {
            Environment evn = Environment.createEnvironment(quickpayBean.getGy_notifyUrl(), "utf-8", "application/json");
            QrcodeMchtInfo qrcodeMcht = this.qrcodeMchtInfoDaoImpl.getMchtInfo(quickpayBean.getGymchtId());
            QuickpayResponse backNotify = NetpayHandlerService.buildResponseQuery(quickpayBean);
            backNotify.setSign(GuangdaUtil.getMd5Sign(backNotify, qrcodeMcht.getSecretKey()));
            String jsonStr = JsonUtil.buildJson(backNotify);
            log.info((Object) ("\u56de\u8c03\u63a5\u5165\u65b9:" + quickpayBean.getGy_notifyUrl() + "----" + jsonStr));
            notifyBack = HttpUtility.postData(evn, jsonStr);
            log.info((Object) ("\u56de\u8c03\u63a5\u5165\u65b9\u8fd4\u56de:" + notifyBack));
            quickpayBean.setNotify_data(jsonStr);
            NetpayHandlerService.buildNotifyBack(quickpayBean, notifyBack);
        }
        this.quickpayDao.update(quickpayBean);
        return "<retcode>00</retcode>";
    }

    @Override
    public String getNetpayCallBack(Map<String, String> resultMap) {
        QuickpayBean quickpayBean = this.quickpayDao.getById(resultMap.get("spbillno"));
        String callBackUrl = "";
        callBackUrl = String.valueOf(callBackUrl) + ((callBackUrl = quickpayBean.getCallback_url()).contains("?") ? "&" : "?");
        String params = "";
        QrcodeMchtInfo qrcodeMcht = this.qrcodeMchtInfoDaoImpl.getMchtInfo(quickpayBean.getGymchtId());
        if ("1".equals(resultMap.get("result"))) {
            if (!TradeStateEnum.SUCCESS.getCode().equals(quickpayBean.getTrade_state())) {
                this.saveNetpayNotify(resultMap);
            }
            quickpayBean.setTrade_state(TradeStateEnum.SUCCESS.getCode());
        }
        params = NetpayHandlerService.buildCallbackParam(quickpayBean, qrcodeMcht);
        callBackUrl = String.valueOf(callBackUrl) + params;
        return callBackUrl;
    }

    @Override
    public QuickpayBean queryNetpay(String tradeSn, String gyMchtId, QrcodeChannelInf qrChannelInf) {
        QuickpayBean quickpayBean = this.quickpayDao.getByTradesn(tradeSn, gyMchtId);
        return this.queryNetpayFromChannel(quickpayBean, qrChannelInf);
    }

    @Override
    public QuickpayBean queryNetpayFromChannel(QuickpayBean quickpayBean, QrcodeChannelInf qrChannelInf) {
        if (quickpayBean != null && !TradeStateEnum.SUCCESS.getCode().equals(quickpayBean.getTrade_state())) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("spid", qrChannelInf.getChannel_mcht_no());
            params.put("spbillno", quickpayBean.getOut_trade_no());
            params.put("channel", quickpayBean.getDevice_info());
            params.put("encode_type", "MD5");
            params.put("sign", GuangdaUtil.getMd5SignByMap(params, qrChannelInf.getSecret_key(), "utf-8").toLowerCase());
            String keyValue = GuangdaUtil.map2HttpParam(params);
            String cipher_data = RSAUtils.encrypt(keyValue, new String[0]);
            String resultXml = HttpUtility.postData(Environment.TFB_EBANK_QUEYR_URL, "cipher_data=" + URLEncoder.encode(cipher_data));
            Map<String, String> returnMap = Dom4jUtil.parseXml2Map(resultXml);
            if (returnMap != null && "00".equals(returnMap.get("retcode"))) {
                String back_cipher_data = RSAUtils.decryptResponseData(returnMap.get("cipher_data"), new String[0]);
                Map<String, String> resultMap = null;
                if (back_cipher_data != null) {
                    resultMap = GuangdaUtil.httpParam2Map(back_cipher_data);
                }
                if (resultMap != null) {
                    quickpayBean.setTransaction_id(resultMap.get("listid"));
                    if ("1".equals(resultMap.get("result"))) {
                        quickpayBean.setPay_result("0");
                        quickpayBean.setTime_end(DateUtil.getCurrTime());
                        quickpayBean.setTrade_state(TradeStateEnum.SUCCESS.getCode());
                    } else if ("2".equals(resultMap.get("result"))) {
                        quickpayBean.setTrade_state(TradeStateEnum.NOTPAY.getCode());
                    } else if ("3".equals(resultMap.get("result"))) {
                        quickpayBean.setTrade_state(TradeStateEnum.CLOSED.getCode());
                    }
                    this.quickpayDao.update(quickpayBean);
                }
            }
        }
        return quickpayBean;
    }

    public static void main(String[] args) {
        new TfbServiceImpl().df();
    }
}
