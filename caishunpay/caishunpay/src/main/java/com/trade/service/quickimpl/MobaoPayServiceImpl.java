/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  com.kspay.AESUtil
 *  com.kspay.MD5Util
 *  org.apache.log4j.Logger
 *  org.springframework.stereotype.Service
 */
package com.trade.service.quickimpl;

import com.gy.system.Environment;
import com.gy.util.DateUtil;
import com.gy.util.HttpUtility;
import com.gy.util.StringUtil;
import com.gy.util.UUIDGenerator;
import com.kspay.AESUtil;
import com.trade.bean.BankCardPay;
import com.trade.bean.own.BankCardPayRequest;
import com.trade.bean.own.PayChannelInf;
import com.trade.enums.ResponseEnum;
import com.trade.enums.TradeStateEnum;
import com.trade.util.MD5Util;
import com.trade.util.JsonUtil;

import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class MobaoPayServiceImpl
extends QuickpayServiceImpl {
    private static Logger log = Logger.getLogger(MobaoPayServiceImpl.class);

    @Override
    public BankCardPay doPrepay(BankCardPayRequest reqParam, PayChannelInf qrChannelInf) {
        BankCardPay tradeInfo = new BankCardPay();
        String tradeNo = UUIDGenerator.getOrderIdByUUId("Qk");
        String timeStart = DateUtil.getCurrTime();
        try {
            LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
            params.put("versionId", "001");
            params.put("businessType", "1401");
            params.put("insCode", "");
            params.put("merId", qrChannelInf.getChannel_mcht_no());
            params.put("orderId", tradeNo);
            params.put("transDate", timeStart);
            params.put("transAmount", StringUtil.changeF2Y(reqParam.getOrderAmount()));
            params.put("transCurrency", "156");
            params.put("cardByName", com.kspay.MD5Util.encode((byte[])reqParam.getCardHolderName().getBytes("UTF-8")));
            params.put("cardByNo", reqParam.getCardNo());
            params.put("cardType", reqParam.getCardType());
            String mobaoExpire = "";
            if (StringUtil.isNotEmpty(reqParam.getExpireDate())) {
                mobaoExpire = String.valueOf(reqParam.getExpireDate().substring(2)) + reqParam.getExpireDate().substring(0, 2);
            }
            params.put("expireDate", mobaoExpire);
            params.put("CVV", reqParam.getCvv());
            params.put("bankCode", reqParam.getBankCode());
            params.put("openBankName", reqParam.getBankName());
            params.put("cerType", reqParam.getCerType());
            params.put("cerNumber", reqParam.getCerNumber());
            params.put("mobile", reqParam.getMobileNum());
            params.put("isAcceptYzm", "00");
            params.put("pageNotifyUrl", "");
            params.put("backNotifyUrl", "");
            params.put("orderDesc", "");
            params.put("instalTransFlag", "01");
            params.put("instalTransNums", "");
            params.put("dev", "");
            params.put("fee", "");
            tradeInfo.setOut_trade_no(tradeNo);
            tradeInfo.setVersion((String)params.get("versionId"));
            tradeInfo.setService("mobao");
            tradeInfo.setTime_start(timeStart);
            String keyValue = MD5Util.map2HttpParam(params);
            keyValue = String.valueOf(keyValue) + qrChannelInf.getSecret_key();
            String signValue = "";
            signValue = com.kspay.MD5Util.MD5Encode((String)keyValue);
            params.put("signType", "MD5");
            params.put("signData", signValue);
            String sendMessage = this.buildSendMessage(params, qrChannelInf);
            log.info((Object)("\u62fc\u63a5:" + sendMessage));
            String result = HttpUtility.postData(Environment.QUICKPAY_MOBAO, sendMessage);
            Map jsonResult = (Map)JsonUtil.parseJson(result);
            if (jsonResult != null) {
                if ("00".equals(jsonResult.get("status")) && "01".equals(jsonResult.get("refCode"))) {
                    tradeInfo.setTrade_state(TradeStateEnum.PRESUCCESS.getCode());
                    tradeInfo.setErr_code((String)jsonResult.get("chanlRefCode"));
                    tradeInfo.setTransaction_id((String)jsonResult.get("ksPayOrderId"));
                    tradeInfo.setOut_transaction_id((String)jsonResult.get("bankOrderId"));
                    tradeInfo.setYzm((String)jsonResult.get("yzm"));
                    tradeInfo.setResp_code("00");
                    log.info((Object)("\u6469\u5b9d\u5feb\u6377\u652f\u4ed8\u9884\u4ea4\u6613\u6210\u529f" + reqParam.getTradeSn()));
                } else {
                    tradeInfo.setTrade_state(TradeStateEnum.PREERROR.getCode());
                }
                tradeInfo.setResult_code((String)jsonResult.get("refCode"));
                tradeInfo.setMessage((String)jsonResult.get("refMsg"));
            } else {
                log.error((Object)("\u6469\u5b9d\u5feb\u6377\u652f\u4ed8\u9884\u4ea4\u6613\u5931\u8d25:" + result));
                tradeInfo.setMessage(ResponseEnum.BACK_EXCEPTION.getMemo());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return tradeInfo;
    }

    @Override
    public BankCardPay doCheckpay(BankCardPay BankCardPay, PayChannelInf qrChannelInf, String yzm) {
        try {
            LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
            params.put("versionId", "001");
            params.put("businessType", "1411");
            params.put("insCode", "");
            params.put("merId", BankCardPay.getMch_id());
            params.put("yzm", yzm);
            params.put("ksPayOrderId", BankCardPay.getTransaction_id());
            String keyValue = MD5Util.map2HttpParam(params);
            keyValue = String.valueOf(keyValue) + qrChannelInf.getSecret_key();
            String signValue = "";
            signValue = com.kspay.MD5Util.MD5Encode((String)keyValue);
            params.put("signType", "MD5");
            params.put("signData", signValue);
            String sendMessage = this.buildSendMessage(params, qrChannelInf);
            log.info((Object)("\u62fc\u63a5:" + sendMessage));
            String result = HttpUtility.postData(Environment.QUICKPAY_MOBAO, sendMessage);
            Map jsonResult = (Map)JsonUtil.parseJson(result);
            if (jsonResult != null) {
                if ("00".equals(jsonResult.get("status")) && "00".equals(jsonResult.get("refCode"))) {
                    BankCardPay.setTrade_state(TradeStateEnum.SUCCESS.getCode());
                    BankCardPay.setTime_end(DateUtil.getCurrTime());
                    log.info((Object)("\u6469\u5b9d\u5feb\u6377\u652f\u4ed8\u4ea4\u6613\u6210\u529f" + BankCardPay.getTradeSn()));
                } else if ("00".equals(jsonResult.get("status")) && "02".equals(jsonResult.get("refCode"))) {
                    BankCardPay.setTrade_state(TradeStateEnum.PAYERROR.getCode());
                }
                BankCardPay.setResult_code((String)jsonResult.get("refCode"));
                BankCardPay.setMessage((String)jsonResult.get("refMsg"));
            } else {
                log.error((Object)("\u6469\u5b9d\u5feb\u6377\u652f\u4ed8\u5931\u8d25:" + result));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return BankCardPay;
    }

    @Override
    public BankCardPay doQuerypay(BankCardPay BankCardPay, PayChannelInf qrChannelInf) {
        try {
            LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
            params.put("versionId", "001");
            params.put("businessType", "1421");
            params.put("insCode", "");
            params.put("merId", BankCardPay.getMch_id());
            params.put("orderId", BankCardPay.getOut_trade_no());
            params.put("transDate", BankCardPay.getTime_start());
            String keyValue = MD5Util.map2HttpParam(params);
            keyValue = String.valueOf(keyValue) + qrChannelInf.getSecret_key();
            String signValue = "";
            signValue = com.kspay.MD5Util.MD5Encode((String)keyValue);
            params.put("signType", "MD5");
            params.put("signData", signValue);
            String sendMessage = this.buildSendMessage(params, qrChannelInf);
            log.info((Object)("\u62fc\u63a5:" + sendMessage));
            String result = HttpUtility.postData(Environment.QUICKPAY_MOBAO, sendMessage);
            Map jsonResult = (Map)JsonUtil.parseJson(result);
            if (jsonResult != null) {
                if ("00".equals(jsonResult.get("status")) && "00".equals(jsonResult.get("refCode"))) {
                    BankCardPay.setTrade_state(TradeStateEnum.SUCCESS.getCode());
                    BankCardPay.setTime_end(DateUtil.getCurrTime());
                    log.info((Object)("\u6469\u5b9d\u5feb\u6377\u652f\u4ed8\u4ea4\u6613\u6210\u529f" + BankCardPay.getTradeSn()));
                } else if ("00".equals(jsonResult.get("status")) && "02".equals(jsonResult.get("refCode"))) {
                    BankCardPay.setTrade_state(TradeStateEnum.PAYERROR.getCode());
                }
                BankCardPay.setResult_code((String)jsonResult.get("refCode"));
                BankCardPay.setMessage((String)jsonResult.get("refMsg"));
            } else {
                log.error((Object)("\u6469\u5b9d\u5feb\u6377\u652f\u4ed8\u5931\u8d25:" + result));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return BankCardPay;
    }

    @Override
    public String saveResultNotify(Map<String, String> resultMap) {
        return null;
    }

    private String buildSendMessage(LinkedHashMap<String, String> params, PayChannelInf qrChannelInf) {
        String keyValue = MD5Util.map2HttpParam(params);
        String transData = AESUtil.encrypt((String)keyValue, (String)qrChannelInf.getSecret_key());
        return "merId=" + qrChannelInf.getChannel_mcht_no() + "&transData=" + transData;
    }

    public static void main(String[] args) {
        String data = "versionId=001&businessType=1401&insCode=&merId=818310048160000&orderId=Qk2017042000001243847205&transDate=20170420150647&transAmount=0.01&transCurrency=156&cardByName=5Y+25bu65paH&cardByNo=6225768722687439&cardType=00&expireDate=2108&CVV=026&bankCode=null&openBankName=\u62db\u5546\u94f6\u884c&cerType=01&cerNumber=362324199107253051&mobile=15217928112&isAcceptYzm=00&pageNotifyUrl=&backNotifyUrl=&orderDesc=&instalTransFlag=01&instalTransNums=&dev=&fee=&signType=MD5&signData=D71475ABB871E82518305A2EE50A9DE7";
        String secretData = AESUtil.encrypt((String)data, (String)"1FDD2547FA4FB61F");
        System.out.println(secretData);
        String signData = "versionId=001&businessType=1401&insCode=&merId=818310048160000&orderId=Qk2017042000000044646636&transDate=20170420142501&transAmount=0.01&transCurrency=156&cardByName=5Y+25bu65paH&cardByNo=6225768722687439&cardType=00&expireDate=2108&CVV=026&bankCode=null&openBankName=\u62db\u5546\u94f6\u884c&cerType=01&cerNumber=362324199107253051&mobile=15217928112&isAcceptYzm=00&pageNotifyUrl=&backNotifyUrl=&orderDesc=&instalTransFlag=01&instalTransNums=&dev=&fee=";
        String sign = com.kspay.MD5Util.MD5Encode((String)(String.valueOf(signData) + "1FDD2547FA4FB61F"));
        System.out.println("sign" + sign);
        String minwen = AESUtil.decrypt((String)"6B8129F821B53B04AC226782D30B169C4A5840FE1926626E4422D2B00E3DB752105DE49E6CCC791F5A51B82B0B7B5FAA6311B447B475E496CDD8AF062DA030A5109976AE9AFCB4D8294A5780F4984F41739341676254F16358D06470E93D4662F3214ED8F4E4AAF6C5DED756A0453613477737EE68FB16989B1F1F8802D08197E29B06F038293A51907D8CA59922D3A93A424FEA79E5E4BA7BAE2D67754C8A31130FBCC0B45AF28789574252F2497A6B28BFAC1709BE2BA5987A8099A5744D40BEF13BB831C5CC7AA4C37D5E85AA29E4B1103B16BF5DAAD0B1BCB1D54F21412B11CCC0F2FB5CDC1A36B7BBF56E31BF96E215BE4D522537F22DF65617AEE9ED0409DC3DD4B520758E16ACF395FF0A67C9DB193281C21040ACFB662B481184501AC3B4499E34EE391B61EFCD6E748BADADAD8530F109471E11311C11241AC3E75D7562618D57F2BCE8D3168C5A6E4B7F323324481BFFE9F19BABDB48397D12AA8D339D5E3E66821434608173DA874E612DE30E0EB739582E39F3B3C5AB4EED03227463E20B81B172AB3210B2A5D5A6669FBF5E0D959597054FBA71B3244C54E0E6B71A756D9772F50A35298A51A3E2035E94B37D3EDE19495F0709E950683AF7C99AEF60B20D231468B18177E14C9E5317026300E3269E0E9236C5B4486FC3D7D5BA9442E45F8031FD4B750E602D3C743BAA1551AA7A2FB5A64BE25A85284F38B8", (String)"1FDD2547FA4FB61F");
        System.out.println("\u660e\u6587:" + minwen);
    }
}
