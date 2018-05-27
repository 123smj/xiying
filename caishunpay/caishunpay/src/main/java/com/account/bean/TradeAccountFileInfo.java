/*
 * Decompiled with CFR 0_124.
 */
package com.account.bean;

import java.io.Serializable;

public class TradeAccountFileInfo
implements Serializable {
    private static final long serialVersionUID = 1L;
    private int offerSeq;
    private String payType;
    private String offerTime;
    private String oprId;
    private String oprTime;
    private String offerDoc;
    private String status;
    private String description;
    private String checkTime;
    private String checkOpr;

    public int getOfferSeq() {
        return this.offerSeq;
    }

    public void setOfferSeq(int offerSeq) {
        this.offerSeq = offerSeq;
    }

    public String getPayType() {
        return this.payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getOfferTime() {
        return this.offerTime;
    }

    public void setOfferTime(String offerTime) {
        this.offerTime = offerTime;
    }

    public String getOprId() {
        return this.oprId;
    }

    public void setOprId(String oprId) {
        this.oprId = oprId;
    }

    public String getOfferDoc() {
        return this.offerDoc;
    }

    public void setOfferDoc(String offerDoc) {
        this.offerDoc = offerDoc;
    }

    public String getOprTime() {
        return this.oprTime;
    }

    public void setOprTime(String oprTime) {
        this.oprTime = oprTime;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCheckTime() {
        return this.checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getCheckOpr() {
        return this.checkOpr;
    }

    public void setCheckOpr(String checkOpr) {
        this.checkOpr = checkOpr;
    }
}
