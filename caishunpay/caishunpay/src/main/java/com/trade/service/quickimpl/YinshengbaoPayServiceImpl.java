/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.log4j.Logger
 *  org.springframework.stereotype.Service
 */
package com.trade.service.quickimpl;

import com.gy.system.Environment;
import com.gy.system.SysParamUtil;
import com.gy.util.AppHttp;
import com.gy.util.DateUtil;
import com.gy.util.MD5Utils;
import com.gy.util.UUIDGenerator;
import com.trade.bean.BankCardPay;
import com.trade.bean.own.BankCardPayRequest;
import com.trade.bean.own.PayChannelInf;
import com.trade.bean.own.MerchantInf;
import com.trade.enums.ResponseEnum;
import com.trade.enums.TradeSource;
import com.trade.enums.TradeStateEnum;
import com.trade.util.JsonUtil;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class YinshengbaoPayServiceImpl
extends QuickpayServiceImpl {
    private static Map<String, String> xieyiMap = new HashMap<String, String>();
    private static Logger log = Logger.getLogger(YinshengbaoPayServiceImpl.class);

    private static String getXieyi(String card) {
        if (!DateUtil.getCurrentDay().equals(xieyiMap.get("workDay"))) {
            xieyiMap = new HashMap<String, String>();
            xieyiMap.put("workDay", DateUtil.getCurrentDay());
        }
        return xieyiMap.get(card);
    }

    private static void setXieyi(String card, String xieyi) {
        xieyiMap.put(card, xieyi);
    }

    @Override
    public BankCardPay doPrepay(BankCardPayRequest reqParam, PayChannelInf qrChannelInf) {
        BankCardPay tradeInfo = new BankCardPay();
        String orderId = UUIDGenerator.getOrderIdByUUId("DK");
        tradeInfo.setTransaction_id(orderId);
        tradeInfo.setOut_trade_no(orderId);
        tradeInfo.setVersion("1.0.0");
        tradeInfo.setTrade_source(TradeSource.DAIKOU.getCode());
        String timeStart = DateUtil.getCurrTime();
        tradeInfo.setTime_start(timeStart);
        String cardNo = reqParam.getCardNo();
        String name = reqParam.getCardHolderName();
        String idCardNo = reqParam.getCerNumber();
        String phoneNo = reqParam.getMobileNum();
        String id = String.valueOf(qrChannelInf.getChannel_mcht_no()) + cardNo + name + idCardNo + phoneNo;
        String xieyi = YinshengbaoPayServiceImpl.getXieyi(id);
        if (xieyi != null && !xieyi.isEmpty()) {
            tradeInfo.setTrade_state(TradeStateEnum.PRESUCCESS.getCode());
            tradeInfo.setErr_code("0000");
            tradeInfo.setYzm(xieyi);
            tradeInfo.setResp_code("00");
            log.info((Object)("\u94f6\u751f\u5b9d\u8bf7\u6c42\u5b50\u534f\u8bae\u5f55\u5165\u6210\u529f" + reqParam.getTradeSn()));
            tradeInfo.setResult_code("0000");
            tradeInfo.setMessage("\u7b7e\u8ba2\u6210\u529f");
            return tradeInfo;
        }
        int afterDay = 31;
        String startDate = DateUtil.getCurrentDay();
        String endDate = DateUtil.getDateBeforeDays(- afterDay - 1, "yyyyMMdd");
        LinkedHashMap<String, String> obj = new LinkedHashMap<String, String>();
        obj.put("accountId", qrChannelInf.getChannel_mcht_no());
        obj.put("contractId", qrChannelInf.getChannel_mcht_no());
        obj.put("name", name);
        obj.put("phoneNo", phoneNo);
        obj.put("cardNo", cardNo);
        obj.put("idCardNo", idCardNo);
        obj.put("startDate", startDate);
        obj.put("endDate", endDate);
        obj.put("cycle", "1");
        obj.put("triesLimit", "1");
        String macString = String.valueOf(MD5Utils.getSignParamNoSort(obj)) + "&key=" + qrChannelInf.getSecret_key();
        System.out.println("\u8ba1\u7b97\u7b7e\u540d\u4e32 :" + macString);
        try {
            String mac = MD5Utils.md5(macString).toUpperCase();
            System.out.println("md5\u7b7e\u540d : " + mac);
            obj.put("mac", mac);
            System.out.println("\u4e0a\u9001\u6570\u636e : " + obj);
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = AppHttp.appadd("http://" + Environment.YINSHENGBAO_PAY_URL.getBaseUrl() + "/unspay-external/subcontract/signSimpleSubContract", MD5Utils.getSignParamNoSort(obj));
        System.out.println("result:" + result);
        Map jsonResult = (Map)JsonUtil.parseJson(result);
        if (jsonResult != null) {
            if ("0000".equals(jsonResult.get("result_code")) || "2001".equals(jsonResult.get("result_code"))) {
                tradeInfo.setTrade_state(TradeStateEnum.PRESUCCESS.getCode());
                tradeInfo.setErr_code((String)jsonResult.get("result_code"));
                String xy = (String)jsonResult.get("subContractId");
                tradeInfo.setYzm((String)jsonResult.get("subContractId"));
                if (xy != null && !xy.isEmpty()) {
                    YinshengbaoPayServiceImpl.setXieyi(id, xy);
                }
                tradeInfo.setResp_code("00");
                log.info((Object)("\u94f6\u751f\u5b9d\u8bf7\u6c42\u5b50\u534f\u8bae\u5f55\u5165\u6210\u529f" + reqParam.getTradeSn()));
            } else {
                tradeInfo.setTrade_state(TradeStateEnum.PREERROR.getCode());
            }
            tradeInfo.setResult_code((String)jsonResult.get("result_code"));
            tradeInfo.setMessage((String)jsonResult.get("result_msg"));
        } else {
            log.error((Object)("\u94f6\u751f\u5b9d\u8bf7\u6c42\u5b50\u534f\u8bae\u5f55\u5165\u5931\u8d25:" + result));
            tradeInfo.setMessage(ResponseEnum.BACK_EXCEPTION.getMemo());
        }
        return tradeInfo;
    }

    @Override
    public BankCardPay doCheckpay(BankCardPay BankCardPay, PayChannelInf qrChannelInf, String yzm) {
        String purpose = "\u673a\u7968";
        BigDecimal money = new BigDecimal(BankCardPay.getTotal_fee());
        String amount = money.movePointLeft(2).toString();
        String phoneNo = BankCardPay.getMobileNum();
        String subContractId = BankCardPay.getYzm();
        String orderId = BankCardPay.getOut_trade_no();
        String responseUrl = SysParamUtil.getParam("YINSHENGBAO_NOTIFY_URL");
        LinkedHashMap<String, String> obj = new LinkedHashMap<String, String>();
        obj.put("accountId", qrChannelInf.getChannel_mcht_no());
        obj.put("subContractId", subContractId);
        obj.put("orderId", orderId);
        obj.put("purpose", purpose);
        obj.put("amount", amount);
        obj.put("phoneNo", phoneNo);
        obj.put("responseUrl", responseUrl);
        String macString = String.valueOf(MD5Utils.getSignParamNoSort(obj)) + "&key=" + qrChannelInf.getSecret_key();
        System.out.println("\u8ba1\u7b97\u7b7e\u540d\u4e32 :" + macString);
        try {
            String mac = MD5Utils.md5(macString).toUpperCase();
            System.out.println("md5\u7b7e\u540d : " + mac);
            obj.put("mac", mac);
            System.out.println("\u4e0a\u9001\u6570\u636e : " + obj);
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        boolean needCheck = true;
        MerchantInf qrCodeMchtInfo = this.merchantInfDaoImpl.getMchtInfo(BankCardPay.getMerchantId());
        boolean bl = needCheck = !"0".equals(qrCodeMchtInfo.getDaikou_need_check());
        if (needCheck) {
            String sendPackge = MD5Utils.getSignParamNoSort(obj);
            BankCardPay.setSend_packge(sendPackge);
            BankCardPay.setTrade_state(TradeStateEnum.CHECKING.getCode());
            BankCardPay.setTime_end(DateUtil.getCurrTime());
            BankCardPay.setResult_code(ResponseEnum.SUCCESS.getCode());
            BankCardPay.setMessage(ResponseEnum.SUCCESS.getMemo());
        } else {
            String result = AppHttp.appadd("http://" + Environment.YINSHENGBAO_PAY_URL.getBaseUrl() + "/unspay-external/delegateCollect/collect", MD5Utils.getSignParamNoSort(obj));
            System.out.println("result:" + result);
            Map jsonResult = (Map)JsonUtil.parseJson(result);
            if (jsonResult != null) {
                if ("0000".equals(jsonResult.get("result_code"))) {
                    BankCardPay.setTrade_state(TradeStateEnum.SUCCESS.getCode());
                    BankCardPay.setTime_end(DateUtil.getCurrTime());
                    log.info((Object)("\u94f6\u751f\u5b9d\u59d4\u6258\u4ee3\u6263\u4ea4\u6613\u6210\u529f" + BankCardPay.getTradeSn()));
                } else {
                    BankCardPay.setTrade_state(TradeStateEnum.PAYERROR.getCode());
                }
                BankCardPay.setResult_code((String)jsonResult.get("result_code"));
                BankCardPay.setMessage((String)jsonResult.get("result_msg"));
            } else {
                log.error((Object)("\u94f6\u751f\u5b9d\u59d4\u6258\u4ee3\u6263\u5931\u8d25:" + result));
                BankCardPay.setMessage(ResponseEnum.BACK_EXCEPTION.getCode());
                BankCardPay.setMessage(ResponseEnum.BACK_EXCEPTION.getMemo());
            }
        }
        return BankCardPay;
    }

    @Override
    public BankCardPay doQuerypay(BankCardPay BankCardPay, PayChannelInf qrChannelInf) {
        String orderId = BankCardPay.getTransaction_id();
        LinkedHashMap<String, String> obj = new LinkedHashMap<String, String>();
        obj.put("accountId", qrChannelInf.getChannel_mcht_no());
        obj.put("orderId", orderId);
        String macString = String.valueOf(MD5Utils.getSignParamNoSort(obj)) + "&key=" + qrChannelInf.getSecret_key();
        System.out.println("\u8ba1\u7b97\u7b7e\u540d\u4e32 :" + macString);
        try {
            String mac = MD5Utils.md5(macString).toUpperCase();
            System.out.println("md5\u7b7e\u540d : " + mac);
            obj.put("mac", mac);
            System.out.println("\u4e0a\u9001\u6570\u636e : " + obj);
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = AppHttp.appadd("http://" + Environment.YINSHENGBAO_PAY_URL.getBaseUrl() + "/unspay-external/delegateCollect/queryOrderStatus", MD5Utils.getSignParamNoSort(obj));
        System.out.println("result:" + result);
        Map jsonResult = (Map)JsonUtil.parseJson(result);
        if (jsonResult != null && "0000".equals(jsonResult.get("result_code"))) {
            if ("00".equals(jsonResult.get("status"))) {
                BankCardPay.setTrade_state(TradeStateEnum.SUCCESS.getCode());
                BankCardPay.setTime_end(DateUtil.getCurrTime());
                log.info((Object)("\u94f6\u751f\u5b9d\u59d4\u6258\u4ee3\u6263\u4ea4\u6613\u6210\u529f" + BankCardPay.getTradeSn()));
            } else {
                BankCardPay.setTrade_state(TradeStateEnum.PAYERROR.getCode());
            }
            BankCardPay.setResult_code((String)jsonResult.get("status"));
            BankCardPay.setMessage((String)jsonResult.get("desc"));
        } else {
            log.error((Object)("\u94f6\u751f\u5b9d\u59d4\u6258\u4ee3\u6263\u5931\u8d25:" + result));
        }
        return BankCardPay;
    }

    @Override
    public String saveResultNotify(Map<String, String> resultMap) {
        BankCardPay notifyBeanTemp = this.bankCardPayDao.getById(resultMap.get("orderId"));
        if (notifyBeanTemp == null) {
            return "notexist";
        }
        String sign = resultMap.get("mac");
        PayChannelInf payChannelInf = this.merchantInfDaoImpl.getChannelInf(notifyBeanTemp.getChannel_id(), notifyBeanTemp.getMch_id());
        LinkedHashMap<String, String> obj = new LinkedHashMap<String, String>();
        obj.put("accountId", payChannelInf.getChannel_mcht_no());
        obj.put("orderId", resultMap.get("orderId"));
        obj.put("amount", resultMap.get("amount"));
        obj.put("result_code", resultMap.get("result_code"));
        obj.put("result_msg", resultMap.get("result_msg"));
        String checkSign = String.valueOf(MD5Utils.getSignParamNoSort(obj)) + "&key=" + payChannelInf.getSecret_key();
        try {
            checkSign = MD5Utils.md5(checkSign).toUpperCase();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (!checkSign.equals(sign)) {
            return "sign error";
        }
        String notifyBack = "";
        if (!TradeStateEnum.SUCCESS.getCode().equals(notifyBeanTemp.getTrade_state())) {
            if ("0000".equals(resultMap.get("result_code"))) {
                notifyBeanTemp.setTrade_state(TradeStateEnum.SUCCESS.getCode());
                this.tradeMchtAccountService.notifyQucikpaySuccess(notifyBeanTemp);
            } else {
                notifyBeanTemp.setTrade_state(TradeStateEnum.PAYERROR.getCode());
            }
            notifyBeanTemp.setResult_code(resultMap.get("result_code"));
            notifyBeanTemp.setMessage(resultMap.get("result_msg"));
            notifyBeanTemp.setTransaction_id(resultMap.get("orderId"));
            notifyBeanTemp.setTime_end(DateUtil.getCurrTime());
        }
        if (notifyBeanTemp.getGy_notifyUrl() != null) {
            "success".equals(notifyBeanTemp.getGynotify_back());
        }
        this.bankCardPayDao.update(notifyBeanTemp);
        return "success";
    }

    public static void doCheckpay() {
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
    }
}
