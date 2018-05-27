/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.log4j.Logger
 *  org.springframework.stereotype.Service
 */
package com.trade.service.quickimpl;

import com.account.service.TradeMchtAccountService;
import com.gy.system.Environment;
import com.gy.system.SysParamUtil;
import com.gy.util.DateUtil;
import com.gy.util.HttpUtility;
import com.gy.util.StringUtil;
import com.gy.util.UUIDGenerator;
import com.trade.bean.QuickpayBean;
import com.trade.bean.own.DfParam;
import com.trade.bean.own.QrcodeChannelInf;
import com.trade.bean.own.QuickpayParam;
import com.trade.dao.QrcodeMchtInfoDao;
import com.trade.dao.QuickpayDao;
import com.trade.enums.ResponseEnum;
import com.trade.enums.TradeStateEnum;
import com.trade.service.DaifuService;
import com.trade.service.quickimpl.QuickpayServiceImpl;
import com.trade.util.GuangdaUtil;
import com.trade.util.JsonUtil;
import com.trade.util.RSA;

import java.io.PrintStream;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class HelibaoQuickServiceImpl
        extends QuickpayServiceImpl
        implements DaifuService {
    private static Logger log = Logger.getLogger(HelibaoQuickServiceImpl.class);

    @Override
    public QuickpayBean doPrepay(QuickpayParam reqParam, QrcodeChannelInf qrChannelInf) {
        QuickpayBean tradeInfo = new QuickpayBean();
        String tradeNo = UUIDGenerator.getOrderIdByUUId("Qk");
        String timeStart = DateUtil.getCurrTime();
        try {
            String payType = "SCAN";
            LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
            params.put("P1_bizType", "QuickPayCreateOrder");
            params.put("P2_customerNumber", qrChannelInf.getChannel_mcht_no());
            params.put("P3_userId", reqParam.getGymchtId());
            params.put("P4_orderId", tradeNo);
            params.put("P5_timestamp", timeStart);
            params.put("P6_payerName", reqParam.getCardHolderName());
            params.put("P7_idCardType", "IDCARD");
            params.put("P8_idCardNo", reqParam.getCerNumber());
            params.put("P9_cardNo", reqParam.getCardNo());
            params.put("P10_year", reqParam.getExpireDate().substring(2));
            params.put("P11_month", reqParam.getExpireDate().substring(0, 2));
            params.put("P12_cvv2", reqParam.getCvv());
            params.put("P13_phone", reqParam.getMobileNum());
            params.put("P14_currency", "CNY");
            params.put("P15_orderAmount", StringUtil.changeF2Y(reqParam.getOrderAmount()));
            params.put("P16_goodsName", "guoyinpay");
            params.put("P17_goodsDesc", "guoyinpay");
            params.put("P18_terminalType", "IMEI");
            params.put("P19_terminalId", reqParam.getGymchtId());
            params.put("P20_orderIp", reqParam.getRemoteAddr());
            params.put("P21_period", "1");
            params.put("P22_periodUnit", "Hour");
            params.put("P23_serverCallbackUrl", SysParamUtil.getParam("HELIBAO_QUICK_NOTIFY_URL"));
            params.put("sign", GuangdaUtil.getHelibaoMd5Sign(params, qrChannelInf.getSecret_key(), "UTF-8"));
            tradeInfo.setOut_trade_no(tradeNo);
            tradeInfo.setVersion("1.0.0");
            tradeInfo.setTime_start(timeStart);
            String keyValue = GuangdaUtil.map2HttpParam(params);
            String result = HttpUtility.postData(Environment.HELIBAO_QUICKPAY_URL, keyValue);
            Map jsonResult = (Map) JsonUtil.parseJson(result);
            if (jsonResult != null) {
                if ("0000".equals(jsonResult.get("rt2_retCode"))) {
                    tradeInfo.setTrade_state(TradeStateEnum.PRESUCCESS.getCode());
                    tradeInfo.setErr_code((String) jsonResult.get("rt2_retCode"));
                    tradeInfo.setResp_code("00");
                    LinkedHashMap<String, String> yzmParams = new LinkedHashMap<String, String>();
                    yzmParams.put("P1_bizType", "QuickPaySendValidateCode");
                    yzmParams.put("P2_customerNumber", qrChannelInf.getChannel_mcht_no());
                    yzmParams.put("P3_orderId", tradeNo);
                    yzmParams.put("P4_timestamp", timeStart);
                    yzmParams.put("P5_phone", reqParam.getMobileNum());
                    yzmParams.put("sign", GuangdaUtil.getHelibaoMd5Sign(yzmParams, qrChannelInf.getSecret_key(), "UTF-8"));
                    String yzmKeyValue = GuangdaUtil.map2HttpParam(yzmParams);
                    String yzmResult = HttpUtility.postData(Environment.HELIBAO_QUICKPAY_URL, yzmKeyValue);
                    Map yzmJsonResult = (Map) JsonUtil.parseJson(yzmResult);
                    if (yzmJsonResult != null) {
                        "0000".equals(yzmJsonResult.get("rt2_retCode"));
                    }
                    log.info((Object) ("\u5408\u5229\u5b9d\u5feb\u6377\u652f\u4ed8\u9884\u4ea4\u6613\u6210\u529f" + reqParam.getTradeSn()));
                } else {
                    tradeInfo.setTrade_state(TradeStateEnum.PREERROR.getCode());
                }
                tradeInfo.setResult_code((String) jsonResult.get("refCode"));
                tradeInfo.setMessage((String) jsonResult.get("refMsg"));
            } else {
                log.error((Object) ("\u5408\u5229\u5b9d\u5feb\u6377\u652f\u4ed8\u9884\u4ea4\u6613\u5931\u8d25:" + result));
                tradeInfo.setMessage(ResponseEnum.BACK_EXCEPTION.getMemo());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tradeInfo;
    }

    @Override
    public QuickpayBean doCheckpay(QuickpayBean quickpayBean, QrcodeChannelInf qrChannelInf, String yzm) {
        try {
            LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
            params.put("P1_bizType", "QuickPayConfirmPay");
            params.put("P2_customerNumber", qrChannelInf.getChannel_mcht_no());
            params.put("P3_orderId", quickpayBean.getOut_trade_no());
            params.put("P4_timestamp", DateUtil.getCurrTime());
            params.put("P5_validateCode", yzm);
            params.put("P6_orderIp", quickpayBean.getMch_create_ip());
            params.put("sign", GuangdaUtil.getHelibaoMd5Sign(params, qrChannelInf.getSecret_key(), "UTF-8"));
            String keyValue = GuangdaUtil.map2HttpParam(params);
            String result = HttpUtility.postData(Environment.HELIBAO_QUICKPAY_URL, keyValue);
            Map jsonResult = (Map) JsonUtil.parseJson(result);
            if (jsonResult != null) {
                quickpayBean.setTransaction_id(StringUtil.trans2Str(jsonResult.get("rt6_serialNumber")));
                quickpayBean.setTime_end(DateUtil.getCurrTime());
                if ("0000".equals(jsonResult.get("rt2_retCode")) && "SUCCESS".equals(jsonResult.get("rt9_orderStatus"))) {
                    quickpayBean.setTrade_state(TradeStateEnum.SUCCESS.getCode());
                    log.info((Object) ("\u5408\u5229\u5b9d\u5feb\u6377\u652f\u4ed8\u4ea4\u6613\u6210\u529f" + quickpayBean.getTradeSn()));
                } else {
                    quickpayBean.setTrade_state(TradeStateEnum.PAYERROR.getCode());
                }
                quickpayBean.setResult_code((String) jsonResult.get("rt2_retCode"));
                quickpayBean.setMessage((String) jsonResult.get("rt3_retMsg"));
            } else {
                log.error((Object) ("\u5408\u5229\u5b9d\u5feb\u6377\u652f\u4ed8\u5931\u8d25:" + result));
                quickpayBean.setMessage(ResponseEnum.BACK_EXCEPTION.getMemo());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return quickpayBean;
    }

    @Override
    public QuickpayBean doQuerypay(QuickpayBean quickpayBean, QrcodeChannelInf qrChannelInf) {
        try {
            LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
            params.put("P1_bizType", "QuickPayQuery");
            params.put("P2_orderId", quickpayBean.getOut_trade_no());
            params.put("P3_customerNumber", qrChannelInf.getChannel_mcht_no());
            params.put("sign", GuangdaUtil.getHelibaoMd5Sign(params, qrChannelInf.getSecret_key(), "UTF-8"));
            String keyValue = GuangdaUtil.map2HttpParam(params);
            String result = HttpUtility.postData(Environment.HELIBAO_QUICKPAY_URL, keyValue);
            Map jsonResult = (Map) JsonUtil.parseJson(result);
            if (jsonResult != null) {
                if ("0000".equals(jsonResult.get("rt2_retCode")) && "SUCCESS".equals(jsonResult.get("rt9_orderStatus"))) {
                    quickpayBean.setTrade_state(TradeStateEnum.SUCCESS.getCode());
                    quickpayBean.setTime_end(DateUtil.getCurrTime());
                    log.info((Object) ("\u5408\u5229\u5b9d\u5feb\u6377\u652f\u4ed8\u4ea4\u6613\u6210\u529f" + quickpayBean.getTradeSn()));
                } else if ("0000".equals(jsonResult.get("rt2_retCode")) && "FAILED".equals(jsonResult.get("rt9_orderStatus"))) {
                    quickpayBean.setTrade_state(TradeStateEnum.PAYERROR.getCode());
                }
                quickpayBean.setResult_code((String) jsonResult.get("rt2_retCode"));
                quickpayBean.setMessage((String) jsonResult.get("rt3_retMsg"));
            } else {
                log.error((Object) ("\u5408\u5229\u5b9d\u5feb\u6377\u652f\u4ed8\u5931\u8d25:" + result));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return quickpayBean;
    }

    @Override
    public String saveResultNotify(Map<String, String> resultMap) {
        QuickpayBean notifyBeanTemp = this.quickpayDao.getById(resultMap.get("rt5_orderId"));
        if (notifyBeanTemp == null) {
            return "notexist";
        }
        String sign = resultMap.get("sign");
        resultMap.remove("sign");
        QrcodeChannelInf qrcodeChannelInf = this.qrcodeMchtInfoDaoImpl.getChannelInf(notifyBeanTemp.getChannel_id(), notifyBeanTemp.getMch_id());
        String checkSign = GuangdaUtil.getHelibaoMd5Sign(resultMap, qrcodeChannelInf.getSecret_key(), "UTF-8").toLowerCase();
        if (!checkSign.equals(sign)) {
            return "sign error";
        }
        String notifyBack = "";
        if (!TradeStateEnum.SUCCESS.getCode().equals(notifyBeanTemp.getTrade_state())) {
            if ("SUCCESS".equals(resultMap.get("rt9_orderStatus"))) {
                notifyBeanTemp.setTrade_state(TradeStateEnum.SUCCESS.getCode());
                this.tradeMchtAccountService.notifyQucikpaySuccess(notifyBeanTemp);
            } else if ("FAIL".equals(resultMap.get("rt9_orderStatus"))) {
                notifyBeanTemp.setTrade_state(TradeStateEnum.PAYERROR.getCode());
            } else if ("CLOSE".equals(resultMap.get("rt9_orderStatus"))) {
                notifyBeanTemp.setTrade_state(TradeStateEnum.CLOSED.getCode());
            }
            notifyBeanTemp.setTransaction_id(resultMap.get("rt6_serialNumber"));
            notifyBeanTemp.setTime_end(DateUtil.getCurrTime());
        }
        if (notifyBeanTemp.getGy_notifyUrl() != null) {
            "success".equals(notifyBeanTemp.getGynotify_back());
        }
        this.quickpayDao.update(notifyBeanTemp);
        return "success";
    }

    @Override
    public Map<String, String> df(DfParam dfParam) {
        String dfTradeNo = UUIDGenerator.getOrderIdByUUId("DF");
        try {
            LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
            params.put("P1_bizType", "Transfer");
            params.put("P2_orderId", dfTradeNo);
            params.put("P3_customerNumber", "C1800001078");
            params.put("P4_amount", StringUtil.changeF2Y(dfParam.getReceiptAmount()));
            params.put("P5_bankCode", "CMBCHINA");
            params.put("P6_bankAccountNo", dfParam.getReceiptPan());
            params.put("P7_bankAccountName", dfParam.getReceiptName());
            params.put("P8_biz", "B2C");
            params.put("P9_bankUnionCode", "");
            params.put("P10_feeType", "PAYER");
            params.put("P11_urgency", "true");
            params.put("P12_summary", "\u7ed3\u7b97\u6b3e");
            params.put("sign", URLEncoder.encode(RSA.sign(GuangdaUtil.buildHelibaoData4Sign(params), RSA.getPrivateKey("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMIoTEfcbInnqJ9huom42gaPeEwYNSMZauqkvspmiFKWHOusIX6vE8KGJX2ivbqBWGGI481e8UvK86xvdHWJjSWh0nPh/InBXD+uJBH7FHEsRyUlF69L8NoKG6PdfR5MiAzTANzCO/rMZORq5RkqUQU5wrYIqkOJnqu58+h0R2RVAgMBAAECgYBCO+cR2LM6NEoE3Rz6oJHYDRd3tgZii+g6Vas0dtfofIcvKpBenDE8efDONDdPKZLR6xXzWIEuXvTTjU4ITr1UULvS52XYVQm+eIwP51r3bQ85l8dRHfnMA2h9DNrmtyc3/QhoJZGAZbOdHj9MtU6B62cIoIKKiujNUwxi6A4BoQJBAP5A6zcWA2BsNxJxhEQY852UlouRVg8QSjNHorJlLGZRErW9jHmYHRfFaxM8eOBPK8sKEyCoiVOW9ZyXNLDQQTkCQQDDfbSliHYzS3u9e2EwtAeyUvu2REe0IrwX3I2JZp15RmNI5X9G8CCKRGjpl1kGfgcqw0Maz5ik8pr3lw4DAWf9AkEAm4zeGsqF8FTkcI0wjXTyfmLso6CcFQUzUCIwgJHzUvmgx72alr2gdu2Z75/cl+b39PVJ9H4H3jMKOu4rF02hyQJAYRpKpcKPu9DtqOPlFdUG0avofdOSrkZmmY+i+jHvXHL7FBVy0CCseSGb3xOfU9s74iDVbzbYwm4DTaJJgnKrXQJAYZhn3Ea4TUu7oRRp3T4eQ/Q/hpdvFnCbQbYMGEzlF4B/Sm1AJqdKozKZh3rC1JEB0jD8Lj0CmxMpPWH1O6diuQ==")), "UTF-8"));
            String keyValue = GuangdaUtil.map2HttpParam(params);
            log.info((Object) ("daifu_params:" + keyValue));
            String result = HttpUtility.postData(Environment.HELIBAO_DAIFU_URL, keyValue);
            Map returnMap = (Map) JsonUtil.parseJson(result);
            String resultCode = StringUtil.trans2Str(returnMap.get("rt2_retCode"));
            String resultMsg = StringUtil.trans2Str(returnMap.get("rt3_retMsg"));
            if ("0000".equals(resultCode)) {
                System.out.println("\u5408\u5229\u5b9d\u4ee3\u4ed8\u8ba2\u5355\u53f7\uff1a" + (String) returnMap.get("rt6_serialNumber") + "---" + resultCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, String> dfQuery(DfParam dfParam) {
        try {
            LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
            params.put("P1_bizType", "TransferQuery");
            params.put("P2_orderId", dfParam.getDfTransactionId());
            params.put("P3_customerNumber", "C1800001078");
            params.put("sign", URLEncoder.encode(RSA.sign(GuangdaUtil.buildHelibaoData4Sign(params), RSA.getPrivateKey("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMIoTEfcbInnqJ9huom42gaPeEwYNSMZauqkvspmiFKWHOusIX6vE8KGJX2ivbqBWGGI481e8UvK86xvdHWJjSWh0nPh/InBXD+uJBH7FHEsRyUlF69L8NoKG6PdfR5MiAzTANzCO/rMZORq5RkqUQU5wrYIqkOJnqu58+h0R2RVAgMBAAECgYBCO+cR2LM6NEoE3Rz6oJHYDRd3tgZii+g6Vas0dtfofIcvKpBenDE8efDONDdPKZLR6xXzWIEuXvTTjU4ITr1UULvS52XYVQm+eIwP51r3bQ85l8dRHfnMA2h9DNrmtyc3/QhoJZGAZbOdHj9MtU6B62cIoIKKiujNUwxi6A4BoQJBAP5A6zcWA2BsNxJxhEQY852UlouRVg8QSjNHorJlLGZRErW9jHmYHRfFaxM8eOBPK8sKEyCoiVOW9ZyXNLDQQTkCQQDDfbSliHYzS3u9e2EwtAeyUvu2REe0IrwX3I2JZp15RmNI5X9G8CCKRGjpl1kGfgcqw0Maz5ik8pr3lw4DAWf9AkEAm4zeGsqF8FTkcI0wjXTyfmLso6CcFQUzUCIwgJHzUvmgx72alr2gdu2Z75/cl+b39PVJ9H4H3jMKOu4rF02hyQJAYRpKpcKPu9DtqOPlFdUG0avofdOSrkZmmY+i+jHvXHL7FBVy0CCseSGb3xOfU9s74iDVbzbYwm4DTaJJgnKrXQJAYZhn3Ea4TUu7oRRp3T4eQ/Q/hpdvFnCbQbYMGEzlF4B/Sm1AJqdKozKZh3rC1JEB0jD8Lj0CmxMpPWH1O6diuQ==")), "UTF-8"));
            String keyValue = GuangdaUtil.map2HttpParam(params);
            log.info((Object) ("daifu_params:" + keyValue));
            String result = HttpUtility.postData(Environment.HELIBAO_DAIFU_URL, keyValue);
            Map returnMap = (Map) JsonUtil.parseJson(result);
            String resultCode = StringUtil.trans2Str(returnMap.get("rt2_retCode"));
            String resultMsg = StringUtil.trans2Str(returnMap.get("rt3_retMsg"));
            if ("0000".equals(resultCode)) {
                System.out.println("\u5408\u5229\u5b9d\u4ee3\u4ed8\u8ba2\u5355\u53f7\uff1a" + (String) returnMap.get("rt6_serialNumber") + "---" + resultCode);
                if ("SUCCESS".equals(returnMap.get("rt7_orderStatus"))) {
                    System.out.println("\u4ee3\u4ed8\u6210\u529f\uff1a" + (String) returnMap.get("rt6_serialNumber") + "---" + (String) returnMap.get("rt7_orderStatus"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        DfParam dfParam = new DfParam();
        dfParam.setReceiptAmount(5000000);
        dfParam.setReceiptName("\u5de6\u677e");
        dfParam.setReceiptPan("6214856556237018");
        new HelibaoQuickServiceImpl().df(dfParam);
    }
}
