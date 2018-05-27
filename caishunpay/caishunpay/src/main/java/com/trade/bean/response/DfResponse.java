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

@JsonInclude(value=JsonInclude.Include.NON_NULL)
public class DfResponse
extends Response {
    private String gymchtId;
    private String accountStatus;
    private Integer balance;
    private String dfTransactionId;
    private String dfSn;
    private String dfState;
    private String dfDesc;
    private String timeEnd;
    private Integer receiptAmount;
    private String receiptName;
    private String receiptPan;
    private String receiptBankNm;
    private String mobile;
    private String memo;

    public String getGymchtId() {
        return this.gymchtId;
    }

    public void setGymchtId(String gymchtId) {
        this.gymchtId = gymchtId;
    }

    public String getAccountStatus() {
        return this.accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public Integer getBalance() {
        return this.balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public String getDfTransactionId() {
        return this.dfTransactionId;
    }

    public void setDfTransactionId(String dfTransactionId) {
        this.dfTransactionId = dfTransactionId;
    }

    public String getDfState() {
        return this.dfState;
    }

    public void setDfState(String dfState) {
        this.dfState = dfState;
    }

    public String getDfDesc() {
        return this.dfDesc;
    }

    public void setDfDesc(String dfDesc) {
        this.dfDesc = dfDesc;
    }

    public String getTimeEnd() {
        return this.timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getDfSn() {
        return this.dfSn;
    }

    public void setDfSn(String dfSn) {
        this.dfSn = dfSn;
    }

    public Integer getReceiptAmount() {
        return this.receiptAmount;
    }

    public void setReceiptAmount(Integer receiptAmount) {
        this.receiptAmount = receiptAmount;
    }

    public String getReceiptName() {
        return this.receiptName;
    }

    public void setReceiptName(String receiptName) {
        this.receiptName = receiptName;
    }

    public String getReceiptPan() {
        return this.receiptPan;
    }

    public void setReceiptPan(String receiptPan) {
        this.receiptPan = receiptPan;
    }

    public String getReceiptBankNm() {
        return this.receiptBankNm;
    }

    public void setReceiptBankNm(String receiptBankNm) {
        this.receiptBankNm = receiptBankNm;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
