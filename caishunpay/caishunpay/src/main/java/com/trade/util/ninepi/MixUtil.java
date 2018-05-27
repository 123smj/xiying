/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.time.DateFormatUtils
 */
package com.trade.util.ninepi;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.time.DateFormatUtils;

public class MixUtil {
    public static Map<String, String> initHeader(String service, String payToken, String merchantId) {
        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("charset", "00");
        paramMap.put("version", "1.0");
        paramMap.put("merchantId", merchantId);
        paramMap.put("requestTime", DateFormatUtils.format((Date)new Date(), (String)"yyyyMMddHHmmss"));
        paramMap.put("requestId", String.valueOf(System.currentTimeMillis()));
        paramMap.put("service", service);
        paramMap.put("signType", "MD5");
        paramMap.put("payToken", payToken);
        return paramMap;
    }
}
