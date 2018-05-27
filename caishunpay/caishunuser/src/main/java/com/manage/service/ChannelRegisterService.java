/*
 * Decompiled with CFR 0_124.
 */
package com.manage.service;

import com.manage.bean.ChannelRegisterBean;
import com.trade.bean.own.QrcodeChannelInf;

public interface ChannelRegisterService {
    public String registerChannelMcht(QrcodeChannelInf var1);

    public String downLoadKey(QrcodeChannelInf var1);

    public String verifyInfo(ChannelRegisterBean var1);
}
