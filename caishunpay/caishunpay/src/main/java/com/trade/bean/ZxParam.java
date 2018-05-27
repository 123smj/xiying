/*
 * Decompiled with CFR 0_124.
 */
package com.trade.bean;

public class ZxParam {
    private String method;
    private String version;
    private String charset;
    private String sign_type;
    private String appid;
    private String mch_id;
    private String sign_agentno;
    private String out_trade_no;
    private String device_info;
    private String body;
    private String attach;
    private int total_fee;
    private String detail;
    private String store_appid;
    private String store_name;
    private String op_user;
    private String fee_type;
    private String spbill_create_ip;
    private String notify_url;
    private String time_start;
    private String time_expire;
    private String goods_tag;
    private String product_id;
    private String nonce_str;
    private String sign;
    private String openid;
    private String wx_appid;

    public ZxParam() {
        this.version = "2.0.1";
        this.charset = "UTF-8";
        this.sign_type = "MD5";
    }

    public ZxParam(String method, String mchId, String outTradeNo) {
        this.method = method;
        this.mch_id = mchId;
        this.out_trade_no = outTradeNo;
        this.version = "2.0.1";
        this.charset = "UTF-8";
        this.sign_type = "MD5";
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
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

    public String getAppid() {
        return this.appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return this.mch_id;
    }

    public void setMch_id(String mchId) {
        this.mch_id = mchId;
    }

    public String getSign_agentno() {
        return this.sign_agentno;
    }

    public void setSign_agentno(String signAgentno) {
        this.sign_agentno = signAgentno;
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

    public String getDetail() {
        return this.detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getStore_appid() {
        return this.store_appid;
    }

    public void setStore_appid(String storeAppid) {
        this.store_appid = storeAppid;
    }

    public String getStore_name() {
        return this.store_name;
    }

    public void setStore_name(String storeName) {
        this.store_name = storeName;
    }

    public String getOp_user() {
        return this.op_user;
    }

    public void setOp_user(String opUser) {
        this.op_user = opUser;
    }

    public String getFee_type() {
        return this.fee_type;
    }

    public void setFee_type(String feeType) {
        this.fee_type = feeType;
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

    public String getSpbill_create_ip() {
        return this.spbill_create_ip;
    }

    public void setSpbill_create_ip(String spbillCreateIp) {
        this.spbill_create_ip = spbillCreateIp;
    }

    public String getGoods_tag() {
        return this.goods_tag;
    }

    public void setGoods_tag(String goodsTag) {
        this.goods_tag = goodsTag;
    }

    public String getProduct_id() {
        return this.product_id;
    }

    public void setProduct_id(String productId) {
        this.product_id = productId;
    }

    public String getOpenid() {
        return this.openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getWx_appid() {
        return this.wx_appid;
    }

    public void setWx_appid(String wxAppid) {
        this.wx_appid = wxAppid;
    }
}
