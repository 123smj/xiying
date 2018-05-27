/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.log4j.Logger
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.trade.service.impl;

import com.gy.cache.WezbankCache;
import com.gy.system.Environment;
import com.gy.system.SysParamUtil;
import com.gy.util.DateUtil;
import com.gy.util.HttpUtility;
import com.gy.util.HttpsUtility;
import com.gy.util.StringUtil;
import com.gy.util.UUIDGenerator;
import com.gy.util.WezbankHttpsUtility;
import com.trade.bean.WezBankClient;
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
import com.trade.service.impl.CommonService;
import com.trade.util.GuangdaUtil;
import com.trade.util.JsonUtil;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WezbankServiceImpl
        extends CommonService
        implements TradeService {
    @Autowired
    private WxNativeDao wxNativeDao;
    @Autowired
    private QrcodeMchtInfoDao qrcodeMchtInfoDaoImpl;
    @Autowired
    private WezbankCache wezbankCache;
    public static final String version = "1.0.0";
    public static final String ALIPAY_APP_ID = "W3438051";
    public static final String ALIPAY_SECRET = "GXXn1SItFP7iJM61eR11DN7vxYSTHxZljsdRZrcmRdfEa0JO6vVK8X8OndZR7suh";
    private static Logger log = Logger.getLogger(WezbankServiceImpl.class);

    @Override
    public ResponseCode doTrade(TradeParam reqParam, QrcodeChannelInf qrChannelInf) {
        WxpayScanCode wxpay = new WxpayScanCode();
        String tradeNo = UUIDGenerator.getOrderIdByUUId(20);
        ResponseCode response = new ResponseCode();
        response.setGymchtId(reqParam.getGymchtId());
        if (this.wxNativeDao.getByTradesn(reqParam.getTradeSn(), reqParam.getGymchtId()) != null) {
            response.setResultCode(ResponseEnum.FAIL_ORDER_NO_REPEAT.getCode());
            response.setMessage(ResponseEnum.FAIL_ORDER_NO_REPEAT.getMemo());
            return response;
        }
        String currTime = DateUtil.getCurrTime();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("wbMerchantId", qrChannelInf.getChannel_mcht_no());
        params.put("orderId", tradeNo);
        params.put("totalAmount", StringUtil.changeF2Y(reqParam.getOrderAmount()));
        params.put("subject", reqParam.getGoodsName());
        params.put("body", reqParam.getGoodsName());
        if (StringUtil.isNotEmpty(reqParam.getExpirySecond())) {
            params.put("timeoutExpress", String.valueOf(Integer.valueOf(reqParam.getExpirySecond()) / 60) + "m");
        }
        wxpay.setService("wezBank_alipay");
        wxpay.setMch_id(qrChannelInf.getChannel_mcht_no());
        wxpay.setMch_create_ip(reqParam.getRemoteAddr());
        wxpay.setChannel_id(qrChannelInf.getChannel_id());
        wxpay.setTrade_type("wezBank_alipay");
        wxpay.setTime_start(currTime);
        wxpay.setOut_trade_no(tradeNo);
        wxpay.setTradeSn(reqParam.getTradeSn());
        wxpay.setTotal_fee(reqParam.getOrderAmount());
        wxpay.setBody(reqParam.getGoodsName());
        wxpay.setNotify_url(SysParamUtil.getParam("WEBANK_NOTIFY_URL"));
        wxpay.setT0Flag(reqParam.getT0Flag());
        wxpay.setGymchtId(reqParam.getGymchtId());
        wxpay.setGy_notifyUrl(reqParam.getNotifyUrl());
        return this.connectChannel(wxpay, params);
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
        String payType = "F60002";
        LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
        params.put("txcode", payType);
        params.put("txdate", currTime.substring(0, 8));
        params.put("txtime", currTime.substring(8));
        params.put("version", "2.0.0");
        if (TradeSourceEnum.ALIPAY.getCode().equals(wxpayScanCode.getTrade_source())) {
            params.put("field003", "900022");
        } else {
            params.put("field003", "900021");
        }
        params.put("field004", StringUtil.fillLeftValue(String.valueOf(wxpayScanCode.getTotal_fee()), 12, '0'));
        params.put("field011", wxpayScanCode.getOut_trade_no().substring(wxpayScanCode.getOut_trade_no().length() - 6));
        params.put("field031", "26001");
        params.put("field041", qrChannelInf.getAgtId());
        params.put("field042", qrChannelInf.getChannel_mcht_no());
        params.put("field048", wxpayScanCode.getOut_trade_no());
        params.put("field060", SysParamUtil.getParam("WLJR_NOTIFY_URL"));
        params.put("field125", "111111");
        params.put("field128", GuangdaUtil.md5SignWithValue(params, qrChannelInf.getSecret_key(), "UTF-8").substring(0, 16));
        wxpayScanCode.setTrade_type(payType);
        wxpayScanCode.setService(payType);
        wxpayScanCode.setNotify_url(SysParamUtil.getParam("WLJR_NOTIFY_URL"));
        return this.connectChannel(wxpayScanCode, params);
    }

    private ResponseCode connectChannel(WxpayScanCode wxpayScanCode, Map<String, String> params) {
        ResponseCode response = new ResponseCode();
        String json = JsonUtil.buildJson4Map(params);
        String jsonResult = this.connectWezbankAlipay(json, Environment.WEZBANK_ALIPAY_URL);
        if (jsonResult == null) {
            response.setResultCode(ResponseEnum.BACK_EXCEPTION.getCode());
            response.setMessage(ResponseEnum.BACK_EXCEPTION.getMemo());
            return response;
        }
        Map returnMap = (Map) JsonUtil.parseJson(jsonResult);
        String resultCode = StringUtil.trans2Str(returnMap.get("code"));
        String resultMsg = StringUtil.trans2Str(returnMap.get("msg"));
        if ("0".equals(resultCode)) {
            response.setResultCode(ResponseEnum.SUCCESS.getCode());
            response.setCode_url(StringUtil.trans2Str(returnMap.get("qrCode")));
            wxpayScanCode.setTransaction_id(StringUtil.trans2Str(returnMap.get("outTradeNo")));
            wxpayScanCode.setCode_url(response.getCode_url());
            wxpayScanCode.setTrade_state(TradeStateEnum.NOTPAY.getCode());
            wxpayScanCode.setResp_code("00");
        } else {
            response.setResultCode(resultCode);
            response.setMessage(resultMsg);
        }
        wxpayScanCode.setResult_code(resultCode);
        wxpayScanCode.setMessage(resultMsg);
        this.wxNativeDao.saveOrUpdate(wxpayScanCode);
        return response;
    }

    @Override
    public WxpayScanCode queryByTradesn(String tradeSn, String gyMchtId, QrcodeChannelInf qrChannelInf) {
        WxpayScanCode wxpayScanCode = this.wxNativeDao.getByTradesn(tradeSn, gyMchtId);
        return this.queryFromChannel(wxpayScanCode, qrChannelInf);
    }

    @Override
    public WxpayScanCode queryFromChannel(WxpayScanCode wxpayScanCode, QrcodeChannelInf qrChannelInf) {
        if (wxpayScanCode != null && StringUtil.isNotEmpty(wxpayScanCode.getCode_url()) && !TradeStateEnum.SUCCESS.getCode().equals(wxpayScanCode.getTrade_state())) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("wbMerchantId", qrChannelInf.getChannel_mcht_no());
            params.put("orderId", wxpayScanCode.getOut_trade_no());
            Environment env = Environment.WEZBANK_ALIQUERY_URL;
            String jsonParam = JsonUtil.buildJson4Map(params);
            String jsonResult = this.connectWezbankAlipay(jsonParam, env);
            Map returnMap = (Map) JsonUtil.parseJson(jsonResult);
            String resultCode = StringUtil.trans2Str(returnMap.get("code"));
            String resultMsg = StringUtil.trans2Str(returnMap.get("msg"));
            if (returnMap != null && "0".equals(resultCode)) {
                String tradeStatus = (String) returnMap.get("tradeStatus");
                if ("01".equals(tradeStatus)) {
                    wxpayScanCode.setPay_result("0");
                    if (!StringUtil.isEmpty((String) returnMap.get("sendPayDate"))) {
                        wxpayScanCode.setTime_end((String) returnMap.get("sendPayDate"));
                    } else {
                        wxpayScanCode.setTime_end(DateUtil.getCurrTime());
                    }
                    wxpayScanCode.setOp_user_id((String) returnMap.get("buyerLogonId"));
                    wxpayScanCode.setTransaction_id((String) returnMap.get("outTradeNo"));
                    wxpayScanCode.setOut_transaction_id((String) returnMap.get("tradeNo"));
                    wxpayScanCode.setTrade_state(TradeStateEnum.SUCCESS.getCode());
                } else if ("03".equals(tradeStatus) || "00".equals(tradeStatus)) {
                    wxpayScanCode.setTrade_state(TradeStateEnum.NOTPAY.getCode());
                } else if ("02".equals(tradeStatus)) {
                    wxpayScanCode.setTrade_state(TradeStateEnum.PAYERROR.getCode());
                } else if ("04".equals(tradeStatus)) {
                    wxpayScanCode.setTrade_state(TradeStateEnum.CLOSED.getCode());
                }
                this.wxNativeDao.update(wxpayScanCode);
            }
        }
        return wxpayScanCode;
    }

    @Override
    public String saveResultNotify(Map<String, String> resultMap) {
        if (resultMap == null || StringUtil.isEmpty(resultMap.get("orderId"))) {
            return "orderId_error";
        }
        WxpayScanCode notifyBeanTemp = this.wxNativeDao.getById(resultMap.get("orderId"));
        if (notifyBeanTemp == null || !StringUtil.changeF2Y(notifyBeanTemp.getTotal_fee()).equals(resultMap.get("totalAmount"))) {
            return "notexist";
        }
        String notifyBack = "";
        String tradeStatus = resultMap.get("tradeStatus");
        if (!TradeStateEnum.SUCCESS.getCode().equals(notifyBeanTemp.getTrade_state())) {
            if ("01".equals(tradeStatus)) {
                notifyBeanTemp.setTrade_state(TradeStateEnum.SUCCESS.getCode());
            } else if ("02".equals(tradeStatus)) {
                notifyBeanTemp.setTrade_state(TradeStateEnum.PAYERROR.getCode());
            } else if ("04".equals(tradeStatus)) {
                notifyBeanTemp.setTrade_state(TradeStateEnum.CLOSED.getCode());
            }
            notifyBeanTemp.setTime_end(DateUtil.getCurrTime());
        }
        if (notifyBeanTemp.getGy_notifyUrl() != null && !"success".equals(notifyBeanTemp.getGynotify_back())) {
            Environment evn = Environment.createEnvironment(notifyBeanTemp.getGy_notifyUrl(), "utf-8", "application/json");
            QrcodeMchtInfo qrcodeMcht = this.qrcodeMchtInfoDaoImpl.getMchtInfo(notifyBeanTemp.getGymchtId());
            ResponseQuery backNotify = this.buildResponseQuery(notifyBeanTemp);
            if ("01".equals(tradeStatus)) {
                backNotify.setPay_result("0");
                backNotify.setTimeEnd(notifyBeanTemp.getTime_end());
            } else {
                backNotify.setPay_result("1");
                backNotify.setPay_info(tradeStatus);
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
        return "success";
    }

    @Override
    public ResponseCode getWxjspay(TradeParam reqParam, QrcodeChannelInf qrChannelInf) {
        return this.regAlimcht();
    }

    public ResponseCode regAlimcht() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("productType", "003");
        params.put("registerType", "01");
        HashMap<String, String> merchantInfo = new HashMap<String, String>();
        merchantInfo.put("agencyId", "1070755003");
        merchantInfo.put("appId", "W3438051");
        merchantInfo.put("idType", "01");
        merchantInfo.put("idNo", "362324199107253051");
        merchantInfo.put("merchantName", "\u56fd\u94f6\u652f\u4ed8\u79d1\u6280\u6709\u9650\u516c\u53f8");
        merchantInfo.put("legalRepresent", "\u5de6\u677e");
        merchantInfo.put("licenceNo", "1002842");
        merchantInfo.put("licenceBeginTime", "2012-12-12");
        merchantInfo.put("licenceEndTime", "2020-12-12");
        merchantInfo.put("taxRegisterNo", "123456");
        merchantInfo.put("positionCode", "0");
        merchantInfo.put("contactName", "\u53f6\u5efa\u6587");
        merchantInfo.put("contactPhoneNo", "15217928112");
        merchantInfo.put("mainBusiness", "\u7ebf\u4e0a\u5546\u57ce");
        merchantInfo.put("businessRange", "\u7ebf\u4e0a\u5546\u57ce");
        merchantInfo.put("registerAddr", "\u6df1\u5733\u5e02\u5b9d\u5b89\u533a");
        merchantInfo.put("merchantTypeCode", "5812");
        merchantInfo.put("merchantLevel", "1");
        merchantInfo.put("merchantNature", "\u79c1\u8425\u4f01\u4e1a");
        merchantInfo.put("contractNo", "1703310022");
        merchantInfo.put("openYear", "2012-12-12");
        merchantInfo.put("categoryId", "2015050700000015");
        params.put("merchantInfo", merchantInfo);
        HashMap<String, String> merchantAccount = new HashMap<String, String>();
        merchantAccount.put("accountNo", "6225830202948815");
        merchantAccount.put("accountOpbankNo", "308584000013");
        merchantAccount.put("accountName", "\u53f6\u5efa\u6587");
        merchantAccount.put("accountOpbank", "\u62db\u5546\u94f6\u884c");
        merchantAccount.put("accountSubbranchOpbank", "\u5e7f\u5dde\u5929\u5e9c\u8def\u652f\u884c");
        merchantAccount.put("accountOpbankAddr", "\u5e7f\u5dde\u5e02\u5929\u5e9c\u8def");
        merchantAccount.put("acctType", "02");
        merchantAccount.put("settlementCycle", "1");
        params.put("merchantAccount", merchantAccount);
        ArrayList merchantRateList = new ArrayList();
        HashMap<String, String> merchantRate = new HashMap<String, String>();
        merchantRate.put("paymentType", "20");
        merchantRate.put("settlementType", "01");
        merchantRate.put("chargeType", "02");
        merchantRate.put("commissionRate", "6");
        merchantRate.put("commissionAmount", "2");
        merchantRateList.add(merchantRate);
        params.put("merchantRateList", merchantRateList);
        params.put("aliasName", "\u56fd\u94f6\u6d4b\u8bd5");
        params.put("servicePhone", "5327642");
        params.put("contactPhone", "5327642");
        params.put("contactEmail", "757174266@qq.com");
        params.put("memo", "\u5907\u6ce8");
        params.put("externalInfo", "\u6269\u5c55");
        params.put("district", "0755");
        String json = JsonUtil.buildJson4Map(params);
        String jsonResult = this.connectWezbankAlipay(json, Environment.WEZBANK_ALI_REGMCHT_URL);
        ResponseCode response = new ResponseCode();
        if (jsonResult == null) {
            response.setResultCode(ResponseEnum.BACK_EXCEPTION.getCode());
            response.setMessage(ResponseEnum.BACK_EXCEPTION.getMemo());
            return response;
        }
        Map returnMap = (Map) JsonUtil.parseJson(jsonResult);
        String resultCode = StringUtil.trans2Str(returnMap.get("code"));
        String resultMsg = StringUtil.trans2Str(returnMap.get("msg"));
        if ("0".equals(resultCode)) {
            response.setResultCode(ResponseEnum.SUCCESS.getCode());
            response.setCode_url(StringUtil.trans2Str(returnMap.get("merchantId")));
        } else {
            response.setResultCode(resultCode);
            response.setMessage(resultMsg);
        }
        return response;
    }

    public String connectWezbankAlipay(String jsonParam, Environment env) {
        String clientJks = String.valueOf(HttpsUtility.absolutePath) + "cert/wezbank/webank_wepay_keystore.jks";
        String serverJks = String.valueOf(HttpsUtility.absolutePath) + "cert/wezbank/webank_wepay_truststore.jks";
        String nonce = StringUtil.getRandom(32);
        WezBankClient wezBankClient = this.wezbankCache.getWezBankClient("W3438051", "GXXn1SItFP7iJM61eR11DN7vxYSTHxZljsdRZrcmRdfEa0JO6vVK8X8OndZR7suh");
        ArrayList<String> signDatas = new ArrayList<String>();
        signDatas.add("W3438051");
        signDatas.add("1.0.0");
        signDatas.add(nonce);
        signDatas.add(jsonParam);
        String sign = GuangdaUtil.wezbankSign(signDatas, wezBankClient.getTicket());
        String jsonResult = WezbankHttpsUtility.httpPost(env, jsonParam, clientJks, "P5810007", serverJks);
        return jsonResult;
    }

    public String connectWezbankWepay(String jsonParam, Environment env) {
        String clientJks = String.valueOf(HttpsUtility.absolutePath) + "cert/wezbank/webank_keystore.jks";
        String serverJks = String.valueOf(HttpsUtility.absolutePath) + "cert/wezbank/webank_truststore.jks";
        String nonce = StringUtil.getRandom(32);
        WezBankClient wezBankClient = this.wezbankCache.getWezBankClient("W3438051", "GXXn1SItFP7iJM61eR11DN7vxYSTHxZljsdRZrcmRdfEa0JO6vVK8X8OndZR7suh");
        ArrayList<String> signDatas = new ArrayList<String>();
        signDatas.add("W3438051");
        signDatas.add("1.0.0");
        signDatas.add(nonce);
        signDatas.add(jsonParam);
        String sign = GuangdaUtil.wezbankSign(signDatas, wezBankClient.getTicket());
        String baseUrl = String.valueOf(env.getBaseUrl()) + "?app_id=" + "W3438051" + "&nonce=" + nonce + "&version=" + "1.0.0" + "&sign=" + sign;
        Environment envReal = Environment.createEnvironment(baseUrl, env.getCharset(), env.getContentType());
        String jsonResult = WezbankHttpsUtility.httpPost(envReal, jsonParam, clientJks, "www.guoyinpay.com", serverJks);
        return jsonResult;
    }

    public static void main(String[] args) {
        String result = "{'field003':'900021','field041':'00000976','field042':'100006700000103','field055':'2017022100000537696975|00|20170221163857|0001|weixin|','field062':'2017021800074860','field128':'91A3AA86423B17C4','txcode':'F60006','txdate':'20170221','txtime':'163842','version':'1.0.0'}";
        Map returnMap = (Map) JsonUtil.parseJson(result);
        String field055 = (String) returnMap.get("field055");
        String[] notifyData = field055.split("\\|");
        System.out.println(Integer.valueOf(notifyData[3]));
        System.out.println("1".equals(Integer.valueOf(notifyData[3]).toString()));
    }
}
