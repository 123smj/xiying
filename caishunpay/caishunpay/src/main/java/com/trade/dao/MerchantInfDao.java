/*
 * Decompiled with CFR 0_124.
 */
package com.trade.dao;

import com.manage.bean.OprInfo;
import com.manage.bean.PageModle;
import com.trade.bean.own.MerchantInf;
import com.trade.bean.own.MerchantInf;
import com.trade.bean.own.PayChannelInf;

import java.util.List;
import java.util.Map;

/**
 * 商人信息
 */
public interface MerchantInfDao {
    public MerchantInf getMchtInfo(String var1);

    public PayChannelInf getChannelInf(String channelId, String var2);

    public void saveChannelInf(PayChannelInf var1);

    public void updateChannelInf(PayChannelInf var1);

    public void saveMchtInfo(MerchantInf var1);

    public void updateMchtInfo(MerchantInf var1);

    public PageModle<MerchantInf> listMchtInfoByPage(Map<String, String> var1, int var2, int var3, OprInfo var4);

    public List<MerchantInf> listAllMcht(Map<String, String> var1, OprInfo var2);

    public List<Map<String, String>> listAllMchtMap(Map<String, String> var1, OprInfo var2);

    public PageModle<PayChannelInf> listChannelMchtInfoByPage(Map<String, String> var1, int var2, int var3);
}
