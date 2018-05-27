/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 */
package com.trade.bean.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
public class BankPayResponse
extends Response {
    private String merchantId;
    private String transaction_id;
    private String out_transaction_id;
    private String tradeState;
    private String yzm;
    private String tradeSn;
    private String orderAmount;
    private String cardHolderName;
    private String cardNo;
    private String cardType;
    private String bankName;
    private String timeEnd;
    private String payUrl;
    private String channelType;

    public String getMerchantId() {
        return this.merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
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

    public String getTradeState() {
        return this.tradeState;
    }

    public void setTradeState(String tradeState) {
        this.tradeState = tradeState;
    }

    public String getYzm() {
        return this.yzm;
    }

    public void setYzm(String yzm) {
        this.yzm = yzm;
    }

    @Override
    public String getNonce() {
        return this.nonce;
    }

    @Override
    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getTradeSn() {
        return this.tradeSn;
    }

    public void setTradeSn(String tradeSn) {
        this.tradeSn = tradeSn;
    }

    public String getOrderAmount() {
        return this.orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
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

    public String getBankName() {
        return this.bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getTimeEnd() {
        return this.timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getPayUrl() {
        return this.payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    public String getChannelType() {
        return this.channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }
}
