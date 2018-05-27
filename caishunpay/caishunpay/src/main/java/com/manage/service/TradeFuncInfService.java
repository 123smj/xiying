/*
 * Decompiled with CFR 0_124.
 */
package com.manage.service;

import com.manage.bean.MenuBean;
import com.manage.bean.TradeFuncInf;
import java.util.HashSet;
import java.util.List;

public interface TradeFuncInfService {
    public TradeFuncInf get(Integer var1);

    public List<TradeFuncInf> getFuncByOprDegree(Integer var1);

    public List<MenuBean> build(List<TradeFuncInf> var1);

    public HashSet<String> getAuthSet(List<TradeFuncInf> var1);
}
