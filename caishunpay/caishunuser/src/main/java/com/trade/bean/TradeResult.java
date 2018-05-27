/*
 * Decompiled with CFR 0_124.
 */
package com.trade.bean;

public class TradeResult {
    private String gymchtId;
    private String chanelId;
    private String tradeSn;
    private String transaction_id;
    private int orderAmount;
    private int coupon_fee;
    private String tradeState;
    private String bankType;
    private String timeEnd;
    private String sign;

    public String getTradeState() {
        return this.tradeState;
    }

    public void setTradeState(String tradeState) {
        this.tradeState = tradeState;
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

    public String getTransaction_id() {
        return this.transaction_id;
    }

    public void setTransaction_id(String transactionId) {
        this.transaction_id = transactionId;
    }

    public int getOrderAmount() {
        return this.orderAmount;
    }

    public void setOrderAmount(int orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getBankType() {
        return this.bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getTimeEnd() {
        return this.timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public int getCoupon_fee() {
        return this.coupon_fee;
    }

    public void setCoupon_fee(int couponFee) {
        this.coupon_fee = couponFee;
    }

    public String getSign() {
        return this.sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
