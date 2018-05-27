/*
 * Decompiled with CFR 0_124.
 */
package com.tuser.dao;

import com.tuser.bean.TradeMchtRoleFuncMap;
import com.tuser.bean.TradeRoleFuncMapPK;

public interface TradeRoleFuncMapDao {
    public void save(TradeMchtRoleFuncMap var1);

    public TradeMchtRoleFuncMap get(TradeRoleFuncMapPK var1);

    public void update(TradeMchtRoleFuncMap var1);

    public void delete(TradeMchtRoleFuncMap var1);
}
