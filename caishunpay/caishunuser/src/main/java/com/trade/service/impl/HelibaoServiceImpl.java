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
import com.trade.service.impl.CommonService;
import com.trade.util.GuangdaUtil;
import com.trade.util.JsonUtil;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HelibaoServiceImpl
        extends CommonService
        implements TradeService {
    @Autowired
    private WxNativeDao wxNativeDao;
    @Autowired
    private QrcodeMchtInfoDao qrcodeMchtInfoDaoImpl;
    private static Logger log = Logger.getLogger(HelibaoServiceImpl.class);

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
        String payType = "SCAN";
        LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
        params.put("P1_bizType", "AppPay");
        params.put("P2_orderId", tradeNo);
        params.put("P3_customerNumber", qrChannelInf.getChannel_mcht_no());
        params.put("P4_payType", payType);
        params.put("P5_orderAmount", StringUtil.changeF2Y(reqParam.getOrderAmount()));
        params.put("P6_currency", "CNY");
        params.put("P7_authcode", "1");
        if (TradeSourceEnum.ALIPAY.getCode().equals(reqParam.getTradeSource())) {
            params.put("P8_appType", "ALIPAY");
            wxpay.setTrade_source(TradeSourceEnum.ALIPAY.getCode());
        } else {
            params.put("P8_appType", "WXPAY");
            wxpay.setTrade_source(TradeSourceEnum.WEPAY.getCode());
        }
        params.put("P9_notifyUrl", SysParamUtil.getParam("HELIBAO_NOTIFY_URL"));
        params.put("P10_successToUrl", "");
        params.put("P11_orderIp", reqParam.getRemoteAddr());
        params.put("P12_goodsName", reqParam.getGoodsName());
        params.put("P13_goodsDetail", reqParam.getGoodsName());
        params.put("P14_desc", reqParam.getGoodsName());
        params.put("sign", GuangdaUtil.getHelibaoMd5Sign(params, qrChannelInf.getSecret_key(), "UTF-8"));
        try {
            params.put("P12_goodsName", URLEncoder.encode(reqParam.getGoodsName(), "UTF-8"));
            params.put("P13_goodsDetail", URLEncoder.encode(reqParam.getGoodsName(), "UTF-8"));
            params.put("P14_desc", URLEncoder.encode(reqParam.getGoodsName(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        wxpay.setService(payType);
        wxpay.setMch_id(qrChannelInf.getChannel_mcht_no());
        wxpay.setMch_create_ip(reqParam.getRemoteAddr());
        wxpay.setChannel_id(qrChannelInf.getChannel_id());
        wxpay.setTrade_type(payType);
        wxpay.setTime_start(currTime);
        wxpay.setOut_trade_no(tradeNo);
        wxpay.setTradeSn(reqParam.getTradeSn());
        wxpay.setTotal_fee(reqParam.getOrderAmount());
        wxpay.setBody(reqParam.getGoodsName());
        wxpay.setNotify_url(SysParamUtil.getParam("HELIBAO_NOTIFY_URL"));
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
        String payType = "SCAN";
        LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
        params.put("P1_bizType", "AppPay");
        params.put("P2_orderId", wxpayScanCode.getOut_trade_no());
        params.put("P3_customerNumber", qrChannelInf.getChannel_mcht_no());
        params.put("P4_payType", payType);
        params.put("P5_orderAmount", StringUtil.changeF2Y(wxpayScanCode.getTotal_fee()));
        params.put("P6_currency", "CNY");
        params.put("P7_authcode", "1");
        params.put("P9_notifyUrl", SysParamUtil.getParam("HELIBAO_NOTIFY_URL"));
        params.put("P11_orderIp", wxpayScanCode.getMch_create_ip());
        try {
            params.put("P12_goodsName", URLEncoder.encode(wxpayScanCode.getBody(), "UTF-8"));
            params.put("P13_goodsDetail", URLEncoder.encode(wxpayScanCode.getBody(), "UTF-8"));
            params.put("P14_desc", URLEncoder.encode(wxpayScanCode.getBody(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (TradeSourceEnum.ALIPAY.getCode().equals(wxpayScanCode.getTrade_source())) {
            params.put("P8_appType", "ALIPAY");
        } else {
            params.put("P8_appType", "WXPAY");
        }
        params.put("sign", GuangdaUtil.getHelibaoMd5Sign(params, qrChannelInf.getSecret_key(), "UTF-8"));
        wxpayScanCode.setTrade_type(payType);
        wxpayScanCode.setService(payType);
        wxpayScanCode.setNotify_url(SysParamUtil.getParam("HELIBAO_NOTIFY_URL"));
        return this.connectChannel(wxpayScanCode, params);
    }

    private ResponseCode connectChannel(WxpayScanCode wxpayScanCode, Map<String, String> params) {
        ResponseCode response = new ResponseCode();
        Environment env = Environment.HELIBAO_URL;
        String keyValue = GuangdaUtil.map2HttpParam(params);
        String jsonResult = HttpUtility.postData(env, keyValue);
        if (jsonResult == null) {
            response.setResultCode(ResponseEnum.BACK_EXCEPTION.getCode());
            response.setMessage(ResponseEnum.BACK_EXCEPTION.getMemo());
            return response;
        }
        Map returnMap = (Map) JsonUtil.parseJson(jsonResult);
        String resultCode = StringUtil.trans2Str(returnMap.get("rt2_retCode"));
        String resultMsg = StringUtil.trans2Str(returnMap.get("rt3_retMsg"));
        if ("0000".equals(resultCode)) {
            response.setResultCode(ResponseEnum.SUCCESS.getCode());
            response.setCode_url(StringUtil.trans2Str(returnMap.get("rt8_qrcode")));
            System.out.println(StringUtil.trans2Str(returnMap.get("rt9_wapurl")));
            wxpayScanCode.setTransaction_id(StringUtil.trans2Str(returnMap.get("rt6_serialNumber")));
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
            LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
            params.put("P1_bizType", "AppPayQuery");
            params.put("P2_orderId", wxpayScanCode.getOut_trade_no());
            params.put("P3_customerNumber", wxpayScanCode.getMch_id());
            params.put("sign", GuangdaUtil.getHelibaoMd5Sign(params, qrChannelInf.getSecret_key(), "UTF-8"));
            Environment env = Environment.HELIBAO_URL;
            String keyValue = GuangdaUtil.map2HttpParam(params);
            String jsonResult = HttpUtility.postData(env, keyValue);
            Map returnMap = (Map) JsonUtil.parseJson(jsonResult);
            String resultCode = StringUtil.trans2Str(returnMap.get("rt2_retCode"));
            String resultMsg = StringUtil.trans2Str(returnMap.get("rt3_retMsg"));
            String backSign = StringUtil.trans2Str(returnMap.get("field128"));
            if (returnMap != null && "0000".equals(resultCode)) {
                if ("SUCCESS".equals(returnMap.get("rt7_orderStatus"))) {
                    wxpayScanCode.setPay_result("0");
                    wxpayScanCode.setTime_end(DateUtil.getCurrTime());
                    wxpayScanCode.setOut_transaction_id((String) returnMap.get("rt6_serialNumber"));
                    wxpayScanCode.setTrade_state(TradeStateEnum.SUCCESS.getCode());
                } else if ("FAIL".equals(returnMap.get("rt7_orderStatus"))) {
                    wxpayScanCode.setTrade_state(TradeStateEnum.PAYERROR.getCode());
                }
                this.wxNativeDao.update(wxpayScanCode);
            }
        }
        return wxpayScanCode;
    }

    @Override
    public String saveResultNotify(Map<String, String> resultMap) {
        WxpayScanCode notifyBeanTemp = this.wxNativeDao.getById(resultMap.get("rt2_orderId"));
        if (notifyBeanTemp == null || !StringUtil.changeF2Y(notifyBeanTemp.getTotal_fee()).equals(resultMap.get("rt5_orderAmount"))) {
            return "notexist";
        }
        String sign = resultMap.get("sign");
        resultMap.remove("sign");
        QrcodeChannelInf qrcodeChannelInf = this.qrcodeMchtInfoDaoImpl.getChannelInf(notifyBeanTemp.getChannel_id(), notifyBeanTemp.getMch_id());
        String checkSign = GuangdaUtil.md5SignHelibao(GuangdaUtil.sortMapByKey(resultMap), qrcodeChannelInf.getSecret_key(), "UTF-8").toLowerCase();
        if (!checkSign.equals(sign)) {
            return "sign error";
        }
        String notifyBack = "";
        if (!TradeStateEnum.SUCCESS.getCode().equals(notifyBeanTemp.getTrade_state())) {
            if ("SUCCESS".equals(resultMap.get("rt4_status"))) {
                notifyBeanTemp.setTrade_state(TradeStateEnum.SUCCESS.getCode());
                this.tradeMchtAccountService.notifySuccess(notifyBeanTemp);
            } else if ("FAIL".equals(resultMap.get("rt4_status"))) {
                notifyBeanTemp.setTrade_state(TradeStateEnum.PAYERROR.getCode());
            } else if ("CLOSE".equals(resultMap.get("rt4_status"))) {
                notifyBeanTemp.setTrade_state(TradeStateEnum.CLOSED.getCode());
            }
            notifyBeanTemp.setTime_end(DateUtil.getCurrTime());
        }
        if (notifyBeanTemp.getGy_notifyUrl() != null && !"success".equals(notifyBeanTemp.getGynotify_back())) {
            Environment evn = Environment.createEnvironment(notifyBeanTemp.getGy_notifyUrl(), "utf-8", "application/json");
            QrcodeMchtInfo qrcodeMcht = this.qrcodeMchtInfoDaoImpl.getMchtInfo(notifyBeanTemp.getGymchtId());
            ResponseQuery backNotify = this.buildResponseQuery(notifyBeanTemp);
            if ("SUCCESS".equals(resultMap.get("rt4_status"))) {
                backNotify.setPay_result("0");
                backNotify.setTimeEnd(notifyBeanTemp.getTime_end());
            } else {
                backNotify.setPay_result("1");
                backNotify.setPay_info(resultMap.get("rt4_status"));
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
        return null;
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
