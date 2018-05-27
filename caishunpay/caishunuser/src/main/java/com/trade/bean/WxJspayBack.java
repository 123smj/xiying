/*
 * Decompiled with CFR 0_124.
 */
package com.trade.bean;

public class WxJspayBack {
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

    public String getAppid() {
        return this.appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
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

    public String toString() {
        return "WxJspayBack [appid=" + this.appid + ", charset=" + this.charset + ", device_info=" + this.device_info + ", err_code=" + this.err_code + ", err_msg=" + this.err_msg + ", mch_id=" + this.mch_id + ", message=" + this.message + ", nonce_str=" + this.nonce_str + ", pay_info=" + this.pay_info + ", result_code=" + this.result_code + ", sign=" + this.sign + ", sign_type=" + this.sign_type + ", status=" + this.status + ", token_id=" + this.token_id + ", version=" + this.version + "]";
    }
}
