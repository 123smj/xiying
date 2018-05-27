/*
 * Decompiled with CFR 0_124.
 */
package com.manage.dao;

import com.manage.bean.TradeFuncInf;
import java.util.List;

public interface TradeFuncInfDao {
    public void save(TradeFuncInf var1);

    public TradeFuncInf get(Integer var1);

    public void update(TradeFuncInf var1);

    public void delete(TradeFuncInf var1);

    public List<TradeFuncInf> getFuncByOprDegree(Integer var1);
}
