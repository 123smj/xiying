/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  org.apache.log4j.Logger
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.trade.service.impl;

import com.account.service.TradeMchtAccountService;
import com.alibaba.fastjson.JSONObject;
import com.gy.system.Environment;
import com.gy.system.SysParamUtil;
import com.gy.util.DateUtil;
import com.gy.util.HttpUtility;
import com.gy.util.StringUtil;
import com.gy.util.UUIDGenerator;
import com.manage.bean.ChannelRegisterBean;
import com.manage.service.ChannelRegisterService;
import com.trade.bean.WxpayScanCode;
import com.trade.bean.own.QrcodeChannelInf;
import com.trade.bean.own.QrcodeMchtInfo;
import com.trade.bean.own.TradeParam;
import com.trade.bean.response.ResponseCode;
import com.trade.bean.response.ResponseQuery;
import com.trade.dao.QrcodeMchtInfoDao;
import com.trade.dao.WxNativeDao;
import com.trade.enums.ChannelInfoEnum;
import com.trade.enums.ChannelStatusEnum;
import com.trade.enums.ResponseEnum;
import com.trade.enums.TradeSourceEnum;
import com.trade.enums.TradeStateEnum;
import com.trade.service.TradeService;
import com.trade.service.impl.CommonService;
import com.trade.util.GuangdaUtil;
import com.trade.util.JsonUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

@Service
public class HfbankServiceImpl
        extends CommonService
        implements TradeService,
        ChannelRegisterService {
    @Autowired
    private WxNativeDao wxNativeDao;
    @Autowired
    private QrcodeMchtInfoDao qrcodeMchtInfoDaoImpl;
    private static Map<String, String> mchtData = new HashMap<String, String>() {
        {
            this.put("714974", "0164f72739897d73aa4dcf2ffe84f31c");
        }
    };
    private static Logger log = Logger.getLogger(HfbankServiceImpl.class);

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
        String payType = "hf_newWeixinPay";
        if (TradeSourceEnum.ALIPAY.getCode().equals(reqParam.getTradeSource())) {
            wxpay.setTrade_source(TradeSourceEnum.ALIPAY.getCode());
            payType = "hf_newALiPay";
        } else if (TradeSourceEnum.WEPAY.getCode().equals(reqParam.getTradeSource())) {
            wxpay.setTrade_source(TradeSourceEnum.WEPAY.getCode());
            payType = "hf_newWeixinPay";
        } else {
            response.setResultCode(ResponseEnum.ERROR_UNSUPPORT.getCode());
            response.setMessage(ResponseEnum.ERROR_UNSUPPORT.getMemo());
            return response;
        }
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("account", qrChannelInf.getChannel_mcht_no());
        params.put("amount", String.valueOf(reqParam.getOrderAmount()));
        params.put("userid", qrChannelInf.getAgtId());
        String sign = GuangdaUtil.getMd5KeyedSignByMap(params, qrChannelInf.getSecret_key(), "utf-8");
        params.put("orderCode", payType);
        params.put("pay_number", tradeNo);
        params.put("notify_url", SysParamUtil.getParam("HFBANK_NOTIFY_URL"));
        params.put("privatekey", qrChannelInf.getPrimary_key());
        params.put("info", reqParam.getGoodsName());
        params.put("sign", sign);
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
        wxpay.setNotify_url(SysParamUtil.getParam("HFBANK_NOTIFY_URL"));
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
        String payType = "hf_newWeixinPay";
        if (TradeSourceEnum.ALIPAY.getCode().equals(wxpayScanCode.getTrade_source())) {
            payType = "hf_newALiPay";
        } else if (TradeSourceEnum.WEPAY.getCode().equals(wxpayScanCode.getTrade_source())) {
            payType = "hf_newWeixinPay";
        } else {
            response.setResultCode(ResponseEnum.ERROR_UNSUPPORT.getCode());
            response.setMessage(ResponseEnum.ERROR_UNSUPPORT.getMemo());
            return response;
        }
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("account", qrChannelInf.getChannel_mcht_no());
        params.put("amount", String.valueOf(wxpayScanCode.getTotal_fee()));
        params.put("userid", qrChannelInf.getAgtId());
        String sign = GuangdaUtil.getMd5KeyedSignByMap(params, qrChannelInf.getSecret_key(), "utf-8");
        params.put("orderCode", payType);
        params.put("pay_number", wxpayScanCode.getOut_trade_no());
        params.put("notify_url", SysParamUtil.getParam("HFBANK_NOTIFY_URL"));
        params.put("privatekey", qrChannelInf.getPrimary_key());
        params.put("info", wxpayScanCode.getBody());
        params.put("sign", sign);
        wxpayScanCode.setTrade_type(payType);
        wxpayScanCode.setService(payType);
        wxpayScanCode.setNotify_url(SysParamUtil.getParam("HFBANK_NOTIFY_URL"));
        return this.connectChannel(wxpayScanCode, params);
    }

    private ResponseCode connectChannel(WxpayScanCode wxpayScanCode, Map<String, String> params) {
        ResponseCode response = new ResponseCode();
        Environment env = Environment.HFBANK_PAY_URL;
        String keyValue = GuangdaUtil.map2HttpParam(params);
        String jsonResult = HttpUtility.postData(env, keyValue);
        if (StringUtil.isEmpty(jsonResult)) {
            response.setResultCode(ResponseEnum.BACK_EXCEPTION.getCode());
            response.setMessage(ResponseEnum.BACK_EXCEPTION.getMemo());
            return response;
        }
        Map returnMap = (Map) JsonUtil.parseJson(jsonResult);
        String resultCode = StringUtil.trans2Str(returnMap.get("respCode"));
        String resultMsg = StringUtil.trans2Str(returnMap.get("respInfo"));
        if ("000000".equals(resultCode)) {
            response.setResultCode(ResponseEnum.SUCCESS.getCode());
            response.setCode_url(StringUtil.trans2Str(returnMap.get("QRcodeURL")));
            wxpayScanCode.setTransaction_id(StringUtil.trans2Str(returnMap.get("orderId")));
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
            params.put("account", qrChannelInf.getChannel_mcht_no());
            params.put("orderId", wxpayScanCode.getTransaction_id());
            params.put("userid", qrChannelInf.getAgtId());
            String sign = GuangdaUtil.getMd5KeyedSignByMap(params, qrChannelInf.getSecret_key(), "utf-8");
            params.put("orderCode", "tb_OrderConfirm");
            params.put("privatekey", qrChannelInf.getPrimary_key());
            params.put("sign", sign);
            Environment env = Environment.HFBANK_PAY_URL;
            String keyValue = GuangdaUtil.map2HttpParam(params);
            String jsonResult = HttpUtility.postData(env, keyValue);
            Map returnMap = (Map) JsonUtil.parseJson(jsonResult);
            String resultCode = StringUtil.trans2Str(returnMap.get("respCode"));
            if (returnMap != null) {
                if ("000000".equals(resultCode)) {
                    wxpayScanCode.setPay_result("0");
                    wxpayScanCode.setTime_end(DateUtil.getCurrTime());
                    wxpayScanCode.setTrade_state(TradeStateEnum.SUCCESS.getCode());
                } else if (!"500001".equals(resultCode)) {
                    wxpayScanCode.setTrade_state(TradeStateEnum.PAYERROR.getCode());
                }
                this.wxNativeDao.update(wxpayScanCode);
            }
        }
        return wxpayScanCode;
    }

    @Override
    public String saveResultNotify(Map<String, String> resultMap) {
        WxpayScanCode notifyBeanTemp = this.wxNativeDao.getById(resultMap.get("pay_number"));
        if (notifyBeanTemp == null || !String.valueOf(notifyBeanTemp.getTotal_fee()).equals(resultMap.get("amount"))) {
            return "notexist";
        }
        String sign = resultMap.get("sign");
        HashMap<String, String> checkSignMap = new HashMap<String, String>();
        checkSignMap.put("pay_number", resultMap.get("pay_number"));
        checkSignMap.put("amount", resultMap.get("amount"));
        checkSignMap.put("orderId", resultMap.get("orderId"));
        QrcodeChannelInf qrcodeChannelInf = this.qrcodeMchtInfoDaoImpl.getChannelInf(notifyBeanTemp.getChannel_id(), notifyBeanTemp.getMch_id());
        String checkSign = GuangdaUtil.getMd5KeyedSignByMap(checkSignMap, qrcodeChannelInf.getSecret_key(), "utf-8");
        if (!checkSign.equalsIgnoreCase(sign)) {
            return "sign error";
        }
        String notifyBack = "";
        if (!TradeStateEnum.SUCCESS.getCode().equals(notifyBeanTemp.getTrade_state())) {
            if ("000000".equals(resultMap.get("respCode"))) {
                notifyBeanTemp.setTrade_state(TradeStateEnum.SUCCESS.getCode());
                notifyBeanTemp.setOut_transaction_id(resultMap.get("WXOrderNo"));
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
            if ("000000".equals(resultMap.get("respCode"))) {
                backNotify.setPay_result("0");
                backNotify.setTimeEnd(notifyBeanTemp.getTime_end());
            } else {
                backNotify.setPay_result("1");
                backNotify.setPay_info(resultMap.get("respCode"));
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

    @Override
    public String registerChannelMcht(QrcodeChannelInf channelInf) {
        String assertResult = this.assertChannel(channelInf);
        if (!ResponseEnum.SUCCESS.getCode().equals(assertResult)) {
            return assertResult;
        }
        if (this.qrcodeMchtInfoDaoImpl.getChannelInf(ChannelInfoEnum.hfbank.getCode(), channelInf.getChannel_mcht_no()) != null) {
            return "\u6e20\u9053\u5546\u6237\u53f7\u91cd\u590d";
        }
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("account", channelInf.getChannel_mcht_no());
        params.put("pass", channelInf.getPass_word());
        params.put("userid", channelInf.getAgtId());
        String sign = GuangdaUtil.getMd5KeyedSignByMap(params, mchtData.get(channelInf.getAgtId()), "utf-8");
        params.put("orderCode", "hf_newRegister");
        params.put("sign", sign);
        Environment env = Environment.HFBANK_PAY_URL;
        String keyValue = GuangdaUtil.map2HttpParam(params);
        String jsonResult = HttpUtility.postData(env, keyValue);
        if (StringUtil.isEmpty(jsonResult)) {
            return ResponseEnum.BACK_EXCEPTION.getMemo();
        }
        Map returnMap = (Map) JsonUtil.parseJson(jsonResult);
        String resultCode = StringUtil.trans2Str(returnMap.get("respCode"));
        String resultMsg = StringUtil.trans2Str(returnMap.get("respInfo"));
        if ("000000".equals(resultCode)) {
            channelInf.setSecret_key(mchtData.get(channelInf.getAgtId()));
            channelInf.setChannel_id("hfbank");
            this.qrcodeMchtInfoDaoImpl.saveChannelInf(channelInf);
            String downloadResult = this.downLoadKey(channelInf);
            if (!downloadResult.equals(ResponseEnum.SUCCESS.getCode())) {
                return "\u4e0b\u8f7d\u5bc6\u94a5\u5931\u8d25\uff1a" + downloadResult;
            }
        } else {
            return resultMsg;
        }
        return ResponseEnum.SUCCESS.getCode();
    }

    @Override
    public String downLoadKey(QrcodeChannelInf channelInf) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("account", channelInf.getChannel_mcht_no());
        params.put("pass", channelInf.getPass_word());
        params.put("userid", channelInf.getAgtId());
        String sign = GuangdaUtil.getMd5KeyedSignByMap(params, mchtData.get(channelInf.getAgtId()), "utf-8");
        params.put("orderCode", "hf_newDownLoadKey");
        params.put("sign", sign);
        Environment env = Environment.HFBANK_PAY_URL;
        String keyValue = GuangdaUtil.map2HttpParam(params);
        String jsonResult = HttpUtility.postData(env, keyValue);
        if (StringUtil.isEmpty(jsonResult)) {
            return ResponseEnum.BACK_EXCEPTION.getMemo();
        }
        Map returnMap = (Map) JsonUtil.parseJson(jsonResult);
        String resultCode = StringUtil.trans2Str(returnMap.get("respCode"));
        String resultMsg = StringUtil.trans2Str(returnMap.get("respInfo"));
        if (!"000000".equals(resultCode)) {
            return resultMsg;
        }
        channelInf.setPrimary_key((String) returnMap.get("privatekey"));
        this.qrcodeMchtInfoDaoImpl.updateChannelInf(channelInf);
        return ResponseEnum.SUCCESS.getCode();
    }

    private String assertChannel(QrcodeChannelInf channelInf) {
        if (StringUtil.isEmpty(channelInf.getChannel_mcht_no())) {
            return "\u5546\u6237\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(channelInf.getAgtId())) {
            return "\u673a\u6784\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
        }
        if (StringUtil.isEmpty(channelInf.getPass_word())) {
            return "\u5bc6\u7801\u4e0d\u80fd\u4e3a\u7a7a";
        }
        return ResponseEnum.SUCCESS.getCode();
    }

    @Override
    public String verifyInfo(ChannelRegisterBean channelRegister) {
        QrcodeChannelInf channelInf = this.qrcodeMchtInfoDaoImpl.getChannelInf(ChannelInfoEnum.hfbank.getCode(), channelRegister.getAccount());
        if (channelInf == null) {
            return "\u901a\u9053\u5546\u6237\u4e0d\u5b58\u5728\uff0c\u8bf7\u6838\u5bf9";
        }
        if (StringUtil.isEmpty(channelInf.getPrimary_key())) {
            return "\u4e3b\u5bc6\u94a5\u4e3a\u7a7a,\u8bf7\u5148\u4e0b\u8f7d\u4e3b\u5bc6\u94a5";
        }
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("account", channelRegister.getAccount());
        params.put("real_name", channelRegister.getReal_name());
        params.put("cmer", channelRegister.getCmer());
        params.put("cmer_sort", channelRegister.getCmer_sort());
        params.put("location", channelRegister.getLocation());
        params.put("card_no", channelRegister.getCard_no());
        params.put("cert_no", channelRegister.getCert_no());
        params.put("mobile", channelRegister.getMobile());
        params.put("phone", channelRegister.getPhone());
        params.put("regionCode", channelRegister.getRegionCode());
        params.put("address", channelRegister.getAddress());
        params.put("userid", channelRegister.getUserid());
        String sign = GuangdaUtil.getMd5KeyedSignByMap(params, mchtData.get(channelRegister.getUserid()), "utf-8");
        params.put("privatekey", channelInf.getPrimary_key());
        params.put("cert_correct", HfbankServiceImpl.GetImageStr(channelRegister.getCert_correct_path()));
        params.put("cert_opposite", HfbankServiceImpl.GetImageStr(channelRegister.getCert_opposite_path()));
        params.put("cert_meet", HfbankServiceImpl.GetImageStr(channelRegister.getCert_meet_path()));
        params.put("card_correct", HfbankServiceImpl.GetImageStr(channelRegister.getCard_correct_path()));
        params.put("card_opposite", HfbankServiceImpl.GetImageStr(channelRegister.getCard_opposite_path()));
        params.put("bl_img", HfbankServiceImpl.GetImageStr(channelRegister.getBl_img_path()));
        params.put("door_img", HfbankServiceImpl.GetImageStr(channelRegister.getDoor_img_path()));
        params.put("cashier_img", HfbankServiceImpl.GetImageStr(channelRegister.getCashier_img_path()));
        params.put("orderCode", "hf_newVerifyInfo");
        params.put("channel_code", channelRegister.getChannel_code());
        params.put("verifyUrl", SysParamUtil.getParam("HFBANK_VERIFY_NOTIFY_URL"));
        params.put("sign", sign);
        channelInf.setPhone(channelRegister.getPhone());
        channelInf.setIdentity_no(channelRegister.getCert_no());
        channelInf.setBank_card_no(channelRegister.getCard_no());
        channelInf.setCard_name(channelRegister.getReal_name());
        channelInf.setAddress(channelRegister.getAddress());
        channelInf.setChannel_name(channelRegister.getCmer());
        Environment env = Environment.HFBANK_PAY_URL;
        String keyValue = GuangdaUtil.map2HttpParam(params);
        String jsonResult = HttpUtility.postData(env, keyValue);
        if (StringUtil.isEmpty(jsonResult)) {
            return ResponseEnum.BACK_EXCEPTION.getMemo();
        }
        Map returnMap = (Map) JsonUtil.parseJson(jsonResult);
        String resultCode = StringUtil.trans2Str(returnMap.get("respCode"));
        String resultMsg = StringUtil.trans2Str(returnMap.get("respInfo"));
        String response = "";
        if ("000000".equals(resultCode)) {
            channelInf.setStatus(ChannelStatusEnum.ADD_FOR_CHECK.getCode());
            response = ResponseEnum.SUCCESS.getCode();
        } else {
            response = String.valueOf(resultCode) + "----" + resultMsg;
        }
        this.qrcodeMchtInfoDaoImpl.updateChannelInf(channelInf);
        return response;
    }

    public static String GetImageStr(String filePath) {
        String[] strA = filePath.split("\\.");
        FileInputStream in = null;
        byte[] data = null;
        try {
            in = new FileInputStream(filePath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        JSONObject json = new JSONObject();
        json.put("content", (Object) encoder.encode(data));
        json.put("suffix", (Object) strA[strA.length - 1]);
        return json.toJSONString();
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
