/*
 * Decompiled with CFR 0_124.
 */
package com.account.bean;

import java.io.Serializable;

public class DfListBean
        implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String recAccountNo;
    private String tranId;
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
    private String field3;
    private String insertTime;
    private String updateTime;
    private String keyRsp;
    private String instDate;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecAccountNo() {
        return this.recAccountNo;
    }

    public void setRecAccountNo(String recAccountNo) {
        this.recAccountNo = recAccountNo;
    }

    public String getTranId() {
        return this.tranId;
    }

    public void setTranId(String tranId) {
        this.tranId = tranId;
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

    public String getField3() {
        return this.field3;
    }

    public void setField3(String field3) {
        this.field3 = field3;
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

    public String getKeyRsp() {
        return this.keyRsp;
    }

    public void setKeyRsp(String keyRsp) {
        this.keyRsp = keyRsp;
    }

    public String getInstDate() {
        return this.instDate;
    }

    public void setInstDate(String instDate) {
        this.instDate = instDate;
    }
}
