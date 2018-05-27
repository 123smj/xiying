/*
 * Decompiled with CFR 0_124.
 */
package com.trade.service;

import com.manage.bean.PageModle;
import com.trade.bean.QuickpayBean;
import com.trade.bean.QuickpayQueryBean;
import com.trade.bean.own.QrcodeChannelInf;
import com.trade.bean.own.QuickpayParam;
import com.trade.bean.response.QuickpayResponse;

import java.util.List;
import java.util.Map;

public interface QuickpayService {
    public QuickpayResponse prePay(QuickpayParam var1, QrcodeChannelInf var2);

    public QuickpayResponse checkPay(QuickpayParam var1, QrcodeChannelInf var2);

    public QuickpayResponse queryPay(QuickpayParam var1, QrcodeChannelInf var2);

    public QuickpayBean doPrepay(QuickpayParam var1, QrcodeChannelInf var2);

    public QuickpayBean doCheckpay(QuickpayBean var1, QrcodeChannelInf var2, String var3);

    public QuickpayBean doQuerypay(QuickpayBean var1, QrcodeChannelInf var2);

    public String saveResultNotify(Map<String, String> var1);

    public PageModle<QuickpayQueryBean> listByPage(Map<String, String> var1, int var2, int var3);

    public List<QuickpayQueryBean> listAll(Map<String, String> var1);
}
