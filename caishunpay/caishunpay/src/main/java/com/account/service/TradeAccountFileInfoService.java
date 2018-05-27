/*
 * Decompiled with CFR 0_124.
 */
package com.account.service;

import com.account.bean.TradeAccountFileDetail;
import com.account.bean.TradeAccountFileInfo;
import com.manage.bean.PageModle;
import java.util.List;
import java.util.Map;

public interface TradeAccountFileInfoService {
    public TradeAccountFileInfo getAccountFileInfo(Integer var1);

    public void update(TradeAccountFileInfo var1);

    public void save(TradeAccountFileInfo var1);

    public PageModle<TradeAccountFileInfo> listAccountFileByPage(Map<String, String> var1, int var2, int var3);

    public void update(TradeAccountFileDetail var1);

    public void save(TradeAccountFileDetail var1);

    public void saveDetails(List<TradeAccountFileDetail> var1);

    public PageModle<TradeAccountFileDetail> listDetailByPage(Map<String, String> var1, int var2, int var3);

    public String checkAccept(Integer var1);

    public String checkRefuse(Integer var1, String var2);
}
