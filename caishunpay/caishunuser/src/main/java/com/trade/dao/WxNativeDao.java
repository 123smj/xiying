/*
 * Decompiled with CFR 0_124.
 */
package com.trade.dao;

import com.manage.bean.OprInfo;
import com.manage.bean.PageModle;
import com.trade.bean.TradeDetailBean;
import com.trade.bean.WxJspayTradeInfo;
import com.trade.bean.WxpayScanCode;

import java.util.List;
import java.util.Map;

public interface WxNativeDao {
    public void save(WxpayScanCode var1);

    public void saveOrUpdate(WxpayScanCode var1);

    public void update(WxpayScanCode var1);

    public WxpayScanCode getByTradesn(String var1, String var2);

    public WxpayScanCode getById(String var1);

    public void saveJspay(WxJspayTradeInfo var1);

    public List<TradeDetailBean> listAll(Map<String, String> var1, OprInfo var2);

    public PageModle<TradeDetailBean> listByPage(Map<String, String> var1, int var2, int var3, OprInfo var4);

    public List<WxpayScanCode> listNotifyFailTrade();

    public int acount(Map<String, String> var1, OprInfo var2);
}
