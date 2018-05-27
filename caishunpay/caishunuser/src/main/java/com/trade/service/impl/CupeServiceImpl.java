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
import com.gy2cupe.util.CupeUtil;
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
import com.trade.service.CupeService;
import com.trade.service.impl.CommonService;
import com.trade.util.GuangdaUtil;
import com.trade.util.JsonUtil;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CupeServiceImpl
        extends CommonService
        implements CupeService {
    @Autowired
    private WxNativeDao wxNativeDao;
    @Autowired
    private QrcodeMchtInfoDao qrcodeMchtInfoDaoImpl;
    private static Logger log = Logger.getLogger(CupeServiceImpl.class);

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
        String timeMillis = String.valueOf(System.currentTimeMillis());
        String cupeRandom = CupeUtil.getRandom(16);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("p", qrChannelInf.getAgtId());
        params.put("t", timeMillis);
        params.put("r", cupeRandom);
        params.put("n", SysParamUtil.getParam("CUPE_NOTIFY_URL"));
        params.put("p0", "");
        params.put("p1", qrChannelInf.getChannel_mcht_no());
        params.put("p2", tradeNo);
        params.put("p3", String.valueOf(reqParam.getOrderAmount()));
        params.put("s", GuangdaUtil.getMd5SignWithValueByMap(params, qrChannelInf.getSecret_key(), "utf-8"));
        String keyValue = GuangdaUtil.map2HttpParam(params);
        wxpay.setService("cupe");
        wxpay.setMch_id(qrChannelInf.getChannel_mcht_no());
        wxpay.setMch_create_ip(reqParam.getRemoteAddr());
        wxpay.setChannel_id(qrChannelInf.getChannel_id());
        wxpay.setTime_start(currTime);
        wxpay.setOut_trade_no(tradeNo);
        wxpay.setTradeSn(reqParam.getTradeSn());
        wxpay.setTotal_fee(reqParam.getOrderAmount());
        wxpay.setBody(reqParam.getGoodsName());
        wxpay.setNotify_url(SysParamUtil.getParam("CUPE_NOTIFY_URL"));
        wxpay.setT0Flag(reqParam.getT0Flag());
        wxpay.setGymchtId(reqParam.getGymchtId());
        wxpay.setGy_notifyUrl(reqParam.getNotifyUrl());
        String resultJson = HttpUtility.postData(Environment.CUPE_GET_PAY_URL, keyValue);
        Map returnMap = (Map) JsonUtil.parseJson(resultJson);
        String resultCode = StringUtil.trans2Str(returnMap.get("RESULT"));
        String body = StringUtil.trans2Str(returnMap.get("BODY"));
        if ("0".equals(resultCode)) {
            response.setResultCode(ResponseEnum.SUCCESS.getCode());
            response.setCode_url(StringUtil.trans2Str(body));
            wxpay.setCode_url(response.getCode_url());
            wxpay.setTrade_state(TradeStateEnum.NOTPAY.getCode());
            wxpay.setResp_code("00");
            wxpay.setMessage("\u8bf7\u6c42\u6210\u529f");
        } else {
            response.setResultCode(resultCode);
            response.setMessage(body);
            wxpay.setMessage(body);
        }
        wxpay.setTrade_source("");
        wxpay.setResult_code(resultCode);
        this.wxNativeDao.save(wxpay);
        return response;
    }

    @Override
    public WxpayScanCode queryByTradesn(String tradeSn, String gyMchtId, QrcodeChannelInf qrChannelInf) {
        WxpayScanCode wxpayScanCode = this.wxNativeDao.getByTradesn(tradeSn, gyMchtId);
        if (wxpayScanCode != null && StringUtil.isNotEmpty(wxpayScanCode.getCode_url()) && !TradeStateEnum.SUCCESS.getCode().equals(wxpayScanCode.getTrade_state())) {
            String cupeRandom = CupeUtil.getRandom(16);
            String timeMillis = String.valueOf(System.currentTimeMillis());
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("p", "160069");
            params.put("t", timeMillis);
            params.put("r", cupeRandom);
            params.put("p0", "");
            params.put("p1", qrChannelInf.getChannel_mcht_no());
            params.put("p2", wxpayScanCode.getOut_trade_no());
            params.put("s", GuangdaUtil.getMd5SignWithValueByMap(params, qrChannelInf.getSecret_key(), "utf-8"));
            String keyValue = GuangdaUtil.map2HttpParam(params);
            String resultJson = HttpUtility.postData(Environment.CUPE_QUERY_PAY_URL, keyValue);
            Map returnMap = (Map) JsonUtil.parseJson(resultJson);
            if (returnMap != null && "0".equals(StringUtil.trans2Str(returnMap.get("RESULT")))) {
                Map resultMap = (Map) JsonUtil.parseJson(StringUtil.trans2Str(returnMap.get("BODY")));
                if (resultMap != null && "1".equals(resultMap.get("s"))) {
                    wxpayScanCode.setPay_result("0");
                    wxpayScanCode.setTime_end(StringUtil.trans2Str(resultMap.get("p2")));
                    wxpayScanCode.setTransaction_id(StringUtil.trans2Str(resultMap.get("p0")));
                    wxpayScanCode.setOut_transaction_id(StringUtil.trans2Str(resultMap.get("p5")));
                    wxpayScanCode.setTrade_source(StringUtil.trans2Str(resultMap.get("p1")));
                    wxpayScanCode.setTrade_state(TradeStateEnum.SUCCESS.getCode());
                    this.wxNativeDao.update(wxpayScanCode);
                } else if (resultMap != null && !"0".equals(resultMap.get("s"))) {
                    wxpayScanCode.setTrade_state(TradeStateEnum.PAYERROR.getCode());
                    this.wxNativeDao.update(wxpayScanCode);
                }
            }
        }
        return wxpayScanCode;
    }

    @Override
    public WxpayScanCode queryTrade(String outTradeNo) {
        return null;
    }

    @Override
    public String saveResultNotify(Map<String, String> resultMap) {
        WxpayScanCode notifyBeanTemp = this.wxNativeDao.getById(resultMap.get("p7"));
        if (notifyBeanTemp == null || !String.valueOf(notifyBeanTemp.getTotal_fee()).equals(resultMap.get("p3"))) {
            return "notexist";
        }
        String notifyBack = "";
        if (!TradeStateEnum.SUCCESS.getCode().equals(notifyBeanTemp.getTrade_state())) {
            if ("0".equals(resultMap.get("c"))) {
                this.tradeMchtAccountService.notifySuccess(notifyBeanTemp);
                notifyBeanTemp.setTrade_state(TradeStateEnum.SUCCESS.getCode());
                notifyBeanTemp.setTransaction_id(StringUtil.trans2Str(resultMap.get("p0")));
                notifyBeanTemp.setOut_transaction_id(StringUtil.trans2Str(resultMap.get("p5")));
                notifyBeanTemp.setTrade_source(StringUtil.trans2Str(resultMap.get("p1")));
            } else {
                notifyBeanTemp.setTrade_state(TradeStateEnum.PAYERROR.getCode());
            }
            notifyBeanTemp.setTime_end(resultMap.get("p2"));
        }
        if (notifyBeanTemp.getGy_notifyUrl() != null && !"success".equals(notifyBeanTemp.getGynotify_back())) {
            Environment evn = Environment.createEnvironment(notifyBeanTemp.getGy_notifyUrl(), "utf-8", "application/json");
            QrcodeMchtInfo qrcodeMcht = this.qrcodeMchtInfoDaoImpl.getMchtInfo(notifyBeanTemp.getGymchtId());
            ResponseQuery backNotify = this.buildResponseQuery(notifyBeanTemp);
            if ("0".equals(resultMap.get("c"))) {
                backNotify.setPay_result("0");
                backNotify.setTimeEnd(notifyBeanTemp.getTime_end());
            } else {
                backNotify.setPay_result(resultMap.get("c"));
                backNotify.setPay_info("\u652f\u4ed8\u5931\u8d25");
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
}
