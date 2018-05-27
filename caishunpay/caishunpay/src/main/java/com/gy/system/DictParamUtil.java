/*
 * Decompiled with CFR 0_124.
 */
package com.gy.system;

import java.util.ArrayList;
import java.util.List;

public class DictParamUtil {
    public static List<String> whiteIpList = new ArrayList<String>();

    public static boolean isWhiteIp(String ip) {
        return whiteIpList.contains(ip);
    }
}
