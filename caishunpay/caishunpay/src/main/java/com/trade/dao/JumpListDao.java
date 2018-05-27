/*
 * Decompiled with CFR 0_124.
 */
package com.trade.dao;

import com.manage.bean.PageModle;
import com.trade.bean.own.JumpListBean;
import java.util.List;
import java.util.Map;

public interface JumpListDao {
    public List<JumpListBean> getJumpList(String var1, String var2);

    public JumpListBean getFirstJumpList(String var1);

    public JumpListBean getJumpList(String var1, String var2, String var3, String var4);

    public void save(JumpListBean var1);

    public void update(JumpListBean var1);

    public void delet(JumpListBean var1);

    public PageModle<Map<String, String>> listGroupByPage(Map<String, String> var1, int var2, int var3);

    public List<Map<String, String>> listAllJumpgroup(Map<String, String> var1);
}
