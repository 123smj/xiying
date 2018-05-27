/*
 * Decompiled with CFR 0_124.
 */
package com.trade.dao;

import com.manage.bean.OprInfo;
import com.manage.bean.PageModle;
import com.trade.bean.TradeDetailBean;
import com.trade.bean.WxJspayTradeInfo;
import com.trade.bean.ThirdPartyPayDetail;
import java.util.List;
import java.util.Map;

/**
 * 三方支付详细
 */
public interface ThirdPartyPayDetailDao {
    public void save(ThirdPartyPayDetail var1);

    public void saveOrUpdate(ThirdPartyPayDetail var1);

    public void update(ThirdPartyPayDetail var1);

    public ThirdPartyPayDetail getByTradesn(String var1, String var2);

    public ThirdPartyPayDetail getById(String var1);

    public ThirdPartyPayDetail queryByTokenid(String var1);

    public void saveJspay(WxJspayTradeInfo var1);

    public List<TradeDetailBean> listAll(Map<String, String> var1, OprInfo var2);

    public int acount(Map<String, String> var1, OprInfo var2);

    public PageModle<TradeDetailBean> listByPage(Map<String, String> var1, int var2, int var3, OprInfo var4);

    public List<ThirdPartyPayDetail> listNotifyFailTrade();
}
