/*
 * Decompiled with CFR 0_124.
 */
package com.manage.dao;

import com.manage.bean.PageModle;
import com.manage.bean.RateInfo;
import java.util.Map;

public interface RateInfoDao {
    public void save(RateInfo var1);

    public RateInfo get(String var1);

    public void update(RateInfo var1);

    public void delete(RateInfo var1);

    public PageModle<Map<String, String>> listCompanyInfoByPage(Map<String, String> var1, int var2, int var3);
}
