/*
 * Decompiled with CFR 0_124.
 */
package com.tuser.dao;

import com.manage.bean.TradeMchtFile;

import java.util.List;

public interface TradeMchtFileDao {
    public void save(TradeMchtFile var1);

    public TradeMchtFile get(String var1);

    public List<TradeMchtFile> getMchtFiles(String var1);

    public void update(TradeMchtFile var1);

    public void delete(TradeMchtFile var1);
}
