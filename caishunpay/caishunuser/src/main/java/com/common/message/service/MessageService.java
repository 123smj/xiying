/*
 * Decompiled with CFR 0_124.
 */
package com.common.message.service;

import com.common.message.TradeMchtYzm;
import com.common.model.Response;

public interface MessageService {
    public boolean checkYzmTime(TradeMchtYzm var1);

    public TradeMchtYzm getYzm(String var1);

    public void save(TradeMchtYzm var1);

    public void update(TradeMchtYzm var1);

    public /* varargs */ Response<String> sendSingleMessage(String var1, String var2, String... var3);
}
