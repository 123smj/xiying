/*
 * Decompiled with CFR 0_124.
 */
package com.trade.bean.own;

public class DfParam {
    private String gymchtId;
    private String dfSn;
    private Integer receiptAmount;
    private String curType;
    private String payType;
    private String receiptName;
    private String receiptPan;
    private String receiptBankNm;
    private String settleNo;
    private String acctType;
    private String mobile;
    private String memo;
    private String nonce;
    private String sign;
    private String dfTransactionId;
    private String qryTime;
    private String qryType;

    public String getGymchtId() {
        return this.gymchtId;
    }

    public void setGymchtId(String gymchtId) {
        this.gymchtId = gymchtId;
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

    public String getCurType() {
        return this.curType;
    }

    public void setCurType(String curType) {
        this.curType = curType;
    }

    public String getPayType() {
        return this.payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
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

    public String getSettleNo() {
        return this.settleNo;
    }

    public void setSettleNo(String settleNo) {
        this.settleNo = settleNo;
    }

    public String getAcctType() {
        return this.acctType;
    }

    public void setAcctType(String acctType) {
        this.acctType = acctType;
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

    public String getNonce() {
        return this.nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getSign() {
        return this.sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getQryTime() {
        return this.qryTime;
    }

    public void setQryTime(String qryTime) {
        this.qryTime = qryTime;
    }

    public String getQryType() {
        return this.qryType;
    }

    public void setQryType(String qryType) {
        this.qryType = qryType;
    }

    public String getDfTransactionId() {
        return this.dfTransactionId;
    }

    public void setDfTransactionId(String dfTransactionId) {
        this.dfTransactionId = dfTransactionId;
    }

    public String toString() {
        return "DfParam [acctType=" + this.acctType + ", curType=" + this.curType + ", dfSn=" + this.dfSn + ", gymchtId=" + this.gymchtId + ", memo=" + this.memo + ", mobile=" + this.mobile + ", nonce=" + this.nonce + ", payType=" + this.payType + ", receiptAmount=" + this.receiptAmount + ", receiptBankNm=" + this.receiptBankNm + ", receiptName=" + this.receiptName + ", receiptPan=" + this.receiptPan + ", settleNo=" + this.settleNo + ", sign=" + this.sign + "]";
    }
}
