/*
 * Decompiled with CFR 0_124.
 */
package com.trade.service;

import com.trade.bean.ThirdPartyPayDetail;
import com.trade.bean.own.MerchantInf;
import com.trade.bean.own.PayChannelInf;
import com.trade.bean.own.PayRequest;
import com.trade.enums.TradeSource;

import java.util.Map;
import java.util.Set;

/**
 * 三方支付接口
 */
public interface ThirdPartyPayService {
    /***
     * 发起支付实现此方法
     */
    ThirdPartyPayDetail doOrderCreate(PayRequest var1, MerchantInf var2, PayChannelInf var3);
    /**
     * 结果查询实现此方法
     */
    ThirdPartyPayDetail doOrderQuery(ThirdPartyPayDetail var1, PayChannelInf var2);
    /***
     * 接收支付结果通知实现此方法
     */
    ThirdPartyPayDetail acceptThirdPartyPayNotify(Map<String,String> resultMap);
    /**
     * 通过此接口，返回支持的接口类型
     */
    Set<TradeSource> supportedThirdPartyTradeSource ();
}
