/*
 * Decompiled with CFR 0_124.
 */
package com.tuser.dao;

import com.tuser.bean.TradeUserMenu;

import java.util.List;

public interface TradeUserMenuDao {
    public void save(TradeUserMenu var1);

    public TradeUserMenu get(Integer var1);

    public void update(TradeUserMenu var1);

    public void delete(TradeUserMenu var1);

    public List<TradeUserMenu> getFuncByOprDegree(Integer var1);
}
