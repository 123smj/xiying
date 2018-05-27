/*
 * Decompiled with CFR 0_124.
 */
package com.account.bean;

public class TradeBranchAccount {
    private String branchNo;
    private int balance;
    private int cashMin;
    private int cashMax;
    private String status;
    private String updateTime;
    private int version;

    public String getBranchNo() {
        return this.branchNo;
    }

    public void setBranchNo(String branchNo) {
        this.branchNo = branchNo;
    }

    public int getBalance() {
        return this.balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getCashMin() {
        return this.cashMin;
    }

    public void setCashMin(int cashMin) {
        this.cashMin = cashMin;
    }

    public int getCashMax() {
        return this.cashMax;
    }

    public void setCashMax(int cashMax) {
        this.cashMax = cashMax;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
