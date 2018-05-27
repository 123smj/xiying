/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  org.apache.commons.lang3.StringUtils
 */
package com.trade.util.ninepi;

import com.alibaba.fastjson.JSONObject;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class StringMapUtil {
    public static String changeMapToString(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
            sb.append("&");
        }
        return sb.substring(0, sb.length() - 1);
    }

    public static Map<String, String> changeStringToMap(String result) {
        String[] resArr = StringUtils.split((String)result, (String)"&");
        HashMap<String, String> map = new HashMap<String, String>();
        String[] arrstring = resArr;
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String data = arrstring[n2];
            int index = StringUtils.indexOf((CharSequence)data, (int)61);
            String key = StringUtils.substring((String)data, (int)0, (int)index);
            String value = StringUtils.substring((String)data, (int)(index + 1));
            map.put(key, value);
            ++n2;
        }
        return map;
    }

    public static Map<String, String> stringToDataFieldMap(String str) {
//        JSONObject value;
//        JSONObject jsonObject;
//        JSONObject map = jsonObject = JSONObject.parseObject((String)str);
//        JSONObject map2 = value = (JSONObject)map.get("data");
//        for (Map.Entry entry : map2.entrySet()) {
//            System.out.println(String.valueOf((String)entry.getKey()) + ":" + (String)entry.getValue());
//        }
//        return map2;
        return null;
    }

    public static Map<String, String> stringToErrorFieldMap(String str) {
//        JSONObject value;
//        JSONObject jsonObject;
//        JSONObject map = jsonObject = JSONObject.parseObject((String)str);
//        JSONObject map2 = value = (JSONObject)map.get("error");
//        for (Map.Entry entry : map2.entrySet()) {
//            System.out.println(String.valueOf((String)entry.getKey()) + ":" + (String)entry.getValue());
//        }
//        return map2;
        return null;
    }
}
