/*
 * Decompiled with CFR 0_124.
 */
package com.manage.service;

import com.manage.bean.MenuBean;
import com.tuser.bean.TradeUserMenu;

import java.util.List;

public interface TradeUserMenuService {
    public TradeUserMenu get(Integer var1);

    public List<TradeUserMenu> getFuncByOprDegree(Integer var1);

    public List<MenuBean> build(List<TradeUserMenu> var1);
}
