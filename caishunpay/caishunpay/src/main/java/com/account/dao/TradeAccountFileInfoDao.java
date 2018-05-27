/*
 * Decompiled with CFR 0_124.
 */
package com.account.dao;

import com.account.bean.TradeAccountFileInfo;
import com.manage.bean.PageModle;
import java.util.Map;

public interface TradeAccountFileInfoDao {
    public TradeAccountFileInfo get(Integer var1);

    public void update(TradeAccountFileInfo var1);

    public void save(TradeAccountFileInfo var1);

    public PageModle<TradeAccountFileInfo> listAccountFileByPage(Map<String, String> var1, int var2, int var3);
}
