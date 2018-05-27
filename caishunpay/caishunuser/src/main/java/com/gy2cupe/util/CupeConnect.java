/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 */
package com.gy2cupe.util;

import com.alibaba.fastjson.JSONObject;
import com.gy2cupe.bean.CupeResult;
import com.gy2cupe.util.CupeHttpUtil;
import com.gy2cupe.util.CupeUtil;

import java.util.Map;

public class CupeConnect {
    public static CupeResult getPay(Map<String, String> reqParam) throws Exception {
        return CupeConnect.commonConnect(reqParam, "http://vc.counect.com/vcupe/getPay.do");
    }

    public static CupeResult queryPay(Map<String, String> reqParam) throws Exception {
        return CupeConnect.commonConnect(reqParam, "http://vc.counect.com/vcupe/queryPay.do");
    }

    public static CupeResult commonConnect(Map<String, String> reqParam, String url) throws Exception {
        Map<String, String> sortedMap = CupeConnect.addSign(reqParam);
        String paramStr = CupeUtil.map2HttpParam(sortedMap);
        String result = CupeHttpUtil.postHttpRequest(url, paramStr);
        if (result == null || "".equals(result)) {
            return null;
        }
        return CupeConnect.parseResult(result);
    }

    private static Map<String, String> addSign(Map<String, String> reqParam) {
        Map<String, String> sortedMap = CupeUtil.sortMapByKey(reqParam);
        String signValue = CupeUtil.md5Sign(sortedMap);
        sortedMap.put("s", signValue);
        return sortedMap;
    }

    private static CupeResult parseResult(String result) {
        JSONObject jsonObject = (JSONObject) JSONObject.parse((String) result);
        CupeResult cupeResult = new CupeResult();
        cupeResult.setResult(jsonObject.getString("RESULT"));
        cupeResult.setBody(jsonObject.getObject("BODY", Object.class));
        return cupeResult;
    }

    public static void main(String[] args) {
        String str = "{\"RESULT\":0,\"BODY\":{\"p0\":\"\u9177\u8d1d\u7cfb\u7edf\u5185\u7684\u652f\u4ed8\u8ba2\u5355\u53f7\",\"p1\":\"\u652f\u4ed8\u65b9\u6cd5\",\"p2\":\"\u652f\u4ed8\u65f6\u95f4\",\"p3\":\"\u652f\u4ed8\u91d1\u989d\",\"p4\":\"\u6298\u6263\u91d1\u989d\",\"p5\":\"\u4ea4\u6613\u8ba2\u5355\u53f7\",\"p6\":\"\u9000\u6b3e\u91d1\u989d\",\"s\":\"\u8ba2\u5355\u72b6\u6001\"}}";
    }
}
