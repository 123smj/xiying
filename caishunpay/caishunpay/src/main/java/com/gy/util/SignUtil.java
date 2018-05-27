/*
 * Decompiled with CFR 0_124.
 */
package com.gy.util;

import com.gy2cupe.util.CupeUtil;
import java.util.Map;

public class SignUtil {
    public static boolean checkSign(Map<String, String> param, String sign) {
        Map<String, String> sortedMap = CupeUtil.sortMapByKey(param);
        String signValue = CupeUtil.md5Sign(sortedMap);
        return signValue.equals(sign);
    }
}
