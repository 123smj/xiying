/*
 * Decompiled with CFR 0_124.
 */
package com.manage.service;

import com.manage.bean.OprInfo;
import com.manage.bean.PageModle;
import com.manage.bean.RateInfo;
import java.util.Map;

public interface OprInfoService {
    public void save(OprInfo var1);

    public void save(OprInfo var1, RateInfo var2);

    public OprInfo get(String var1);

    public RateInfo getRate(String var1);

    public void update(OprInfo var1);

    public void update(OprInfo var1, RateInfo var2);

    public void delete(OprInfo var1);

    public PageModle<Map<String, String>> listCompanyInfoByPage(Map<String, String> var1, int var2, int var3);
}
