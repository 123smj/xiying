/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  org.apache.commons.lang3.RandomStringUtils
 */
package com.gy2cupe.util;

import com.alibaba.fastjson.JSONObject;
import com.gy.util.MD5Encrypt;
import com.gy.util.MapKeyComparator;
import com.gy.util.MapValueComparator;
import com.gy2cupe.bean.CupeResult;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.RandomStringUtils;

public class CupeUtil {
    public static final Map<String, String> ORDER_STATUS = new HashMap<String, String>() {
        {
            this.put("0", "\u5f85\u652f\u4ed8");
            this.put("1", "\u652f\u4ed8\u6210\u529f");
            this.put("2", "\u652f\u4ed8\u5931\u8d25");
        }
    };

    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        TreeMap<String, String> sortMap = new TreeMap<String, String>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }

    public static String md5Sign(Map<String, String> param) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : param.entrySet()) {
            sb.append(entry.getValue());
        }
        sb.append("C0D26A23A03343E695D480D254C932FB");
        String signValue = null;
        try {
            signValue = MD5Encrypt.getMessageDigest(sb.toString(), new String[0]).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return signValue;
    }

    public static String map2HttpParam(Map<String, String> param) {
        StringBuffer sb = new StringBuffer();
        boolean isFirst = true;
        for (Map.Entry<String, String> entry : param.entrySet()) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append("&");
            }
            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return sb.toString();
    }

    public static String getRandom(int length) {
        return RandomStringUtils.randomAlphanumeric((int) length);
    }

    public static String fillString(String str, char fill, int len, boolean isfillEnd) {
        if (str == null) {
            str = "";
        }
        int fillLen = len - str.getBytes().length;
        if (len <= 0) {
            return str;
        }
        if (fillLen <= 0) {
            str = str.substring(str.getBytes().length - len);
        } else {
            int i = 0;
            while (i < fillLen) {
                str = isfillEnd ? String.valueOf(str) + fill : String.valueOf(fill) + str;
                ++i;
            }
        }
        return str;
    }

    public static Map<String, String> sortMapByValue(Map<String, String> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        LinkedHashMap<String, String> sortedMap = new LinkedHashMap<String, String>();
        ArrayList<Map.Entry<String, String>> entryList = new ArrayList<Map.Entry<String, String>>(oriMap.entrySet());
        Collections.sort(entryList, new MapValueComparator());
        Iterator<Map.Entry<String, String>> iter = entryList.iterator();
        Map.Entry<String, String> tmpEntry = null;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }
        return sortedMap;
    }

    public static String buildCupeReturn(String code, Object value) {
        CupeResult cupeResult = new CupeResult();
        cupeResult.setResult(code);
        cupeResult.setBody(value);
        Object jsonObject = JSONObject.toJSON((Object) cupeResult);
        return jsonObject.toString();
    }

    public static String toString(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    public static void main(String[] args) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("tradeSnOri", "201606027311851102");
        Map<String, String> sortedMap = CupeUtil.sortMapByKey(map);
        String sign = CupeUtil.md5Sign(sortedMap);
        System.out.println(sign);
    }
}
