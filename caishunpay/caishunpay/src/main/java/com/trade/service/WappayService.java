/*
 * Decompiled with CFR 0_124.
 */
package com.trade.service;

import com.trade.bean.ThirdPartyPayDetail;
import com.trade.bean.own.MerchantInf;
import com.trade.bean.own.PayChannelInf;
import com.trade.bean.response.ThirdPartyPayResponse;

import javax.servlet.http.HttpServletRequest;

public interface WappayService {
    ThirdPartyPayResponse doWapTrade(HttpServletRequest request, ThirdPartyPayDetail thirdPartyPayDetail, MerchantInf var2, PayChannelInf var3);
}
