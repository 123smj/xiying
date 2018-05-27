/*
 * Decompiled with CFR 0_124.
 */
package com.manage.service;

import com.manage.bean.ChannelRegisterBean;
import com.trade.bean.own.PayChannelInf;

import java.util.Map;

public interface ChannelRegisterService {
    public String registerChannelMcht(PayChannelInf var1);

    public String downLoadKey(PayChannelInf var1);

    public String verifyInfo(ChannelRegisterBean var1);

    public String notifyVerrifyInfo(Map<String, String> var1);
}
