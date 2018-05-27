/*
 * Decompiled with CFR 0_124.
 */
package com.trade.bean;

public class TradeNotifyBean {
    private String out_trade_no;
    private String nativ_time_end;
    private String notify_time;
    private String returnData;
    private int notify_num;

    public String getOut_trade_no() {
        return this.out_trade_no;
    }

    public void setOut_trade_no(String outTradeNo) {
        this.out_trade_no = outTradeNo;
    }

    public String getNativ_time_end() {
        return this.nativ_time_end;
    }

    public void setNativ_time_end(String nativTimeEnd) {
        this.nativ_time_end = nativTimeEnd;
    }

    public String getNotify_time() {
        return this.notify_time;
    }

    public void setNotify_time(String notifyTime) {
        this.notify_time = notifyTime;
    }

    public int getNotify_num() {
        return this.notify_num;
    }

    public void setNotify_num(int notifyNum) {
        this.notify_num = notifyNum;
    }

    public String getReturnData() {
        return this.returnData;
    }

    public void setReturnData(String returnData) {
        this.returnData = returnData;
    }
}
