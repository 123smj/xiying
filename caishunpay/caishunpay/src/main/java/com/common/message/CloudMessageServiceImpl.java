/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.springframework.stereotype.Component
 */
package com.common.message;

import com.gy.system.Environment;
import com.gy.util.DateUtil;
import com.gy.util.HttpUtility;
import com.gy.util.MD5Encrypt;
import com.gy.util.StringUtil;
import com.trade.util.Base64;
import com.trade.util.JsonUtil;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class CloudMessageServiceImpl {
    private static final String accountSid = "705d3efd03b66584789ab5e3fdfb29f5";
    private static final String authToken = "94c1330e9d3a8a935a903cafa9067966";
    private static final String appId = "b2488a4c0fdd4e2e8336a8fc1de0f0e8";
    public static final String templateId = "39645";
    private static final String apiUrl = "https://api.ucpaas.com/";

    public /* varargs */ String sendSingleMessage(String accountSid, String authToken, String appId, String phoneNum, String templateId, String ... msgContent) {
        if (StringUtil.isEmpty(phoneNum)) {
            return "param error";
        }
        String apiUrl = this.buildMessageApiUrl(accountSid, authToken, appId);
        HashMap<String, String> params = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        String[] arrstring = msgContent;
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String msg = arrstring[n2];
            sb.append(msg).append(",");
            ++n2;
        }
        params.put("param", sb.length() == 0 ? "" : sb.substring(0, sb.length() - 1));
        params.put("appId", appId);
        params.put("templateId", templateId);
        params.put("to", phoneNum);
        HashMap<String, HashMap<String, String>> sendParam = new HashMap<String, HashMap<String, String>>();
        sendParam.put("templateSMS", params);
        String jsonParam = JsonUtil.buildJson4Map(sendParam);
        Environment cloundMsgEnv = Environment.createEnvironment(apiUrl, "utf-8", "application/json");
        String authorizetion = String.valueOf(accountSid) + ":" + DateUtil.getCurrTime();
        cloundMsgEnv.addHeader("Authorization", Base64.encode(authorizetion.getBytes()));
        String backJson = HttpUtility.postData(cloundMsgEnv, jsonParam);
        System.out.println(backJson);
        Map backJsonMap = (Map)JsonUtil.parseJson(backJson);
        Map resultMap = (Map)backJsonMap.get("resp");
        if (StringUtil.isEmpty(backJson) || resultMap == null) {
            return "ERROR";
        }
        if ("000000".equals(resultMap.get("respCode"))) {
            return "SUCCESS";
        }
        return (String)resultMap.get("respCode");
    }

    public /* varargs */ String sendSingleMessage(String phoneNum, String templateId, String ... msgContent) {
        return this.sendSingleMessage("705d3efd03b66584789ab5e3fdfb29f5", "94c1330e9d3a8a935a903cafa9067966", "b2488a4c0fdd4e2e8336a8fc1de0f0e8", phoneNum, templateId, msgContent);
    }

    private String buildApiUrl(String accountSid, String authToken, String appId, String function, String operation) {
        return "https://api.ucpaas.com//2014-06-30/Accounts/" + accountSid + "/" + function + "/" + operation + "?sig=" + this.getSig(accountSid, authToken);
    }

    public String buildMessageApiUrl(String accountSid, String authToken, String appId) {
        return this.buildApiUrl(accountSid, authToken, appId, "Messages", "templateSMS");
    }

    public String getSig(String accountSid, String authToken) {
        String sig = "";
        try {
            sig = MD5Encrypt.getMessageDigest(String.valueOf(accountSid) + authToken + DateUtil.getCurrTime(), "utf-8");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return sig;
    }

    public static void main(String[] args) {
        String result = new CloudMessageServiceImpl().sendSingleMessage("15217928112", "38694", "0000");
        System.out.println(result);
    }
}
