/*
 * Decompiled with CFR 0_124.
 */
package com.trade.service;

import com.account.bean.TradeMchtAccount;
import com.manage.bean.OprInfo;
import com.manage.bean.PageModle;
import com.trade.bean.own.QrcodeChannelInf;
import com.trade.bean.own.QrcodeMchtInfo;

import java.util.List;
import java.util.Map;

public interface QrcodeMchtInfoService {
    public QrcodeMchtInfo getMchtInfo(String var1);

    public QrcodeChannelInf getChannelInf(String var1, String var2);

    public QrcodeChannelInf getChannelInfBalance(String var1, String var2);

    public void saveChannelInf(QrcodeChannelInf var1);

    public void updateChannelInf(QrcodeChannelInf var1);

    public void saveMchtInfo(QrcodeMchtInfo var1);

    public void saveMchtInfo(QrcodeMchtInfo var1, TradeMchtAccount var2);

    public void updateMchtInfo(QrcodeMchtInfo var1);

    public PageModle<QrcodeMchtInfo> listMchtInfoByPage(Map<String, String> var1, int var2, int var3, OprInfo var4);

    public List<QrcodeMchtInfo> listAllMcht(Map<String, String> var1, OprInfo var2);

    public PageModle<QrcodeChannelInf> listChannelMchtInfoByPage(Map<String, String> var1, int var2, int var3);
}
