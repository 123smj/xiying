/*
 * Decompiled with CFR 0_124.
 */
package com.trade.bean;

import java.io.Serializable;

public class TradeDfDayamount
implements Serializable {
    private static final long serialVersionUID = 1L;
    private String bank_card_num;
    private String bank_name;
    private String real_name;
    private int money_amount;
    private String df_day;
    private String mcht_no;
    private String update_time;

    public String getBank_card_num() {
        return this.bank_card_num;
    }

    public void setBank_card_num(String bankCardNum) {
        this.bank_card_num = bankCardNum;
    }

    public String getBank_name() {
        return this.bank_name;
    }

    public void setBank_name(String bankName) {
        this.bank_name = bankName;
    }

    public String getReal_name() {
        return this.real_name;
    }

    public void setReal_name(String realName) {
        this.real_name = realName;
    }

    public int getMoney_amount() {
        return this.money_amount;
    }

    public void setMoney_amount(int moneyAmount) {
        this.money_amount = moneyAmount;
    }

    public String getDf_day() {
        return this.df_day;
    }

    public void setDf_day(String dfDay) {
        this.df_day = dfDay;
    }

    public String getMcht_no() {
        return this.mcht_no;
    }

    public void setMcht_no(String mchtNo) {
        this.mcht_no = mchtNo;
    }

    public String getUpdate_time() {
        return this.update_time;
    }

    public void setUpdate_time(String updateTime) {
        this.update_time = updateTime;
    }
}
