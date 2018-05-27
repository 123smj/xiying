/*
 * Decompiled with CFR 0_124.
 */
package com.trade.util;

import java.io.PrintStream;
import java.security.MessageDigest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Safe {
    static String[] strDigits = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static String md5(String txt) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(txt.getBytes("UTF-8"));
            StringBuffer buff = new StringBuffer();
            int i = 0;
            while (i < bytes.length) {
                int bt = bytes[i];
                if (bt < 0) {
                    bt += 256;
                }
                int iD1 = bt / 16;
                int iD2 = bt % 16;
                buff.append(strDigits[iD1]).append(strDigits[iD2]);
                ++i;
            }
            return buff.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String sign(LinkedHashMap<String, String> params, String key) {
        try {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            StringBuffer buff = new StringBuffer();
            for (Map.Entry<String, String> entry : entrySet) {
                buff.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            buff.append(key);
            System.out.println("huika_sign:" + buff);
            return Safe.md5(buff.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
