/*
 * Decompiled with CFR 0_124.
 */
package com.trade.bean.own;

public class QuickpayParam {
    private String remoteAddr;
    private String gymchtId;
    private String chanelId;
    private String tradeSn;
    private Integer orderAmount;
    private String cardHolderName;
    private String cardNo;
    private String cardType;
    private String expireDate;
    private String cvv;
    private String bankCode;
    private String bankName;
    private String cerType;
    private String cerNumber;
    private String mobileNum;
    private String yzm;
    private String sign;
    private String nonce;
    private String transaction_id;
    private String goodsName;
    private String expirySecond;
    private String callbackUrl;
    private String notifyUrl;
    private String bankSegment;
    private String channelType;
    private String t0Flag;
    private String tradeSource;

    public String getRemoteAddr() {
        return this.remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getNonce() {
        return this.nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getGymchtId() {
        return this.gymchtId;
    }

    public void setGymchtId(String gymchtId) {
        this.gymchtId = gymchtId;
    }

    public String getChanelId() {
        return this.chanelId;
    }

    public void setChanelId(String chanelId) {
        this.chanelId = chanelId;
    }

    public String getTradeSn() {
        return this.tradeSn;
    }

    public void setTradeSn(String tradeSn) {
        this.tradeSn = tradeSn;
    }

    public Integer getOrderAmount() {
        return this.orderAmount;
    }

    public void setOrderAmount(Integer orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getCardHolderName() {
        return this.cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCardNo() {
        return this.cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardType() {
        return this.cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getExpireDate() {
        return this.expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getCvv() {
        return this.cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getBankCode() {
        return this.bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return this.bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCerType() {
        return this.cerType;
    }

    public void setCerType(String cerType) {
        this.cerType = cerType;
    }

    public String getCerNumber() {
        return this.cerNumber;
    }

    public void setCerNumber(String cerNumber) {
        this.cerNumber = cerNumber;
    }

    public String getMobileNum() {
        return this.mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getT0Flag() {
        return this.t0Flag;
    }

    public void setT0Flag(String t0Flag) {
        this.t0Flag = t0Flag;
    }

    public String getTradeSource() {
        return this.tradeSource;
    }

    public void setTradeSource(String tradeSource) {
        this.tradeSource = tradeSource;
    }

    public String getSign() {
        return this.sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getYzm() {
        return this.yzm;
    }

    public void setYzm(String yzm) {
        this.yzm = yzm;
    }

    public String getTransaction_id() {
        return this.transaction_id;
    }

    public void setTransaction_id(String transactionId) {
        this.transaction_id = transactionId;
    }

    public String getGoodsName() {
        return this.goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getExpirySecond() {
        return this.expirySecond;
    }

    public void setExpirySecond(String expirySecond) {
        this.expirySecond = expirySecond;
    }

    public String getCallbackUrl() {
        return this.callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getNotifyUrl() {
        return this.notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getBankSegment() {
        return this.bankSegment;
    }

    public void setBankSegment(String bankSegment) {
        this.bankSegment = bankSegment;
    }

    public String getChannelType() {
        return this.channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String toString() {
        return "QuickpayParam [bankCode=" + this.bankCode + ", bankName=" + this.bankName + ", cardHolderName=" + this.cardHolderName + ", cardNo=" + this.cardNo + ", cardType=" + this.cardType + ", cerNumber=" + this.cerNumber + ", cerType=" + this.cerType + ", chanelId=" + this.chanelId + ", cvv=" + this.cvv + ", expireDate=" + this.expireDate + ", gymchtId=" + this.gymchtId + ", mobileNum=" + this.mobileNum + ", nonce=" + this.nonce + ", orderAmount=" + this.orderAmount + ", remoteAddr=" + this.remoteAddr + ", sign=" + this.sign + ", t0Flag=" + this.t0Flag + ", tradeSn=" + this.tradeSn + ", tradeSource=" + this.tradeSource + ", transaction_id=" + this.transaction_id + ", yzm=" + this.yzm + "]";
    }
}
