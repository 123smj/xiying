/*
 * Decompiled with CFR 0_124.
 */
package com.trade.dao;

import com.manage.bean.PageModle;
import com.trade.bean.QuickpayBean;
import com.trade.bean.QuickpayQueryBean;

import java.util.List;
import java.util.Map;

public interface QuickpayDao {
    public void save(QuickpayBean var1);

    public void saveOrUpdate(QuickpayBean var1);

    public void update(QuickpayBean var1);

    public QuickpayBean getByTradesn(String var1, String var2);

    public QuickpayBean getById(String var1);

    public PageModle<QuickpayQueryBean> listByPage(Map<String, String> var1, int var2, int var3);

    public List<QuickpayQueryBean> listAll(Map<String, String> var1);

    public List<QuickpayBean> listNotifyFailTrade();
}
