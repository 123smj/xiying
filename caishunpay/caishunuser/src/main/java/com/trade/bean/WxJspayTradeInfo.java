/*
 * Decompiled with CFR 0_124.
 */
package com.trade.bean;

import com.trade.bean.WxJspayParam;

public class WxJspayTradeInfo
        extends WxJspayParam {
    private String appid;
    private String version;
    private String charset;
    private String sign_type;
    private String status;
    private String message;
    private String result_code;
    private String mch_id;
    private String device_info;
    private String nonce_str;
    private String err_code;
    private String err_msg;
    private String sign;
    private String token_id;
    private String pay_info;
    private String gymchtId;
    private String channel_id;
    private String gy_notifyUrl;
    private String tradeSn;
    private String trade_state;
    private String openid;
    private String trade_type;
    private String is_subscribe;
    private String pay_result;
    private String transaction_id;
    private String out_transaction_id;
    private String sub_is_subscribe;
    private String sub_appid;
    private String sub_openid;
    private String coupon_fee;
    private String fee_type;
    private String bank_type;
    private String bank_billno;
    private String time_end;

    public String getAppid() {
        return this.appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    @Override
    public String getVersion() {
        return this.version;
    }

    @Override
    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String getCharset() {
        return this.charset;
    }

    @Override
    public void setCharset(String charset) {
        this.charset = charset;
    }

    @Override
    public String getSign_type() {
        return this.sign_type;
    }

    @Override
    public void setSign_type(String signType) {
        this.sign_type = signType;
    }

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

    @Override
    public String getMch_id() {
        return this.mch_id;
    }

    @Override
    public void setMch_id(String mchId) {
        this.mch_id = mchId;
    }

    @Override
    public String getDevice_info() {
        return this.device_info;
    }

    @Override
    public void setDevice_info(String deviceInfo) {
        this.device_info = deviceInfo;
    }

    @Override
    public String getNonce_str() {
        return this.nonce_str;
    }

    @Override
    public void setNonce_str(String nonceStr) {
        this.nonce_str = nonceStr;
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

    @Override
    public String getSign() {
        return this.sign;
    }

    @Override
    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getToken_id() {
        return this.token_id;
    }

    public void setToken_id(String tokenId) {
        this.token_id = tokenId;
    }

    public String getPay_info() {
        return this.pay_info;
    }

    public void setPay_info(String payInfo) {
        this.pay_info = payInfo;
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

    @Override
    public String getSub_appid() {
        return this.sub_appid;
    }

    @Override
    public void setSub_appid(String subAppid) {
        this.sub_appid = subAppid;
    }

    @Override
    public String getSub_openid() {
        return this.sub_openid;
    }

    @Override
    public void setSub_openid(String subOpenid) {
        this.sub_openid = subOpenid;
    }

    public String getCoupon_fee() {
        return this.coupon_fee;
    }

    public void setCoupon_fee(String couponFee) {
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

    @Override
    public String toString() {
        return "WxJspayTradeInfo [appid=" + this.appid + ", channel_id=" + this.channel_id + ", charset=" + this.charset + ", device_info=" + this.device_info + ", err_code=" + this.err_code + ", err_msg=" + this.err_msg + ", gy_notifyUrl=" + this.gy_notifyUrl + ", gymchtId=" + this.gymchtId + ", mch_id=" + this.mch_id + ", message=" + this.message + ", nonce_str=" + this.nonce_str + ", pay_info=" + this.pay_info + ", result_code=" + this.result_code + ", sign=" + this.sign + ", sign_type=" + this.sign_type + ", status=" + this.status + ", token_id=" + this.token_id + ", tradeSn=" + this.tradeSn + ", version=" + this.version + "]";
    }
}
