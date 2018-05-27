/*
 * Decompiled with CFR 0_124.
 */
package com.manage.service;

import com.manage.bean.TradeMchtFile;

import java.util.List;

public interface TradeMchtFileService {
    public void save(TradeMchtFile var1);

    public void save(List<TradeMchtFile> var1);

    public TradeMchtFile get(String var1);

    public List<TradeMchtFile> getMchtFiles(String var1);

    public void update(TradeMchtFile var1);

    public void delete(TradeMchtFile var1);
}
