/*
 * Decompiled with CFR 0_124.
 */
package com.trade.bean.df;

import java.io.Serializable;

public class TradeUnionDfBean
implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String mcht_tranid;
    private String gy_tranid;
    private String recAccountNo;
    private String recAccountName;
    private String rcvBankCode;
    private String rcvBankName;
    private int tranAmount;
    private String tranState;
    private String tranDesc;
    private String batchNo;
    private String abstracted;
    private String channel;
    private String field1;
    private String field2;
    private String memo;
    private String insertTime;
    private String updateTime;
    private String mcht_no;
    private String pay_type;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMcht_tranid() {
        return this.mcht_tranid;
    }

    public void setMcht_tranid(String mchtTranid) {
        this.mcht_tranid = mchtTranid;
    }

    public String getGy_tranid() {
        return this.gy_tranid;
    }

    public void setGy_tranid(String gyTranid) {
        this.gy_tranid = gyTranid;
    }

    public String getRecAccountNo() {
        return this.recAccountNo;
    }

    public void setRecAccountNo(String recAccountNo) {
        this.recAccountNo = recAccountNo;
    }

    public String getRecAccountName() {
        return this.recAccountName;
    }

    public void setRecAccountName(String recAccountName) {
        this.recAccountName = recAccountName;
    }

    public String getRcvBankCode() {
        return this.rcvBankCode;
    }

    public void setRcvBankCode(String rcvBankCode) {
        this.rcvBankCode = rcvBankCode;
    }

    public String getRcvBankName() {
        return this.rcvBankName;
    }

    public void setRcvBankName(String rcvBankName) {
        this.rcvBankName = rcvBankName;
    }

    public int getTranAmount() {
        return this.tranAmount;
    }

    public void setTranAmount(int tranAmount) {
        this.tranAmount = tranAmount;
    }

    public String getTranState() {
        return this.tranState;
    }

    public void setTranState(String tranState) {
        this.tranState = tranState;
    }

    public String getTranDesc() {
        return this.tranDesc;
    }

    public void setTranDesc(String tranDesc) {
        this.tranDesc = tranDesc;
    }

    public String getBatchNo() {
        return this.batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getAbstracted() {
        return this.abstracted;
    }

    public void setAbstracted(String abstracted) {
        this.abstracted = abstracted;
    }

    public String getChannel() {
        return this.channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getField1() {
        return this.field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return this.field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getInsertTime() {
        return this.insertTime;
    }

    public void setInsertTime(String insertTime) {
        this.insertTime = insertTime;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getMcht_no() {
        return this.mcht_no;
    }

    public void setMcht_no(String mchtNo) {
        this.mcht_no = mchtNo;
    }

    public String getPay_type() {
        return this.pay_type;
    }

    public void setPay_type(String payType) {
        this.pay_type = payType;
    }

    public String toString() {
        return "TradeUnionDfBean [abstracted=" + this.abstracted + ", batchNo=" + this.batchNo + ", channel=" + this.channel + ", field1=" + this.field1 + ", field2=" + this.field2 + ", gy_tranid=" + this.gy_tranid + ", id=" + this.id + ", insertTime=" + this.insertTime + ", mcht_no=" + this.mcht_no + ", mcht_tranid=" + this.mcht_tranid + ", memo=" + this.memo + ", pay_type=" + this.pay_type + ", rcvBankCode=" + this.rcvBankCode + ", rcvBankName=" + this.rcvBankName + ", recAccountName=" + this.recAccountName + ", recAccountNo=" + this.recAccountNo + ", tranAmount=" + this.tranAmount + ", tranDesc=" + this.tranDesc + ", tranState=" + this.tranState + ", updateTime=" + this.updateTime + "]";
    }
}
