/*
 * Decompiled with CFR 0_124.
 */
package com.account.service;

import com.account.bean.TradeMchtAccountDetail;
import com.trade.bean.own.DfParam;
import com.trade.bean.own.MerchantInf;
import com.trade.enums.ResponseEnum;

public interface DaifuService {
    //单笔支付
    public ResponseEnum singlePay(TradeMchtAccountDetail var1, MerchantInf var2);
    //执行一个支付
    public ResponseEnum doSinglePay(TradeMchtAccountDetail var1, MerchantInf var2);
    //存在疑问支付
    public TradeMchtAccountDetail querySinglePay(DfParam var1);
}
