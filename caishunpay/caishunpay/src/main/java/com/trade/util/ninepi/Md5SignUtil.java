/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.trade.util.ninepi;

import java.io.PrintStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Md5SignUtil {
    private static final Logger logger = LoggerFactory.getLogger(Md5SignUtil.class);
    private static char[] Digit = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String md5(String str) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(str.getBytes("UTF-8"));
        }
        catch (Exception ex) {
            logger.error("\u5f02\u5e38-", (Throwable)ex);
            throw new RuntimeException(ex.getMessage());
        }
        byte[] encodedValue = md5.digest();
        int j = encodedValue.length;
        char[] finalValue = new char[j * 2];
        int k = 0;
        int i = 0;
        while (i < j) {
            byte encoded = encodedValue[i];
            finalValue[k++] = Digit[encoded >> 4 & 15];
            finalValue[k++] = Digit[encoded & 15];
            ++i;
        }
        return new String(finalValue);
    }

    public static String md5(Map<String, String> paramMap, String key) {
        TreeMap<String, String> signMap = new TreeMap<String, String>(paramMap);
        StringBuilder builder = new StringBuilder();
        for (String k : signMap.keySet()) {
            builder.append(signMap.get(k)).append("|");
        }
        builder.append(key);
        String reqParam = builder.toString();
        logger.info("reqParam:" + reqParam);
        return Md5SignUtil.md5(reqParam);
    }

    public static void main(String[] arg) throws Exception {
        HashMap<String, String> routerParaMap = new HashMap<String, String>();
        routerParaMap.put("amount", "1000");
        routerParaMap.put("autoTime", "20170315");
        routerParaMap.put("orderId", "10002000300040066");
        routerParaMap.put("orderSts", "S");
        routerParaMap.put("coreOrderId", "170223130060021552");
        routerParaMap.put("checkDate", "20170223");
        routerParaMap.put("txDesc", "");
        System.out.println("sign:" + Md5SignUtil.md5Sign(routerParaMap, "aa", "GB18030", null));
    }

    public static String md5Sign(Map<String, String> params, String signKey) {
        return Md5SignUtil.md5Sign(params, signKey, "UTF-8", null);
    }

    public static String md5Sign(Map<String, String> params, String signKey, String charset, String connector) {
        params = Md5SignUtil.removeNullFromMap(params);
        if (StringUtils.isBlank((CharSequence)connector)) {
            connector = "&";
        }
        ArrayList<Map.Entry<String, String>> entryList = new ArrayList<Map.Entry<String, String>>(params.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<String, String>>(){

            @Override
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return (String.valueOf(o1.getKey()) + "=" + o1.getValue()).compareTo(String.valueOf(o2.getKey()) + "=" + o2.getValue());
            }
        });
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : entryList) {
            String value;
            if (sb.length() > 0) {
                sb.append(connector);
            }
            if (StringUtils.isEmpty((CharSequence)(value = entry.getValue()).trim())) continue;
            sb.append(entry.getKey()).append("=").append(value);
        }
        sb.append(signKey);
        logger.debug("md5Sign sb:{}", (Object)sb.toString());
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return Md5SignUtil.byte2hexString(md.digest(sb.toString().getBytes(charset)));
        }
        catch (Exception ex) {
            throw new RuntimeException("md5 sign error !", ex);
        }
    }

    public static Map<String, String> removeNullFromMap(Map<String, String> map) {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue() == null || "".equals(entry.getValue()) || "null".equalsIgnoreCase(entry.getValue())) continue;
            resultMap.put(entry.getKey(), entry.getValue());
        }
        return resultMap;
    }

    public static String byte2hexString(byte[] b) {
        StringBuilder sb = new StringBuilder("");
        String stmp = "";
        int n = 0;
        while (n < b.length) {
            stmp = Integer.toHexString(b[n] & 255);
            if (stmp.length() == 1) {
                sb.append("0").append(stmp);
            } else {
                sb.append(stmp);
            }
            ++n;
        }
        return sb.toString();
    }

}
