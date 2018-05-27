/*
 * Decompiled with CFR 0_124.
 */
package com.trade.service;

import com.trade.bean.QuickpayBean;
import com.trade.bean.own.QrcodeChannelInf;
import com.trade.bean.own.QuickpayParam;

import java.util.Map;

public interface NetpayService {
    public QuickpayBean netpayApply(QuickpayParam var1, QrcodeChannelInf var2);

    public QuickpayBean queryNetpay(String var1, String var2, QrcodeChannelInf var3);

    public QuickpayBean queryNetpayFromChannel(QuickpayBean var1, QrcodeChannelInf var2);

    public String saveNetpayNotify(Map<String, String> var1);

    public String getNetpayCallBack(Map<String, String> var1);
}
