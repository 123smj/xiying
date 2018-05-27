/*
 * Decompiled with CFR 0_124.
 */
package com.trade.dao;

import com.manage.bean.PageModle;
import com.trade.bean.BankCardPay;
import com.trade.bean.QuickpayQueryBean;

import java.util.List;
import java.util.Map;

/***
 * 银行卡支付账目数据库操作Interface
 */
public interface BankCardPayDao {
    public void save(BankCardPay var1);

    public void saveOrUpdate(BankCardPay var1);

    public void update(BankCardPay var1);

    public BankCardPay getByTradesn(String var1, String var2);

    public BankCardPay getById(String var1);

    public PageModle<QuickpayQueryBean> listByPage(Map<String, String> var1, int var2, int var3);

    public List<QuickpayQueryBean> listAll(Map<String, String> var1);

    public List<BankCardPay> listNotifyFailTrade();
}
