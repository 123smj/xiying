/*
 * Decompiled with CFR 0_124.
 */
package com.trade.dao;

import com.trade.bean.own.JumpListBean;

import java.util.List;

public interface JumpListDao {
    public List<JumpListBean> getJumpList(String var1, String var2);

    public void save(JumpListBean var1);

    public void update(JumpListBean var1);

    public void delet(JumpListBean var1);
}
