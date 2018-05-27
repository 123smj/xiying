/*
 * Decompiled with CFR 0_124.
 */
package com.trade.service;

import com.trade.bean.WxpayScanCode;
import com.trade.bean.own.QrcodeChannelInf;
import com.trade.bean.own.TradeParam;
import com.trade.bean.response.ResponseCode;

import java.util.Map;

public interface CupeService {
    public ResponseCode doTrade(TradeParam var1, QrcodeChannelInf var2);

    public ResponseCode getWxjspay(TradeParam var1, QrcodeChannelInf var2);

    public String saveResultNotify(Map<String, String> var1);

    public WxpayScanCode queryTrade(String var1);

    public WxpayScanCode queryByTradesn(String var1, String var2, QrcodeChannelInf var3);
}
