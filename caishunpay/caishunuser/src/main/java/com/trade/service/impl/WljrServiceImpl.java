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
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WljrServiceImpl
        extends CommonService
        implements TradeService {
    @Autowired
    private WxNativeDao wxNativeDao;
    @Autowired
    private QrcodeMchtInfoDao qrcodeMchtInfoDaoImpl;
    private static Logger log = Logger.getLogger(WljrServiceImpl.class);

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
        String payType = "F60002";
        LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
        params.put("txcode", payType);
        params.put("txdate", currTime.substring(0, 8));
        params.put("txtime", currTime.substring(8));
        params.put("version", "2.0.0");
        if (TradeSourceEnum.ALIPAY.getCode().equals(reqParam.getTradeSource())) {
            params.put("field003", "900022");
            wxpay.setTrade_source(TradeSourceEnum.ALIPAY.getCode());
        } else {
            params.put("field003", "900021");
            wxpay.setTrade_source(TradeSourceEnum.WEPAY.getCode());
        }
        params.put("field004", StringUtil.fillLeftValue(String.valueOf(reqParam.getOrderAmount()), 12, '0'));
        params.put("field011", tradeNo.substring(tradeNo.length() - 6));
        params.put("field031", "26001");
        params.put("field041", qrChannelInf.getAgtId());
        params.put("field042", qrChannelInf.getChannel_mcht_no());
        params.put("field048", tradeNo);
        params.put("field060", SysParamUtil.getParam("WLJR_NOTIFY_URL"));
        params.put("field125", "111111");
        params.put("field128", GuangdaUtil.md5SignWithValue(params, qrChannelInf.getSecret_key(), "UTF-8").substring(0, 16));
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
        wxpay.setNotify_url(SysParamUtil.getParam("WLJR_NOTIFY_URL"));
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

    private ResponseCode connectChannel(WxpayScanCode wxpayScanCode, LinkedHashMap<String, String> params) {
        ResponseCode response = new ResponseCode();
        Environment env = Environment.WLJR_PAY_URL;
        String json = JsonUtil.buildSortedJson(params);
        String jsonResult = HttpUtility.postData(env, json);
        if (jsonResult == null) {
            response.setResultCode(ResponseEnum.BACK_EXCEPTION.getCode());
            response.setMessage(ResponseEnum.BACK_EXCEPTION.getMemo());
            return response;
        }
        Map returnMap = (Map) JsonUtil.parseJson(jsonResult);
        String resultCode = StringUtil.trans2Str(returnMap.get("field039"));
        String resultMsg = StringUtil.trans2Str(returnMap.get("field124"));
        if ("00".equals(resultCode)) {
            response.setResultCode(ResponseEnum.SUCCESS.getCode());
            response.setCode_url(StringUtil.trans2Str(returnMap.get("field055")));
            wxpayScanCode.setTransaction_id(StringUtil.trans2Str(returnMap.get("field062")));
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
            params.put("txcode", "F60004");
            params.put("txdate", wxpayScanCode.getTime_start().substring(0, 8));
            params.put("txtime", wxpayScanCode.getTime_start().substring(8));
            params.put("version", "2.0.0");
            if (TradeSourceEnum.ALIPAY.getCode().equals(wxpayScanCode.getTrade_source())) {
                params.put("field003", "900022");
            } else {
                params.put("field003", "900021");
            }
            params.put("field004", StringUtil.fillLeftValue(String.valueOf(wxpayScanCode.getTotal_fee()), 12, '0'));
            params.put("field011", wxpayScanCode.getOut_trade_no().substring(wxpayScanCode.getOut_trade_no().length() - 6));
            params.put("field041", qrChannelInf.getAgtId());
            params.put("field042", qrChannelInf.getChannel_mcht_no());
            params.put("field048", wxpayScanCode.getOut_trade_no());
            params.put("field062", wxpayScanCode.getTransaction_id());
            params.put("field125", "111111");
            params.put("field128", GuangdaUtil.md5SignWithValue(params, qrChannelInf.getSecret_key(), "UTF-8").substring(0, 16));
            Environment env = Environment.WLJR_PAY_URL;
            String json = JsonUtil.buildSortedJson(params);
            String jsonResult = HttpUtility.postData(env, json);
            Map returnMap = (Map) JsonUtil.parseJson(jsonResult);
            String resultCode = StringUtil.trans2Str(returnMap.get("field039"));
            String resultMsg = StringUtil.trans2Str(returnMap.get("field124"));
            String backSign = StringUtil.trans2Str(returnMap.get("field128"));
            if (returnMap != null && "00".equals(resultCode) && returnMap.get("field055") != null) {
                String[] tradeData = ((String) returnMap.get("field055")).split("\\|");
                if (tradeData != null && "1".equals(tradeData[6])) {
                    wxpayScanCode.setPay_result("0");
                    wxpayScanCode.setTime_end(tradeData[3]);
                    wxpayScanCode.setOut_transaction_id(tradeData[9]);
                    wxpayScanCode.setTrade_state(TradeStateEnum.SUCCESS.getCode());
                } else if (tradeData != null && "0".equals(tradeData[6])) {
                    wxpayScanCode.setTrade_state(TradeStateEnum.NOTPAY.getCode());
                } else {
                    wxpayScanCode.setTrade_state(TradeStateEnum.PAYERROR.getCode());
                }
                this.wxNativeDao.update(wxpayScanCode);
            }
        }
        return wxpayScanCode;
    }

    @Override
    public String saveResultNotify(Map<String, String> resultMap) {
        String field055 = resultMap.get("field055");
        String[] notifyData = field055.split("\\|");
        WxpayScanCode notifyBeanTemp = this.wxNativeDao.getById(notifyData[0]);
        if (notifyBeanTemp == null || !String.valueOf(notifyBeanTemp.getTotal_fee()).equals(Integer.valueOf(notifyData[3]).toString())) {
            return "notexist";
        }
        String notifyBack = "";
        if (!TradeStateEnum.SUCCESS.getCode().equals(notifyBeanTemp.getTrade_state())) {
            if ("00".equals(notifyData[1])) {
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
            if ("00".equals(notifyData[1])) {
                backNotify.setPay_result("0");
                backNotify.setTimeEnd(notifyBeanTemp.getTime_end());
            } else {
                backNotify.setPay_result("1");
                backNotify.setPay_info(notifyData[4]);
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
        QrcodeChannelInf qrChannelInf = this.qrcodeMchtInfoDaoImpl.getChannelInf(notifyBeanTemp.getChannel_id(), notifyBeanTemp.getMch_id());
        LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
        params.put("txcode", "F60006");
        params.put("txdate", notifyBeanTemp.getTime_start().substring(0, 8));
        params.put("txtime", notifyBeanTemp.getTime_start().substring(8));
        params.put("version", "2.0.0");
        if (TradeSourceEnum.ALIPAY.getCode().equals(notifyBeanTemp.getTrade_source())) {
            params.put("field003", "900022");
        } else {
            params.put("field003", "900021");
        }
        params.put("field011", notifyBeanTemp.getOut_trade_no().substring(notifyBeanTemp.getOut_trade_no().length() - 6));
        params.put("field039", "00");
        params.put("field041", resultMap.get("field041"));
        params.put("field042", resultMap.get("field042"));
        params.put("field062", resultMap.get("field062"));
        params.put("field0124", "\u63a5\u6536\u901a\u77e5\u6210\u529f");
        params.put("field125", "111111");
        params.put("field128", GuangdaUtil.md5SignWithValue(params, qrChannelInf.getSecret_key(), "UTF-8").substring(0, 16));
        String json = JsonUtil.buildSortedJson(params);
        return json;
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
