/*
 * Decompiled with CFR 0_124.
 */
package com.trade.service;

import com.manage.bean.OprInfo;
import com.manage.bean.PageModle;
import com.trade.bean.NativeNotifyResultBean;
import com.trade.bean.TradeDetailBean;
import com.trade.bean.WxpayScanCode;
import com.trade.service.TradeService;

import java.util.List;
import java.util.Map;

public interface WxpayService
        extends TradeService {
    public String saveResultNotify(NativeNotifyResultBean var1);

    public WxpayScanCode queryTrade(String var1);

    public List<TradeDetailBean> listAll(Map<String, String> var1, OprInfo var2);

    public int acount(Map<String, String> var1, OprInfo var2);

    public PageModle<TradeDetailBean> listByPage(Map<String, String> var1, int var2, int var3, OprInfo var4);
}
