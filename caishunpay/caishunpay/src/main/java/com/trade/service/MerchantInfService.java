/*
 * Decompiled with CFR 0_124.
 */
package com.trade.service;

import com.account.bean.TradeMchtAccount;
import com.manage.bean.OprInfo;
import com.manage.bean.PageModle;
import com.trade.bean.BankCardPay;
import com.trade.bean.own.MerchantInf;
import com.trade.bean.own.PayChannelInf;
import com.trade.enums.TradeSource;

import java.util.List;
import java.util.Map;

/***
 * 商户信息Service
 */
public interface MerchantInfService {
    public MerchantInf getMchtInfo(String var1);

    public PayChannelInf getChannelInf(String channelId, String channelMerchantNo);

    public PayChannelInf getChannelInfBalance(String var1, String var2);

    public PayChannelInf getChannelInf(MerchantInf merchantInf, TradeSource tradeSource);

    public void saveChannelInf(PayChannelInf var1);

    public void updateChannelInf(PayChannelInf var1);

    public void saveMchtInfo(MerchantInf var1);

    public void saveMchtInfo(MerchantInf var1, TradeMchtAccount var2);

    public void updateMchtInfo(MerchantInf var1);

    public PageModle<MerchantInf> listMchtInfoByPage(Map<String, String> var1, int var2, int var3, OprInfo var4);

    public List<MerchantInf> listAllMcht(Map<String, String> var1, OprInfo var2);

    public PageModle<PayChannelInf> listChannelMchtInfoByPage(Map<String, String> var1, int var2, int var3);

    public void updateQuickpayBean(BankCardPay var1);
}
