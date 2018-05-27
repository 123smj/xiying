/*
 * Decompiled with CFR 0_124.
 */
package com.trade.bean;

import com.gy.util.StringUtil;

public class TradeDfbean {
    private String out_trade_no;
    private String tradeSn;
    private String transaction_id;
    private String realname;
    private String idcard;
    private String bankcardnum;
    private String bankname;
    private String subbranch;
    private String pcnaps;
    private String stlType;
    private Integer money;
    private Integer free;
    private Integer fee;
    private String t0RespCode;
    private String t0RespDesc;
    private String t0_status;
    private String time_start;
    private String time_end;
    private String formatBankcardnum;

    public String getOut_trade_no() {
        return this.out_trade_no;
    }

    public void setOut_trade_no(String outTradeNo) {
        this.out_trade_no = outTradeNo;
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

    public String getRealname() {
        return this.realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getIdcard() {
        return this.idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getBankcardnum() {
        return this.bankcardnum;
    }

    public void setBankcardnum(String bankcardnum) {
        this.bankcardnum = bankcardnum;
    }

    public Integer getMoney() {
        return this.money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Integer getFree() {
        return this.free;
    }

    public void setFree(Integer free) {
        this.free = free;
    }

    public Integer getFee() {
        return this.fee;
    }

    public void setFee(Integer fee) {
        this.fee = fee;
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

    public String getTime_start() {
        return this.time_start;
    }

    public void setTime_start(String timeStart) {
        this.time_start = timeStart;
    }

    public String getTime_end() {
        return this.time_end;
    }

    public void setTime_end(String timeEnd) {
        this.time_end = timeEnd;
    }

    public String getT0_status() {
        return this.t0_status;
    }

    public void setT0_status(String t0Status) {
        this.t0_status = t0Status;
    }

    public String getBankname() {
        return this.bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getSubbranch() {
        return this.subbranch;
    }

    public void setSubbranch(String subbranch) {
        this.subbranch = subbranch;
    }

    public String getFormatBankcardnum() {
        return StringUtil.mosaic(this.bankcardnum, '*');
    }

    public void setFormatBankcardnum(String formatBankcardnum) {
        this.formatBankcardnum = formatBankcardnum;
    }

    public String getPcnaps() {
        return this.pcnaps;
    }

    public void setPcnaps(String pcnaps) {
        this.pcnaps = pcnaps;
    }

    public String getStlType() {
        return this.stlType;
    }

    public void setStlType(String stlType) {
        this.stlType = stlType;
    }
}
