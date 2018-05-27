/*
 * Decompiled with CFR 0_124.
 */
package com.trade.service;

import com.account.bean.TradeMchtAccount;
import com.manage.bean.OprInfo;
import com.manage.bean.PageModle;
import com.trade.bean.own.QrcodeMchtInfoTmp;
import java.util.List;
import java.util.Map;

public interface QrcodeMchtInfoTmpService {
    public QrcodeMchtInfoTmp getMchtInfo(String var1);

    public void saveMchtInfo(QrcodeMchtInfoTmp var1, TradeMchtAccount var2);

    public void updateMchtInfo(QrcodeMchtInfoTmp var1);

    public PageModle<QrcodeMchtInfoTmp> listMchtInfoTmpByPage(Map<String, String> var1, int var2, int var3, OprInfo var4);

    public PageModle<QrcodeMchtInfoTmp> listMchtInfoTmp4CheckByPage(Map<String, String> var1, int var2, int var3, OprInfo var4);

    public List<QrcodeMchtInfoTmp> listAllMcht(Map<String, String> var1, OprInfo var2);
}
