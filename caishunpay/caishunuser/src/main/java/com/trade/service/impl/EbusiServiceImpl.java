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
import com.trade.enums.TradeSourceEnum;
import com.trade.enums.TradeStateEnum;
import com.trade.service.TradeService;
import com.trade.service.impl.CommonService;
import com.trade.util.GuangdaUtil;
import com.trade.util.JsonUtil;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EbusiServiceImpl
        extends CommonService
        implements TradeService {
    @Autowired
    private WxNativeDao wxNativeDao;
    @Autowired
    private QrcodeMchtInfoDao qrcodeMchtInfoDaoImpl;
    private static Logger log = Logger.getLogger(EbusiServiceImpl.class);

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
        params.put("groupId", qrChannelInf.getAgtId());
        params.put("signType", "MD5");
        params.put("datetime", currTime);
        params.put("merchantCode", qrChannelInf.getChannel_mcht_no());
        params.put("terminalCode", "02059626");
        params.put("orderNum", tradeNo);
        params.put("payMoney", String.valueOf(reqParam.getOrderAmount()));
        params.put("productName", reqParam.getGoodsName());
        params.put("notifyUrl", SysParamUtil.getParam("EBUSI_NOTIFY_URL"));
        if (TradeSourceEnum.ALIPAY.getCode().equals(reqParam.getTradeSource())) {
            params.put("service", "SMZF005");
            wxpay.setTrade_source(TradeSourceEnum.ALIPAY.getCode());
        } else {
            params.put("service", "SMZF004");
            wxpay.setTrade_source(TradeSourceEnum.WEPAY.getCode());
        }
        params.put("sign", GuangdaUtil.getMd5SignByMapChinaSort(params, qrChannelInf.getSecret_key(), "utf-8").toLowerCase());
        String keyValue = GuangdaUtil.map2HttpParam(params);
        wxpay.setService("ebusi");
        wxpay.setMch_id(qrChannelInf.getChannel_mcht_no());
        wxpay.setMch_create_ip(reqParam.getRemoteAddr());
        wxpay.setChannel_id(qrChannelInf.getChannel_id());
        wxpay.setTime_start(currTime);
        wxpay.setOut_trade_no(tradeNo);
        wxpay.setTradeSn(reqParam.getTradeSn());
        wxpay.setTotal_fee(reqParam.getOrderAmount());
        wxpay.setBody(reqParam.getGoodsName());
        wxpay.setNotify_url(SysParamUtil.getParam("EBUSI_NOTIFY_URL"));
        wxpay.setT0Flag(reqParam.getT0Flag());
        wxpay.setGymchtId(reqParam.getGymchtId());
        wxpay.setGy_notifyUrl(reqParam.getNotifyUrl());
        String resultJson = HttpUtility.postData(Environment.EBUSI_PAY_URL, keyValue);
        Map returnMap = (Map) JsonUtil.parseJson(resultJson);
        String backSign = StringUtil.trans2Str(returnMap.get("sign"));
        returnMap.remove("sign");
        String checkSign = GuangdaUtil.getMd5SignByMapChinaSort(returnMap, qrChannelInf.getSecret_key(), "utf-8").toLowerCase();
        if (backSign == null || !backSign.equals(checkSign)) {
            System.out.println("\u9a8c\u7b7e\u5931\u8d25");
        }
        String resultCode = StringUtil.trans2Str(returnMap.get("pl_code"));
        String message = StringUtil.trans2Str(returnMap.get("pl_message"));
        if ("0000".equals(resultCode)) {
            response.setResultCode(ResponseEnum.SUCCESS.getCode());
            response.setCode_url(StringUtil.decode(StringUtil.trans2Str(returnMap.get("pl_url")), "utf-8"));
            wxpay.setCode_url(response.getCode_url());
            wxpay.setTransaction_id(StringUtil.trans2Str(returnMap.get("pl_orderNum")));
            wxpay.setTrade_state(TradeStateEnum.NOTPAY.getCode());
            wxpay.setResp_code("00");
            wxpay.setMessage("\u8bf7\u6c42\u6210\u529f");
        } else {
            response.setResultCode(resultCode);
            response.setMessage(message);
            wxpay.setMessage(message);
        }
        wxpay.setResult_code(resultCode);
        this.wxNativeDao.save(wxpay);
        return response;
    }

    @Override
    public ResponseCode doTrade(WxpayScanCode wxpayScanCode) {
        return null;
    }

    @Override
    public WxpayScanCode queryByTradesn(String tradeSn, String gyMchtId, QrcodeChannelInf qrChannelInf) {
        WxpayScanCode wxpayScanCode = this.wxNativeDao.getByTradesn(tradeSn, gyMchtId);
        if (wxpayScanCode != null && StringUtil.isNotEmpty(wxpayScanCode.getCode_url()) && !TradeStateEnum.SUCCESS.getCode().equals(wxpayScanCode.getTrade_state())) {
            String cupeRandom = CupeUtil.getRandom(16);
            String timeMillis = String.valueOf(System.currentTimeMillis());
            String currTime = DateUtil.getCurrTime();
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("groupId", qrChannelInf.getAgtId());
            params.put("signType", "MD5");
            params.put("datetime", currTime);
            params.put("merchantCode", qrChannelInf.getChannel_mcht_no());
            params.put("orderNum", wxpayScanCode.getOut_trade_no());
            params.put("service", "SMZF006");
            params.put("sign", GuangdaUtil.getMd5SignByMapChinaSort(params, qrChannelInf.getSecret_key(), "utf-8").toLowerCase());
            String keyValue = GuangdaUtil.map2HttpParam(params);
            String resultJson = HttpUtility.postData(Environment.EBUSI_PAY_URL, keyValue);
            Map returnMap = (Map) JsonUtil.parseJson(resultJson);
            String backSign = StringUtil.trans2Str(returnMap.get("sign"));
            returnMap.remove("sign");
            String checkSign = GuangdaUtil.getMd5SignByMapChinaSort(returnMap, qrChannelInf.getSecret_key(), "utf-8").toLowerCase();
            if (backSign == null || !backSign.equals(checkSign)) {
                System.out.println("\u9a8c\u7b7e\u5931\u8d25");
            }
            if (returnMap != null && "0000".equals(StringUtil.trans2Str(returnMap.get("pl_code")))) {
                if ("4".equals(StringUtil.trans2Str(returnMap.get("pl_payState")))) {
                    wxpayScanCode.setPay_result("0");
                    wxpayScanCode.setTime_end(StringUtil.trans2Str(currTime));
                    wxpayScanCode.setTrade_state(TradeStateEnum.SUCCESS.getCode());
                    this.wxNativeDao.update(wxpayScanCode);
                } else if ("5".equals(returnMap.get("pl_payState"))) {
                    wxpayScanCode.setTrade_state(TradeStateEnum.PAYERROR.getCode());
                    this.wxNativeDao.update(wxpayScanCode);
                }
            }
        }
        return wxpayScanCode;
    }

    @Override
    public String saveResultNotify(Map<String, String> resultMap) {
        WxpayScanCode notifyBeanTemp = this.wxNativeDao.getById(resultMap.get("orderNum"));
        if (notifyBeanTemp == null) {
            return "notexist";
        }
        String notifyBack = "";
        if (!TradeStateEnum.SUCCESS.getCode().equals(notifyBeanTemp.getTrade_state())) {
            if ("4".equals(resultMap.get("pl_payState"))) {
                notifyBeanTemp.setTrade_state(TradeStateEnum.SUCCESS.getCode());
            } else {
                notifyBeanTemp.setTrade_state(TradeStateEnum.PAYERROR.getCode());
            }
            notifyBeanTemp.setTime_end(DateUtil.getCurrTime());
        }
        if (notifyBeanTemp.getGy_notifyUrl() != null) {
            Environment evn = Environment.createEnvironment(notifyBeanTemp.getGy_notifyUrl(), "utf-8", "application/json");
            QrcodeMchtInfo qrcodeMcht = this.qrcodeMchtInfoDaoImpl.getMchtInfo(notifyBeanTemp.getGymchtId());
            ResponseQuery backNotify = this.buildResponseQuery(notifyBeanTemp);
            if ("4".equals(resultMap.get("pl_payState"))) {
                backNotify.setPay_result("0");
                backNotify.setTimeEnd(notifyBeanTemp.getTime_end());
            } else {
                backNotify.setPay_result(resultMap.get("pl_payState"));
                backNotify.setPay_info("\u652f\u4ed8\u5931\u8d25");
            }
            backNotify.setSign(GuangdaUtil.getMd5Sign(backNotify, qrcodeMcht.getSecretKey()));
            String jsonStr = JsonUtil.buildJson(backNotify);
            log.info((Object) ("\u56de\u8c03\u63a5\u5165\u65b9:" + notifyBeanTemp.getGy_notifyUrl() + "----" + jsonStr));
            notifyBack = HttpUtility.postData(evn, jsonStr);
            log.info((Object) ("\u56de\u8c03\u63a5\u5165\u65b9\u8fd4\u56de:" + notifyBack));
        }
        this.buildNotifyBack(notifyBeanTemp, notifyBack);
        this.wxNativeDao.update(notifyBeanTemp);
        return "SUCCESS";
    }

    @Override
    public ResponseCode getWxjspay(TradeParam reqParam, QrcodeChannelInf qrChannelInf) {
        return null;
    }

    public static void df() {
        String currTime = DateUtil.getCurrTime();
        String tradeNo = UUIDGenerator.getOrderIdByUUId();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("groupId", "100469");
        params.put("signType", "MD5");
        params.put("datetime", currTime);
        params.put("merchantCode", "928000000000906");
        params.put("terminalCode", "00018531");
        params.put("orderNum", "2016121300000943267709");
        params.put("transMoney", "2");
        params.put("transDate", currTime.substring(0, 8));
        params.put("transTime", currTime.substring(8));
        params.put("accountName", "\u4f55\u627f\u793c");
        params.put("bankCard", "622909397679593213");
        params.put("bankName", "\u5174\u4e1a\u94f6\u884c");
        params.put("bankLinked", "2432432");
        params.put("service", "SMZF009");
        params.put("sign", GuangdaUtil.getMd5SignByMapChinaSort(params, "ee983b", "utf-8").toLowerCase());
        String keyValue = GuangdaUtil.map2HttpParam(params);
        String resultJson = HttpUtility.postData(Environment.EBUSI_PAY_URL, keyValue);
    }

    @Override
    public WxpayScanCode queryFromChannel(WxpayScanCode wxpayScanCode, QrcodeChannelInf qrChannelInf) {
        return null;
    }

    public static void main(String[] args) {
        EbusiServiceImpl.df();
    }
}
