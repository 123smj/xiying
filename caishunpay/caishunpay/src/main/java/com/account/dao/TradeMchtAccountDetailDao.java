/*
 * Decompiled with CFR 0_124.
 */
package com.account.dao;

import com.account.bean.TradeMchtAccountDetail;
import com.manage.bean.PageModle;
import java.util.List;
import java.util.Map;

public interface TradeMchtAccountDetailDao {
    public TradeMchtAccountDetail get(String var1);

    public TradeMchtAccountDetail getByDfsn(String var1, String var2);

    public void update(TradeMchtAccountDetail var1);

    public void save(TradeMchtAccountDetail var1);

    public List<TradeMchtAccountDetail> listUnSuccessDf();

    public PageModle<TradeMchtAccountDetail> listMchtAccountDetailByPage(Map<String, String> var1, String var2, int var3, int var4);

    public List<TradeMchtAccountDetail> listAllAccountDetail(Map<String, String> var1);
}
