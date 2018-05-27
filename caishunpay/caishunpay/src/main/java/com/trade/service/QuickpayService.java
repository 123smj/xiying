/*
 * Decompiled with CFR 0_124.
 */
package com.trade.service;

import com.manage.bean.PageModle;
import com.trade.bean.BankCardPay;
import com.trade.bean.QuickpayQueryBean;
import com.trade.bean.own.BankCardPayRequest;
import com.trade.bean.own.MerchantInf;
import com.trade.bean.own.PayChannelInf;
import com.trade.bean.response.BankPayResponse;
import java.util.List;
import java.util.Map;

/***
 * 对接快捷支付通道实现此接口
 */
public interface QuickpayService {
    /***
     * 交易发起
     * @param var1 交易发起参数
     * @param var2 商户信息
     * @param var3 通道信息
     */
    public BankPayResponse prePay(BankCardPayRequest var1, MerchantInf var2, PayChannelInf var3);

    /***
     * 短信验证接口
     */
    public BankPayResponse checkPay(BankCardPayRequest var1, PayChannelInf var2);

    /***
     * 交易查询接口
     * @param var1
     * @param var2
     * @return
     */
    public BankPayResponse queryPay(BankCardPayRequest var1, PayChannelInf var2);

    public BankCardPay doPrepay(BankCardPayRequest var1, PayChannelInf var2);

    public BankCardPay doCheckpay(BankCardPay var1, PayChannelInf var2, String var3);

    public BankCardPay doQuerypay(BankCardPay var1, PayChannelInf var2);

    /***
     * 回调处理接口
     * @param var1
     * @return
     */
    public String saveResultNotify(Map<String, String> var1);

    public PageModle<QuickpayQueryBean> listByPage(Map<String, String> var1, int var2, int var3);

    public List<QuickpayQueryBean> listAll(Map<String, String> var1);
}
