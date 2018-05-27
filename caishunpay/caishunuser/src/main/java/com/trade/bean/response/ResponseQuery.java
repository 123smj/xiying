/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 */
package com.trade.bean.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.trade.bean.response.Response;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ResponseQuery
        extends Response {
    private String gymchtId;
    private String chanelId;
    private String tradeSn;
    private String transaction_id;
    private String out_transaction_id;
    private Integer orderAmount;
    private Integer coupon_fee;
    private String tradeState;
    private String bankType;
    private String timeEnd;
    private String pay_result;
    private String pay_info;
    private String t0Flag;
    private String t0RespCode;
    private String t0RespDesc;
    private String t0_status;

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

    public Integer getOrderAmount() {
        return this.orderAmount;
    }

    public void setOrderAmount(Integer orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Integer getCoupon_fee() {
        return this.coupon_fee;
    }

    public void setCoupon_fee(Integer couponFee) {
        this.coupon_fee = couponFee;
    }

    public String getTradeState() {
        return this.tradeState;
    }

    public void setTradeState(String tradeState) {
        this.tradeState = tradeState;
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

    public String getT0Flag() {
        return this.t0Flag;
    }

    public void setT0Flag(String t0Flag) {
        this.t0Flag = t0Flag;
    }

    public String getT0_status() {
        return this.t0_status;
    }

    public void setT0_status(String t0Status) {
        this.t0_status = t0Status;
    }

    public String getT0RespCode() {
        return this.t0RespCode;
    }

    public void setT0RespCode(String t0RespCode) {
        this.t0RespCode = t0RespCode;
    }

    public String getT0RespDesc() {
        return this.t0RespDesc;
    }

    public void setT0RespDesc(String t0RespDesc) {
        this.t0RespDesc = t0RespDesc;
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

    public String getOut_transaction_id() {
        return this.out_transaction_id;
    }

    public void setOut_transaction_id(String outTransactionId) {
        this.out_transaction_id = outTransactionId;
    }
}
