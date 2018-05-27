/*
 * Decompiled with CFR 0_124.
 */
package com.trade.util.ninepi;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RequestUtil {
    public static Map<String, String> getMapFromRequestMap(Map<String, String[]> requestPara) {
        HashMap<String, String> paraMap = new HashMap<String, String>();
        for (Map.Entry<String, String[]> entry : requestPara.entrySet()) {
            String key = entry.getKey();
            String[] values = entry.getValue();
            if (values == null || values.length < 1) continue;
            paraMap.put(key, new String(values[0].getBytes(), Charset.forName("UTF-8")));
        }
        return paraMap;
    }
}
