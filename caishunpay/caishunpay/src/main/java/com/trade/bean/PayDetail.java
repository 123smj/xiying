/*
 * Decompiled with CFR 0_124.
 */
package com.trade.bean;

public class PayDetail {
    protected String gy_notifyUrl;
    protected String gynotify_back;
    protected Integer notify_num;
    protected String notify_data;
    protected String nofity_time;
    //商户ID
    protected String merchantId;
    //通道ID
    protected String channel_id;
    //对下游通道的交易号
    protected String tradeSn;
    //交易状态
    protected String trade_state;
    //主键, 对上游通道的交易号
    private String out_trade_no;
    private String service;
    private String version;
    private String charset;
    private String sign_type;
    //上游通道商户号
    private String mch_id;
    private String sign_agentno;
    private String device_info;
    private String body;
    private String attach;
    private int total_fee;
    private String mch_create_ip;
    private String notify_url;
    private String time_start;
    private String time_expire;
    private String op_user_id;
    private String goods_tag;
    private String product_id;
    private String nonce_str;
    private String sign;

    public PayDetail() {
        this.version = "2.0";
        this.charset = "UTF-8";
        this.sign_type = "MD5";
    }

    public PayDetail(String service, String mchId, String outTradeNo) {
        this.service = service;
        this.mch_id = mchId;
        this.out_trade_no = outTradeNo;
        this.version = "2.0";
        this.charset = "UTF-8";
        this.sign_type = "MD5";
    }

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

    public String getOp_user_id() {
        return this.op_user_id;
    }

    public void setOp_user_id(String opUserId) {
        this.op_user_id = opUserId;
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

    public String getGy_notifyUrl() {
        return this.gy_notifyUrl;
    }

    public void setGy_notifyUrl(String gyNotifyUrl) {
        this.gy_notifyUrl = gyNotifyUrl;
    }

    public String getGynotify_back() {
        return this.gynotify_back;
    }

    public void setGynotify_back(String gynotifyBack) {
        this.gynotify_back = gynotifyBack;
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

    public String getMerchantId() {
        return this.merchantId;
    }

    public void setMerchantId(String gymchtId) {
        this.merchantId = gymchtId;
    }

    public String getChannel_id() {
        return this.channel_id;
    }

    public void setChannel_id(String channelId) {
        this.channel_id = channelId;
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
}
