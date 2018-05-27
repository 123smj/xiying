/*
 * Decompiled with CFR 0_124.
 */
package com.trade.bean;

public class WxJspayParam {
    private String sign_agentno;
    private String service;
    private String version;
    private String charset;
    private String sign_type;
    private String mch_id;
    private String is_raw;
    private String out_trade_no;
    private String device_info;
    private String body;
    private String sub_openid;
    private String sub_appid;
    private String attach;
    private int total_fee;
    private String mch_create_ip;
    private String notify_url;
    private String callback_url;
    private String time_start;
    private String time_expire;
    private String goods_tag;
    private String nonce_str;
    private String limit_credit_pay;
    private String sign;

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

    public String getMch_id() {
        return this.mch_id;
    }

    public void setMch_id(String mchId) {
        this.mch_id = mchId;
    }

    public String getOut_trade_no() {
        return this.out_trade_no;
    }

    public void setOut_trade_no(String outTradeNo) {
        this.out_trade_no = outTradeNo;
    }

    public String getDevice_info() {
        return this.device_info;
    }

    public void setDevice_info(String deviceInfo) {
        this.device_info = deviceInfo;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAttach() {
        return this.attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public int getTotal_fee() {
        return this.total_fee;
    }

    public void setTotal_fee(int totalFee) {
        this.total_fee = totalFee;
    }

    public String getMch_create_ip() {
        return this.mch_create_ip;
    }

    public void setMch_create_ip(String mchCreateIp) {
        this.mch_create_ip = mchCreateIp;
    }

    public String getNotify_url() {
        return this.notify_url;
    }

    public void setNotify_url(String notifyUrl) {
        this.notify_url = notifyUrl;
    }

    public String getTime_start() {
        return this.time_start;
    }

    public void setTime_start(String timeStart) {
        this.time_start = timeStart;
    }

    public String getTime_expire() {
        return this.time_expire;
    }

    public void setTime_expire(String timeExpire) {
        this.time_expire = timeExpire;
    }

    public String getGoods_tag() {
        return this.goods_tag;
    }

    public void setGoods_tag(String goodsTag) {
        this.goods_tag = goodsTag;
    }

    public String getNonce_str() {
        return this.nonce_str;
    }

    public void setNonce_str(String nonceStr) {
        this.nonce_str = nonceStr;
    }

    public String getSign() {
        return this.sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getIs_raw() {
        return this.is_raw;
    }

    public void setIs_raw(String isRaw) {
        this.is_raw = isRaw;
    }

    public String getSub_openid() {
        return this.sub_openid;
    }

    public void setSub_openid(String subOpenid) {
        this.sub_openid = subOpenid;
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

    public String getSign_agentno() {
        return this.sign_agentno;
    }

    public void setSign_agentno(String signAgentno) {
        this.sign_agentno = signAgentno;
    }

    public String getSub_appid() {
        return this.sub_appid;
    }

    public void setSub_appid(String subAppid) {
        this.sub_appid = subAppid;
    }

    public String toString() {
        return "WxJspayParam [attach=" + this.attach + ", body=" + this.body + ", callback_url=" + this.callback_url + ", charset=" + this.charset + ", device_info=" + this.device_info + ", goods_tag=" + this.goods_tag + ", is_raw=" + this.is_raw + ", limit_credit_pay=" + this.limit_credit_pay + ", mch_create_ip=" + this.mch_create_ip + ", mch_id=" + this.mch_id + ", nonce_str=" + this.nonce_str + ", notify_url=" + this.notify_url + ", out_trade_no=" + this.out_trade_no + ", service=" + this.service + ", sign=" + this.sign + ", sign_agentno=" + this.sign_agentno + ", sign_type=" + this.sign_type + ", sub_appid=" + this.sub_appid + ", sub_openid=" + this.sub_openid + ", time_expire=" + this.time_expire + ", time_start=" + this.time_start + ", total_fee=" + this.total_fee + ", version=" + this.version + "]";
    }
}
