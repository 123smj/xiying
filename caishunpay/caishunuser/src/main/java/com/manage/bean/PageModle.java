/*
 * Decompiled with CFR 0_124.
 */
package com.manage.bean;

import java.util.List;

public class PageModle<T> {
    private int totalPage;
    private int totalNum;
    private int currentPage;
    private int perPageNum;
    private double totalAmount;
    private double totalSingleFee;
    private double totalFee;
    private double accountNum;
    private List<T> data;

    public int getTotalPage() {
        return this.totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalNum() {
        return this.totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPerPageNum() {
        return this.perPageNum;
    }

    public void setPerPageNum(int perPageNum) {
        this.perPageNum = perPageNum;
    }

    public List<T> getData() {
        return this.data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public double getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getTotalFee() {
        return this.totalFee;
    }

    public void setTotalFee(double totalFee) {
        this.totalFee = totalFee;
    }

    public double getAccountNum() {
        return this.accountNum;
    }

    public void setAccountNum(double accountNum) {
        this.accountNum = accountNum;
    }

    public double getTotalSingleFee() {
        return this.totalSingleFee;
    }

    public void setTotalSingleFee(double totalSingleFee) {
        this.totalSingleFee = totalSingleFee;
    }
}
