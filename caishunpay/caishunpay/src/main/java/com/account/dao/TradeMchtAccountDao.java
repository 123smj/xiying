/*
 * Decompiled with CFR 0_124.
 */
package com.account.dao;

import com.account.bean.TradeMchtAccount;
import com.manage.bean.PageModle;
import java.util.Map;

public interface TradeMchtAccountDao {
    public TradeMchtAccount get(String var1);

    public void update(TradeMchtAccount var1);

    public void save(TradeMchtAccount var1);

    public PageModle<TradeMchtAccount> listMchtAccountByPage(Map<String, String> var1, int var2, int var3);
}
