/*
 * Decompiled with CFR 0_124.
 */
package com.account.dao;

import com.account.bean.TradeAccountFileDetail;
import com.manage.bean.PageModle;
import java.util.List;
import java.util.Map;

public interface TradeAccountFileDetailDao {
    public void update(TradeAccountFileDetail var1);

    public void save(TradeAccountFileDetail var1);

    public TradeAccountFileDetail get(String var1, String var2);

    public List<TradeAccountFileDetail> getAccoutFileDetailList(Integer var1);

    public PageModle<TradeAccountFileDetail> listDetailByPage(Map<String, String> var1, int var2, int var3);
}
