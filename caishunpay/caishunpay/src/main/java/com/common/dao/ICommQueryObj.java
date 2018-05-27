/*
 * Decompiled with CFR 0_124.
 */
package com.common.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface ICommQueryObj {
    public List find(String var1);

    public List findByNamedQuery(String var1);

    public List findByNamedQuery(String var1, int var2, int var3);

    public List findByNamedQuery(String var1, Map var2);

    public List findByNamedQuery(String var1, Map var2, int var3, int var4);

    public List findBySQLQuery(String var1, int var2, int var3);

    public void excute(String var1);

    public String findCountBySQLQuery(String var1);

    public List findBySQLQuery(String var1);

    public List findBySQLQuery(String var1, Map var2);

    public String findCountBySQLQuery(String var1, Map var2);

    public List findBySQLQuery(String var1, int var2, int var3, Map var4);

    public List findByNamedQuery(String var1, Serializable[] var2);

    public List findByNamedQuery(String var1, Serializable[] var2, int var3, int var4);

    public List findByHQLQuery(String var1, int var2, int var3);

    public List findByHQLQuery(String var1);
}
