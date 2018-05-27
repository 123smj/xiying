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
import com.gy.util.CommonFunction;
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
import com.trade.enums.TradeStateEnum;
import com.trade.service.MinshengService;
import com.trade.service.impl.CommonService;
import com.trade.util.GuangdaUtil;
import com.trade.util.JsonUtil;
import com.trade.util.SM2Utils;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MinshengServiceImpl
        extends CommonService
        implements MinshengService {
    @Autowired
    private WxNativeDao wxNativeDao;
    @Autowired
    private QrcodeMchtInfoDao qrcodeMchtInfoDaoImpl;
    private String platformId = "cust0001";
    private static Logger log = Logger.getLogger(MinshengServiceImpl.class);

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
        String tradeType = TradeTypesEnum.API_WXQRCODE.getCode();
        String timeStart = DateUtil.getCurrTime();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("platformId", this.platformId);
        params.put("merchantNo", qrChannelInf.getChannel_mcht_no());
        params.put("selectTradeType", tradeType);
        params.put("amount", String.valueOf(reqParam.getOrderAmount()));
        params.put("orderInfo", reqParam.getGoodsName());
        params.put("merchantSeq", tradeNo);
        params.put("transDate", DateUtil.getCurrentDay());
        params.put("transTime", timeStart);
        params.put("notifyUrl", SysParamUtil.getParam("MINSHENG_NOTIFY_URL"));
        wxpay.setService(tradeType);
        wxpay.setMch_id(qrChannelInf.getChannel_mcht_no());
        wxpay.setMch_create_ip(reqParam.getRemoteAddr());
        wxpay.setChannel_id(qrChannelInf.getChannel_id());
        wxpay.setTrade_type(tradeType);
        wxpay.setTime_start(timeStart);
        wxpay.setOut_trade_no(tradeNo);
        wxpay.setTradeSn(reqParam.getTradeSn());
        wxpay.setMch_create_ip(reqParam.getRemoteAddr());
        wxpay.setTotal_fee(reqParam.getOrderAmount());
        wxpay.setBody(reqParam.getGoodsName());
        wxpay.setNotify_url(SysParamUtil.getParam("MINSHENG_NOTIFY_URL"));
        wxpay.setT0Flag(reqParam.getT0Flag());
        wxpay.setGymchtId(reqParam.getGymchtId());
        wxpay.setGy_notifyUrl(reqParam.getNotifyUrl());
        String json = JsonUtil.buildJson4Map(params);
        String sign = SM2Utils.getSign(json, SysParamUtil.getParam("cust0001_sm2"), qrChannelInf.getSecret_key(), new String[0]);
        String signContext = SM2Utils.sign(sign, json);
        String encryptContext = SM2Utils.encrypt(signContext, SysParamUtil.getParam("cust0001_cer"), new String[0]);
        String reqJson = JsonUtil.buildJson4Map(this.buildCommonParam(encryptContext, params));
        String resultJson = HttpUtility.postData(Environment.MINSHENG_PAY_URL, reqJson);
        System.out.println("json\u4e32\uff1a" + json);
        System.out.println("\u7b7e\u540d\u4e32\uff1a" + sign);
        System.out.println("\u52a0\u5bc6\u524d\u660e\u6587" + signContext);
        System.out.println("\u5bc6\u6587" + encryptContext);
        System.out.println("\u53d1\u9001\u62a5\u6587\uff1a" + reqJson);
        System.out.println("\u8fd4\u56de\u62a5\u6587\uff1a" + resultJson);
        Map resultMap = (Map) JsonUtil.parseJson(resultJson);
        if ("S".equals(resultMap.get("gateReturnType"))) {
            String businessContext = StringUtil.trans2Str(resultMap.get("businessContext"));
            String dncryptContext = SM2Utils.dncrypt(businessContext, SysParamUtil.getParam("cust0001_sm2"), new String[0]);
            System.out.println("\u54cd\u5e94\u660e\u6587:" + dncryptContext);
            Map<String, Object> resultDataMap = JsonUtil.gsonParseJson(dncryptContext);
            String body = StringUtil.trans2Str(resultDataMap.get("body"));
            String backSign = StringUtil.trans2Str(resultDataMap.get("sign"));
            boolean backSignCheck = SM2Utils.signCheck(body, backSign, SysParamUtil.getParam("cust0001_cer"), new String[0]);
            System.out.println("\u9a8c\u8bc1\u7b7e\u540d\u7ed3\u679c\uff1a" + backSignCheck);
            if (backSignCheck) {
                Map<String, Object> resultData = JsonUtil.gsonParseJson(body);
                if ("R".equals(resultData.get("tradeStatus"))) {
                    response.setResultCode(ResponseEnum.SUCCESS.getCode());
                    response.setCode_url(SM2Utils.decodeBase64(StringUtil.trans2Str(resultData.get("payInfo")), new String[0]));
                    wxpay.setTransaction_id("");
                    wxpay.setCode_url(response.getCode_url());
                    wxpay.setTrade_state(TradeStateEnum.NOTPAY.getCode());
                    wxpay.setResp_code("00");
                    wxpay.setMessage("\u64cd\u4f5c\u6210\u529f");
                } else {
                    wxpay.setMessage("\u8ba2\u5355\u5931\u8d25");
                }
                wxpay.setResult_code(StringUtil.trans2Str(resultData.get("tradeStatus")));
            } else {
                wxpay.setResult_code(ResponseEnum.BACK_SIGN_ERROR.getCode());
                wxpay.setMessage(ResponseEnum.BACK_SIGN_ERROR.getMemo());
                response.setResultCode(ResponseEnum.BACK_SIGN_ERROR.getCode());
                response.setMessage(ResponseEnum.BACK_SIGN_ERROR.getMemo());
            }
        } else {
            response.setResultCode(StringUtil.trans2Str(resultMap.get("gateReturnType")));
            response.setMessage(StringUtil.trans2Str(resultMap.get("gateReturnMessage")));
            wxpay.setResult_code(StringUtil.trans2Str(resultMap.get("gateReturnType")));
            wxpay.setMessage(StringUtil.trans2Str(resultMap.get("gateReturnMessage")));
        }
        this.wxNativeDao.save(wxpay);
        return response;
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
        String tradeType = TradeTypesEnum.H5_WXJSAPI.getCode();
        String timeStart = DateUtil.getCurrTime();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("platformId", this.platformId);
        params.put("merchantNo", qrChannelInf.getChannel_mcht_no());
        params.put("defaultTradeType", tradeType);
        params.put("merchantName", reqParam.getGoodsName());
        params.put("selectTradeType", tradeType);
        params.put("amount", String.valueOf(reqParam.getOrderAmount()));
        params.put("orderInfo", "\u8ba2\u5355\u4fe1\u606f");
        params.put("merchantSeq", tradeNo);
        params.put("transDate", DateUtil.getCurrentDay());
        params.put("transTime", timeStart);
        params.put("notifyUrl", SysParamUtil.getParam("MINSHENG_NOTIFY_URL"));
        params.put("redirectUrl", "www.baidu.com");
        params.put("remark", "\u5907\u6ce8");
        wxpay.setService(tradeType);
        wxpay.setMch_id(qrChannelInf.getChannel_mcht_no());
        wxpay.setMch_create_ip(reqParam.getRemoteAddr());
        wxpay.setChannel_id(qrChannelInf.getChannel_id());
        wxpay.setTrade_type(tradeType);
        wxpay.setTime_start(timeStart);
        wxpay.setOut_trade_no(tradeNo);
        wxpay.setTradeSn(reqParam.getTradeSn());
        wxpay.setMch_create_ip(reqParam.getRemoteAddr());
        wxpay.setTotal_fee(reqParam.getOrderAmount());
        wxpay.setBody(reqParam.getGoodsName());
        wxpay.setNotify_url(SysParamUtil.getParam("MINSHENG_NOTIFY_URL"));
        wxpay.setT0Flag(reqParam.getT0Flag());
        wxpay.setGymchtId(reqParam.getGymchtId());
        wxpay.setGy_notifyUrl(reqParam.getNotifyUrl());
        String json = JsonUtil.buildJson4Map(params);
        String sign = SM2Utils.getSign(json, SysParamUtil.getParam("cust0001_sm2"), qrChannelInf.getSecret_key(), new String[0]);
        String signContext = SM2Utils.sign(sign, json);
        String encryptContext = SM2Utils.encrypt(signContext, SysParamUtil.getParam("cust0001_cer"), new String[0]);
        response.setResultCode(ResponseEnum.SUCCESS.getCode());
        response.setMessage(ResponseEnum.SUCCESS.getMemo());
        System.out.println(encryptContext);
        response.setPay_info(String.valueOf(Environment.MINSHENG_JSPAY_URL.getBaseUrl()) + "?context=" + encryptContext);
        System.out.println("json\u4e32\uff1a" + json);
        System.out.println("\u7b7e\u540d\u4e32\uff1a" + sign);
        System.out.println("\u52a0\u5bc6\u524d\u660e\u6587" + signContext);
        System.out.println("\u5bc6\u6587" + encryptContext);
        System.out.println("\u53d1\u9001\u62a5\u6587\uff1a" + response.getPay_info());
        wxpay.setTrade_state(TradeStateEnum.NOTPAY.getCode());
        wxpay.setResp_code("00");
        wxpay.setMessage("\u64cd\u4f5c\u6210\u529f");
        this.wxNativeDao.save(wxpay);
        return response;
    }

    private Map<String, String> buildCommonParam(String businessContext, Map<String, String> params) {
        HashMap<String, String> commonParams = new HashMap<String, String>();
        commonParams.put("version", "");
        commonParams.put("source", "PC");
        commonParams.put("merchantNum", params.get("merchantNo"));
        commonParams.put("merchantSeq", params.get("merchantSeq"));
        commonParams.put("transDate", params.get("transDate"));
        commonParams.put("transTime", params.get("transTime"));
        commonParams.put("transCode", params.get("selectTradeType"));
        commonParams.put("securityType", "");
        commonParams.put("businessContext", businessContext);
        return commonParams;
    }

    @Override
    public WxpayScanCode queryByTradesn(String tradeSn, String gyMchtId, QrcodeChannelInf qrChannelInf) {
        WxpayScanCode wxpayScanCode = this.wxNativeDao.getByTradesn(tradeSn, gyMchtId);
        if (wxpayScanCode != null && StringUtil.isNotEmpty(wxpayScanCode.getCode_url()) && !TradeStateEnum.SUCCESS.getCode().equals(wxpayScanCode.getTrade_state())) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("platformId", this.platformId);
            params.put("merchantNo", qrChannelInf.getChannel_mcht_no());
            params.put("merchantSeq", wxpayScanCode.getOut_trade_no());
            params.put("tradeType", "1");
            String json = JsonUtil.buildJson4Map(params);
            String sign = SM2Utils.getSign(json, SysParamUtil.getParam("cust0001_sm2"), qrChannelInf.getSecret_key(), new String[0]);
            String signContext = SM2Utils.sign(sign, json);
            String encryptContext = SM2Utils.encrypt(signContext, SysParamUtil.getParam("cust0001_cer"), new String[0]);
            String reqJson = JsonUtil.buildJson4Map(this.buildCommonParam(encryptContext, params));
            String resultJson = HttpUtility.postData(Environment.MINSHENG_QUERY_PAY_URL, reqJson);
            System.out.println("json\u4e32\uff1a" + json);
            System.out.println("\u7b7e\u540d\u4e32\uff1a" + sign);
            System.out.println("\u52a0\u5bc6\u524d\u660e\u6587" + signContext);
            System.out.println("\u5bc6\u6587" + encryptContext);
            System.out.println("\u53d1\u9001\u62a5\u6587\uff1a" + reqJson);
            System.out.println("\u8fd4\u56de\u62a5\u6587\uff1a" + resultJson);
            Map<String, Object> resultMap = JsonUtil.gsonParseJson(resultJson);
            if ("S".equals(resultMap.get("gateReturnType"))) {
                String businessContext = StringUtil.trans2Str(resultMap.get("businessContext"));
                String dncryptContext = SM2Utils.dncrypt(businessContext, SysParamUtil.getParam("cust0001_sm2"), new String[0]);
                System.out.println("\u54cd\u5e94\u660e\u6587:" + dncryptContext);
                Map<String, Object> resultDataMap = JsonUtil.gsonParseJson(dncryptContext);
                String body = StringUtil.trans2Str(resultDataMap.get("body"));
                String backSign = StringUtil.trans2Str(resultDataMap.get("sign"));
                boolean backSignCheck = SM2Utils.signCheck(body, backSign, SysParamUtil.getParam("cust0001_cer"), new String[0]);
                if (backSignCheck) {
                    Map<String, Object> resultData = JsonUtil.gsonParseJson(body);
                    if ("S".equals(resultData.get("tradeStatus"))) {
                        String centerInfo = StringUtil.trans2Str(resultData.get("centerInfo"));
                        Map<String, String> centerInfoMap = CommonFunction.parseList2Map(centerInfo, "|");
                        wxpayScanCode.setPay_result("0");
                        wxpayScanCode.setTime_end(centerInfoMap.get("time_end"));
                        wxpayScanCode.setTransaction_id(StringUtil.trans2Str(resultData.get("bankTradeNo")));
                        wxpayScanCode.setOut_transaction_id(StringUtil.trans2Str(resultData.get("centerSeqId")));
                        wxpayScanCode.setTrade_state(TradeStateEnum.SUCCESS.getCode());
                        this.wxNativeDao.update(wxpayScanCode);
                    } else if ("E".equals(resultData.get("tradeStatus"))) {
                        wxpayScanCode.setTrade_state(TradeStateEnum.PAYERROR.getCode());
                        this.wxNativeDao.update(wxpayScanCode);
                    } else {
                        "T".equals(resultData.get("tradeStatus"));
                    }
                }
            }
        }
        return wxpayScanCode;
    }

    @Override
    public String saveResultNotify(Map<String, String> resultMap) {
        WxpayScanCode notifyBeanTemp = this.wxNativeDao.getById(resultMap.get("orderNo"));
        if (notifyBeanTemp == null || !String.valueOf(notifyBeanTemp.getTotal_fee()).equals(resultMap.get("amount"))) {
            return "notexist";
        }
        String notifyBack = "";
        if (!TradeStateEnum.SUCCESS.getCode().equals(notifyBeanTemp.getTrade_state())) {
            String centerInfo = StringUtil.trans2Str(resultMap.get("centerInfo"));
            Map<String, String> centerInfoMap = CommonFunction.parseList2Map(centerInfo, "|");
            if ("S".equals(resultMap.get("tradeStatus"))) {
                notifyBeanTemp.setPay_result("0");
                notifyBeanTemp.setTransaction_id(resultMap.get("bankTradeNo"));
                notifyBeanTemp.setTrade_state(TradeStateEnum.SUCCESS.getCode());
                this.tradeMchtAccountService.notifySuccess(notifyBeanTemp);
            } else {
                notifyBeanTemp.setTrade_state(TradeStateEnum.PAYERROR.getCode());
            }
            notifyBeanTemp.setTime_end(centerInfoMap.get("time_end"));
        }
        if (notifyBeanTemp.getGy_notifyUrl() != null && !"success".equals(notifyBeanTemp.getGynotify_back())) {
            Environment evn = Environment.createEnvironment(notifyBeanTemp.getGy_notifyUrl(), "utf-8", "application/json");
            QrcodeMchtInfo qrcodeMcht = this.qrcodeMchtInfoDaoImpl.getMchtInfo(notifyBeanTemp.getGymchtId());
            ResponseQuery backNotify = this.buildResponseQuery(notifyBeanTemp);
            if ("S".equals(resultMap.get("tradeStatus"))) {
                backNotify.setPay_result("0");
                backNotify.setTimeEnd(notifyBeanTemp.getTime_end());
            } else {
                backNotify.setPay_result(resultMap.get("tradeStatus"));
                backNotify.setPay_info("\u4ea4\u6613\u5931\u8d25");
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

    private static enum TradeTypesEnum {
        H5_WXJSAPI("H5_WXJSAPI", "\u5fae\u4fe1\u516c\u4f17\u53f7\u8df3\u8f6c\u652f\u4ed8"),
        API_WXQRCODE("API_WXQRCODE", "\u5fae\u4fe1\u6b63\u626b"),
        API_WXSCAN("API_WXSCAN", "\u5fae\u4fe1\u53cd\u626b"),
        API_ZFBQRCODE("API_ZFBQRCODE", "\u652f\u4ed8\u5b9d\u6b63\u626b"),
        API_ZFBSCAN("API_ZFBSCAN", "\u652f\u4ed8\u5b9d\u53cd\u626b"),
        H5_ZFBJSAPI("H5_ZFBJSAPI", "\u652f\u4ed8\u5b9d\u670d\u52a1\u7a97");

        private String code;
        private String memo;

        private TradeTypesEnum(String code, String memo) {
            this.code = code;
            this.memo = (String) memo;
        }

        public String getCode() {
            return this.code;
        }

        public String getMemo() {
            return this.memo;
        }
    }
}
