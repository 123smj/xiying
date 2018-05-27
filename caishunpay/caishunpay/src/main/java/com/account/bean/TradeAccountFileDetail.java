/*
 * Decompiled with CFR 0_124.
 */
package com.account.bean;

import java.io.Serializable;

public class TradeAccountFileDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    private int offerSeq;
    private String offerDoc;
    private String mcht_no;
    private String mcht_name;
    private int trade_amount;
    private int mcht_income;
    private String dfsn;
    private String offer_time;
    private String trade_type;
    private String opr_time;
    private String status;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getOfferSeq() {
        return this.offerSeq;
    }

    public void setOfferSeq(int offerSeq) {
        this.offerSeq = offerSeq;
    }

    public String getMcht_no() {
        return this.mcht_no;
    }

    public void setMcht_no(String mchtNo) {
        this.mcht_no = mchtNo;
    }

    public String getMcht_name() {
        return this.mcht_name;
    }

    public void setMcht_name(String mchtName) {
        this.mcht_name = mchtName;
    }

    public int getTrade_amount() {
        return this.trade_amount;
    }

    public void setTrade_amount(int tradeAmount) {
        this.trade_amount = tradeAmount;
    }

    public int getMcht_income() {
        return this.mcht_income;
    }

    public void setMcht_income(int mchtIncome) {
        this.mcht_income = mchtIncome;
    }

    public String getDfsn() {
        return this.dfsn;
    }

    public void setDfsn(String dfsn) {
        this.dfsn = dfsn;
    }

    public String getOffer_time() {
        return this.offer_time;
    }

    public void setOffer_time(String offerTime) {
        this.offer_time = offerTime;
    }

    public String getTrade_type() {
        return this.trade_type;
    }

    public void setTrade_type(String tradeType) {
        this.trade_type = tradeType;
    }

    public String getOpr_time() {
        return this.opr_time;
    }

    public void setOpr_time(String oprTime) {
        this.opr_time = oprTime;
    }

    public String getOfferDoc() {
        return this.offerDoc;
    }

    public void setOfferDoc(String offerDoc) {
        this.offerDoc = offerDoc;
    }
}
