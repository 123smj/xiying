/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  org.apache.log4j.Logger
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.trade.service.impl;

import com.account.service.TradeMchtAccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.trade.enums.TradeStateEnum;
import com.trade.service.HuikaService;
import com.trade.service.impl.CommonService;
import com.trade.util.GuangdaUtil;
import com.trade.util.JsonUtil;
import com.trade.util.Safe;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HuikaServiceImpl
        extends CommonService
        implements HuikaService {
    @Autowired
    private WxNativeDao wxNativeDao;
    @Autowired
    private QrcodeMchtInfoDao qrcodeMchtInfoDaoImpl;
    private static Logger log = Logger.getLogger(HuikaServiceImpl.class);

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
        String day = DateUtil.getCurrentDay();
        String time = DateUtil.getCurrentTime();
        String payType = "011";
        LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
        params.put("version", "V001");
        params.put("organNo", qrChannelInf.getAgtId());
        params.put("hicardMerchNo", qrChannelInf.getChannel_mcht_no());
        params.put("payType", payType);
        params.put("bizType", "\u96f6\u552e");
        params.put("merchOrderNo", tradeNo);
        params.put("showPage", "0");
        params.put("amount", Integer.toString(reqParam.getOrderAmount()));
        String returnUrl = "http://localhost:8080/return";
        params.put("frontEndUrl", returnUrl);
        params.put("backEndUrl", SysParamUtil.getParam("HUIKA_NOTIFY_URL"));
        params.put("sign", Safe.sign(params, qrChannelInf.getSecret_key()));
        params.put("remark", "\u5907\u6ce8");
        params.put("reserved", "\u9884\u7559\u7a7a\u95f4");
        params.put("isT0", reqParam.getT0Flag());
        params.put("goodsName", reqParam.getGoodsName());
        String json = null;
        try {
            json = new ObjectMapper().writeValueAsString(params);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        wxpay.setService(payType);
        wxpay.setMch_id(qrChannelInf.getChannel_mcht_no());
        wxpay.setMch_create_ip(reqParam.getRemoteAddr());
        wxpay.setChannel_id(qrChannelInf.getChannel_id());
        wxpay.setTrade_type(payType);
        wxpay.setTime_start(String.valueOf(day) + time);
        wxpay.setOut_trade_no(tradeNo);
        wxpay.setTradeSn(reqParam.getTradeSn());
        wxpay.setMch_create_ip(reqParam.getRemoteAddr());
        wxpay.setTotal_fee(reqParam.getOrderAmount());
        wxpay.setBody(reqParam.getGoodsName());
        wxpay.setNotify_url(SysParamUtil.getParam("HUIKA_NOTIFY_URL"));
        wxpay.setT0Flag(reqParam.getT0Flag());
        String resultJson = HttpUtility.postData(Environment.HUIKA_ORDER_CREATE_URL, json);
        Map returnMap = (Map) JsonUtil.parseJson(resultJson);
        wxpay.setT0Flag(reqParam.getT0Flag());
        wxpay.setGymchtId(reqParam.getGymchtId());
        wxpay.setGy_notifyUrl(reqParam.getNotifyUrl());
        wxpay.setChannel_id(qrChannelInf.getChannel_id());
        if ("00".equals(StringUtil.trans2Str(returnMap.get("respCode")))) {
            response.setResultCode(ResponseEnum.SUCCESS.getCode());
            response.setCode_url(StringUtil.trans2Str(returnMap.get("qrURL")));
            wxpay.setTransaction_id(StringUtil.trans2Str(returnMap.get("hicardOrderNo")));
            wxpay.setCode_url(response.getCode_url());
            wxpay.setTrade_state(TradeStateEnum.NOTPAY.getCode());
            wxpay.setResp_code("00");
        } else {
            response.setResultCode(StringUtil.trans2Str(returnMap.get("respCode")));
            response.setMessage(StringUtil.trans2Str(returnMap.get("respMsg")));
        }
        wxpay.setResult_code(StringUtil.trans2Str(returnMap.get("respCode")));
        wxpay.setMessage(StringUtil.trans2Str(returnMap.get("respMsg")));
        this.wxNativeDao.save(wxpay);
        return response;
    }

    @Override
    public WxpayScanCode queryByTradesn(String tradeSn, String gyMchtId, QrcodeChannelInf qrChannelInf) {
        WxpayScanCode wxpayScanCode = this.wxNativeDao.getByTradesn(tradeSn, gyMchtId);
        if (wxpayScanCode != null && StringUtil.isNotEmpty(wxpayScanCode.getCode_url()) && !TradeStateEnum.SUCCESS.getCode().equals(wxpayScanCode.getTrade_state())) {
            String day = DateUtil.getCurrentDay();
            String time = DateUtil.getCurrentTime();
            LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
            params.put("version", "V001");
            params.put("hicardMerchNo", qrChannelInf.getChannel_mcht_no());
            params.put("merchOrderNo", wxpayScanCode.getOut_trade_no());
            params.put("hicardOrderNo", wxpayScanCode.getTransaction_id());
            params.put("sign", Safe.sign(params, qrChannelInf.getSecret_key()));
            params.put("createTime", DateUtil.transDateFormat(wxpayScanCode.getTime_start(), "yyyyMMddHHmmss", "yyyy-MM-dd hh:mm:ss"));
            String json = null;
            try {
                json = new ObjectMapper().writeValueAsString(params);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            String resultJson = HttpUtility.postData(Environment.HUIKA_ORDER_QUERY_URL, json);
            Map resultMap = (Map) JsonUtil.parseJson(resultJson);
            if (resultMap != null) {
                String currTime = DateUtil.getCurrTime();
                if ("00".equals(resultMap.get("respCode"))) {
                    wxpayScanCode.setPay_result("0");
                    wxpayScanCode.setTime_end(DateUtil.transDateFormat(resultMap.get("payTime").toString(), "yyyy-MM-dd hh:mm:ss", "yyyyMMddHHmmss"));
                    wxpayScanCode.setTrade_state(TradeStateEnum.SUCCESS.getCode());
                    this.wxNativeDao.update(wxpayScanCode);
                } else if ("04".equals(resultMap.get("respCode"))) {
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
    public WxpayScanCode queryTrade(String outTradeNo) {
        return null;
    }

    @Override
    public String saveResultNotify(Map<String, String> resultMap) {
        WxpayScanCode notifyBeanTemp = this.wxNativeDao.getById(resultMap.get("merchOrderNo"));
        if (notifyBeanTemp == null || !String.valueOf(notifyBeanTemp.getTotal_fee()).equals(resultMap.get("amount"))) {
            return "notexist";
        }
        String notifyBack = "";
        if (!TradeStateEnum.SUCCESS.getCode().equals(notifyBeanTemp.getTrade_state())) {
            if ("00".equals(resultMap.get("respCode"))) {
                notifyBeanTemp.setTrade_state(TradeStateEnum.SUCCESS.getCode());
                this.tradeMchtAccountService.notifySuccess(notifyBeanTemp);
            } else {
                notifyBeanTemp.setTrade_state(TradeStateEnum.PAYERROR.getCode());
            }
            notifyBeanTemp.setTime_end(DateUtil.transDateFormat(resultMap.get("payTime").toString(), "yyyy-MM-dd hh:mm:ss", "yyyyMMddHHmmss"));
        }
        if (notifyBeanTemp.getGy_notifyUrl() != null && !"success".equals(notifyBeanTemp.getGynotify_back())) {
            Environment evn = Environment.createEnvironment(notifyBeanTemp.getGy_notifyUrl(), "utf-8", "application/json");
            QrcodeMchtInfo qrcodeMcht = this.qrcodeMchtInfoDaoImpl.getMchtInfo(notifyBeanTemp.getGymchtId());
            ResponseQuery backNotify = this.buildResponseQuery(notifyBeanTemp);
            if ("00".equals(resultMap.get("respCode"))) {
                backNotify.setPay_result("0");
                backNotify.setTimeEnd(notifyBeanTemp.getTime_end());
            } else {
                backNotify.setPay_result(resultMap.get("respCode"));
                backNotify.setPay_info(resultMap.get("respMsg"));
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
}
