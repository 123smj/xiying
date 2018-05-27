/*
 * Decompiled with CFR 0_124.
 */
package com.trade.bean;

import com.trade.bean.WxNativeParam;

import java.io.PrintStream;

public class WxpayScanCode
        extends WxNativeParam {
    private String status;
    private String message;
    private String result_code;
    private String err_code;
    private String err_msg;
    private String code_url;
    private String code_img_url;
    private String gymchtId;
    private String channel_id;
    private String gy_notifyUrl;
    private String tradeSn;
    private String trade_state;
    private String openid;
    private String trade_type;
    private String is_subscribe;
    private String pay_result;
    private String pay_info;
    private String transaction_id;
    private String out_transaction_id;
    private String sub_is_subscribe;
    private String sub_appid;
    private String sub_openid;
    private Integer coupon_fee;
    private String fee_type;
    private String bank_type;
    private String bank_billno;
    private String time_end;
    private String t0Flag;
    private String trade_source;
    private String appid;
    private String token_id;
    private String is_raw;
    private String callback_url;
    private String limit_credit_pay;
    private String gynotify_back;
    private String resp_code;
    private String is_new_trade;
    private Integer notify_num;
    private String notify_data;
    private String nofity_time;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResult_code() {
        return this.result_code;
    }

    public void setResult_code(String resultCode) {
        this.result_code = resultCode;
    }

    public String getErr_code() {
        return this.err_code;
    }

    public void setErr_code(String errCode) {
        this.err_code = errCode;
    }

    public String getErr_msg() {
        return this.err_msg;
    }

    public void setErr_msg(String errMsg) {
        this.err_msg = errMsg;
    }

    public String getCode_url() {
        return this.code_url;
    }

    public void setCode_url(String codeUrl) {
        this.code_url = codeUrl;
    }

    public String getCode_img_url() {
        return this.code_img_url;
    }

    public void setCode_img_url(String codeImgUrl) {
        this.code_img_url = codeImgUrl;
    }

    public static void main(String[] args) {
        System.out.println(new WxpayScanCode().toString());
    }

    public String getGymchtId() {
        return this.gymchtId;
    }

    public void setGymchtId(String gymchtId) {
        this.gymchtId = gymchtId;
    }

    public String getChannel_id() {
        return this.channel_id;
    }

    public void setChannel_id(String channelId) {
        this.channel_id = channelId;
    }

    public String getGy_notifyUrl() {
        return this.gy_notifyUrl;
    }

    public void setGy_notifyUrl(String gyNotifyUrl) {
        this.gy_notifyUrl = gyNotifyUrl;
    }

    public String getTradeSn() {
        return this.tradeSn;
    }

    public void setTradeSn(String tradeSn) {
        this.tradeSn = tradeSn;
    }

    public String getTrade_state() {
        return this.trade_state;
    }

    public void setTrade_state(String tradeState) {
        this.trade_state = tradeState;
    }

    public String getOpenid() {
        return this.openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getTrade_type() {
        return this.trade_type;
    }

    public void setTrade_type(String tradeType) {
        this.trade_type = tradeType;
    }

    public String getIs_subscribe() {
        return this.is_subscribe;
    }

    public void setIs_subscribe(String isSubscribe) {
        this.is_subscribe = isSubscribe;
    }

    public String getPay_result() {
        return this.pay_result;
    }

    public void setPay_result(String payResult) {
        this.pay_result = payResult;
    }

    public String getPay_info() {
        return this.pay_info;
    }

    public void setPay_info(String payInfo) {
        this.pay_info = payInfo;
    }

    public String getTransaction_id() {
        return this.transaction_id;
    }

    public void setTransaction_id(String transactionId) {
        this.transaction_id = transactionId;
    }

    public String getOut_transaction_id() {
        return this.out_transaction_id;
    }

    public void setOut_transaction_id(String outTransactionId) {
        this.out_transaction_id = outTransactionId;
    }

    public String getSub_is_subscribe() {
        return this.sub_is_subscribe;
    }

    public void setSub_is_subscribe(String subIsSubscribe) {
        this.sub_is_subscribe = subIsSubscribe;
    }

    public String getSub_appid() {
        return this.sub_appid;
    }

    public void setSub_appid(String subAppid) {
        this.sub_appid = subAppid;
    }

    public String getSub_openid() {
        return this.sub_openid;
    }

    public void setSub_openid(String subOpenid) {
        this.sub_openid = subOpenid;
    }

    public Integer getCoupon_fee() {
        return this.coupon_fee;
    }

    public void setCoupon_fee(Integer couponFee) {
        this.coupon_fee = couponFee;
    }

    public String getFee_type() {
        return this.fee_type;
    }

    public void setFee_type(String feeType) {
        this.fee_type = feeType;
    }

    public String getBank_type() {
        return this.bank_type;
    }

    public void setBank_type(String bankType) {
        this.bank_type = bankType;
    }

    public String getBank_billno() {
        return this.bank_billno;
    }

    public void setBank_billno(String bankBillno) {
        this.bank_billno = bankBillno;
    }

    public String getTime_end() {
        return this.time_end;
    }

    public void setTime_end(String timeEnd) {
        this.time_end = timeEnd;
    }

    public String getT0Flag() {
        return this.t0Flag;
    }

    public void setT0Flag(String t0Flag) {
        this.t0Flag = t0Flag;
    }

    public String getGynotify_back() {
        return this.gynotify_back;
    }

    public void setGynotify_back(String gynotifyBack) {
        this.gynotify_back = gynotifyBack;
    }

    public String getAppid() {
        return this.appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getToken_id() {
        return this.token_id;
    }

    public void setToken_id(String tokenId) {
        this.token_id = tokenId;
    }

    public String getIs_raw() {
        return this.is_raw;
    }

    public void setIs_raw(String isRaw) {
        this.is_raw = isRaw;
    }

    public String getCallback_url() {
        return this.callback_url;
    }

    public void setCallback_url(String callbackUrl) {
        this.callback_url = callbackUrl;
    }

    public String getLimit_credit_pay() {
        return this.limit_credit_pay;
    }

    public void setLimit_credit_pay(String limitCreditPay) {
        this.limit_credit_pay = limitCreditPay;
    }

    public String getResp_code() {
        return this.resp_code;
    }

    public void setResp_code(String respCode) {
        this.resp_code = respCode;
    }

    public String getTrade_source() {
        return this.trade_source;
    }

    public void setTrade_source(String tradeSource) {
        this.trade_source = tradeSource;
    }

    public String getIs_new_trade() {
        return this.is_new_trade;
    }

    public void setIs_new_trade(String isNewTrade) {
        this.is_new_trade = isNewTrade;
    }

    public Integer getNotify_num() {
        return this.notify_num;
    }

    public void setNotify_num(Integer notifyNum) {
        this.notify_num = notifyNum;
    }

    public String getNotify_data() {
        return this.notify_data;
    }

    public void setNotify_data(String notifyData) {
        this.notify_data = notifyData;
    }

    public String getNofity_time() {
        return this.nofity_time;
    }

    public void setNofity_time(String nofityTime) {
        this.nofity_time = nofityTime;
    }
}
