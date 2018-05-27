/*
 * Decompiled with CFR 0_124.
 */
package com.trade.bean;

public class NativeNotifyResultBean {
    private String service;
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
    private String out_trade_no;
    private String total_fee;
    private String coupon_fee;
    private String fee_type;
    private String attach;
    private String bank_type;
    private String bank_billno;
    private String time_end;

    public String getService() {
        return this.service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCharset() {
        return this.charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getSign_type() {
        return this.sign_type;
    }

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

    public String getMch_id() {
        return this.mch_id;
    }

    public void setMch_id(String mchId) {
        this.mch_id = mchId;
    }

    public String getDevice_info() {
        return this.device_info;
    }

    public void setDevice_info(String deviceInfo) {
        this.device_info = deviceInfo;
    }

    public String getNonce_str() {
        return this.nonce_str;
    }

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

    public String getSign() {
        return this.sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
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

    public String getOut_trade_no() {
        return this.out_trade_no;
    }

    public void setOut_trade_no(String outTradeNo) {
        this.out_trade_no = outTradeNo;
    }

    public String getFee_type() {
        return this.fee_type;
    }

    public void setFee_type(String feeType) {
        this.fee_type = feeType;
    }

    public String getAttach() {
        return this.attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
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

    public String getPay_result() {
        return this.pay_result;
    }

    public void setPay_result(String payResult) {
        this.pay_result = payResult;
    }

    public String getTotal_fee() {
        return this.total_fee;
    }

    public void setTotal_fee(String totalFee) {
        this.total_fee = totalFee;
    }

    public String getCoupon_fee() {
        return this.coupon_fee;
    }

    public void setCoupon_fee(String couponFee) {
        this.coupon_fee = couponFee;
    }

    public String getTrade_state() {
        return this.trade_state;
    }

    public void setTrade_state(String tradeState) {
        this.trade_state = tradeState;
    }

    public String toString() {
        return "NativeNotifyResultBean [attach=" + this.attach + ", bank_billno=" + this.bank_billno + ", bank_type=" + this.bank_type + ", charset=" + this.charset + ", coupon_fee=" + this.coupon_fee + ", device_info=" + this.device_info + ", err_code=" + this.err_code + ", err_msg=" + this.err_msg + ", fee_type=" + this.fee_type + ", is_subscribe=" + this.is_subscribe + ", mch_id=" + this.mch_id + ", message=" + this.message + ", nonce_str=" + this.nonce_str + ", openid=" + this.openid + ", out_trade_no=" + this.out_trade_no + ", out_transaction_id=" + this.out_transaction_id + ", pay_info=" + this.pay_info + ", pay_result=" + this.pay_result + ", result_code=" + this.result_code + ", service=" + this.service + ", sign=" + this.sign + ", sign_type=" + this.sign_type + ", status=" + this.status + ", sub_appid=" + this.sub_appid + ", sub_is_subscribe=" + this.sub_is_subscribe + ", sub_openid=" + this.sub_openid + ", time_end=" + this.time_end + ", total_fee=" + this.total_fee + ", trade_state=" + this.trade_state + ", trade_type=" + this.trade_type + ", transaction_id=" + this.transaction_id + ", version=" + this.version + "]";
    }
}
