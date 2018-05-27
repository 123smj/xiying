/*
 * Decompiled with CFR 0_124.
 */
package com.trade.service;

import com.trade.bean.BankCardPay;
import com.trade.bean.own.PayChannelInf;
import com.trade.bean.own.BankCardPayRequest;

import java.util.Map;

/***
 * 网银支付接口
 */
public interface BankCardPayService {
    BankCardPay doOrderCreate(BankCardPayRequest var1, PayChannelInf var2);

    BankCardPay doOrderQuery(BankCardPay var1, PayChannelInf var2);

    BankCardPay acceptBankCardPayNotify(Map<String, String> resultMap);
}
