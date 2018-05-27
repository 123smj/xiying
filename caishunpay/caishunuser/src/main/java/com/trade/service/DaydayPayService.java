/*
 * Decompiled with CFR 0_124.
 */
package com.trade.service;

import com.trade.bean.TradeDfbean;
import com.trade.bean.WxpayScanCode;
import com.trade.bean.own.QrcodeChannelInf;
import com.trade.bean.own.TradeParam;
import com.trade.bean.response.ResponseCode;

import java.util.Map;

public interface DaydayPayService {
    public ResponseCode getDaypayQrCode(TradeParam var1, QrcodeChannelInf var2);

    public WxpayScanCode queryByTradesn(String var1, String var2, QrcodeChannelInf var3);

    public String saveResultNotify(Map var1);

    public TradeDfbean getDfBean(String var1);
}
