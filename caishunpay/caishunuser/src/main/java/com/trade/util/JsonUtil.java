/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  com.google.gson.Gson
 */
package com.trade.util;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import java.util.Map;
import java.util.Set;

public class JsonUtil {
    public static String buildJson(Object object) {
        Object jsonObject = JSONObject.toJSON((Object) object);
        return jsonObject.toString();
    }

    public static String buildJson4Map(Map map) {
        Object jsonObject = JSONObject.toJSON((Object) map);
        return jsonObject.toString();
    }

    public static String buildSortedJson(Map<String, String> map) {
        StringBuilder sBuilder = new StringBuilder("{");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sBuilder.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\"").append(",");
        }
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        sBuilder.append("}");
        return sBuilder.toString();
    }

    public static Object parseJson(String jsonStr) {
        return JSONObject.parse((String) jsonStr);
    }

    public static Map<String, Object> gsonParseJson(String jsonStr) {
        Gson gson = new Gson();
        Map paraMap = (Map) gson.fromJson(jsonStr, Map.class);
        return paraMap;
    }
}
