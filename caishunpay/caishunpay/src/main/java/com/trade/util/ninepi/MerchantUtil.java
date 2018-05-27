///*
// * Decompiled with CFR 0_124.
// *
// * Could not load the following classes:
// *  net.sf.json.JSONArray
// *  net.sf.json.JSONObject
// *  org.apache.commons.lang3.StringUtils
// */
//package com.trade.util.ninepi;
//
//import com.trade.util.ninepi.HttpSendResult;
//import com.trade.util.ninepi.SimpleHttpsClient;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.PrintStream;
//import java.io.Writer;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
////import net.sf.json.JSONArray;
////import net.sf.json.JSONObject;
//
//import org.apache.commons.lang3.StringUtils;
//
//public class MerchantUtil {
//    public static void getChkFile(String url, String filePath) {
//        try {
//            File e = new File(filePath);
//            if (!e.exists()) {
//                e.createNewFile();
//            }
//            BufferedWriter bw = new BufferedWriter(new FileWriter(e));
//            String res = MerchantUtil.sendAndRecv(url, "", "GBK");
//            if (!StringUtils.isBlank((CharSequence)res)) {
//                bw.write(res);
//            }
//            bw.close();
//        }
//        catch (IOException var5) {
//            var5.printStackTrace();
//        }
//    }
//
//    public static String sendAndRecv(String url, String buf, String characterSet) throws IOException {
//        String charType = "00".equals(characterSet) ? "GBK" : ("01".equals(characterSet) ? "GB2312" : ("02".equals(characterSet) ? "UTF-8" : null));
//        String[] resArr = StringUtils.split((String)buf, (String)"&");
//        HashMap<String, String> reqMap = new HashMap<String, String>();
//        int httpsClient = 0;
//        while (httpsClient < resArr.length) {
//            String res = resArr[httpsClient];
//            int repMsg = StringUtils.indexOf((CharSequence)res, (int)61);
//            String nm = StringUtils.substring((String)res, (int)0, (int)repMsg);
//            String val = StringUtils.substring((String)res, (int)(repMsg + 1));
//            reqMap.put(nm, val);
//            ++httpsClient;
//        }
//        SimpleHttpsClient var11 = new SimpleHttpsClient();
//        HttpSendResult var12 = var11.postRequest(url, reqMap, 120000, charType);
//        String var13 = var12.getResponseBody();
//        return var13;
//    }
//
//    public static HashMap<String, String> toHashMap(Object object) {
//        HashMap<String, String> data = new HashMap<String, String>();
//        JSONObject jsonObject = JSONObject.fromObject((Object)object);
//        Iterator it = jsonObject.keys();
//        while (it.hasNext()) {
//            String key = String.valueOf(it.next());
//            String value = (String)jsonObject.get(key);
//            data.put(key, value);
//        }
//        return data;
//    }
//
//    public static void main(String[] args) {
//        JSONArray array = JSONArray.fromObject((Object)"[{\"id\":\"1\",\"txAmt\":\"10\",\"txType\":\"05\",\"ordTm\":\"20150301103526\"},{\"channelId\":\"2\",\"txAmt\":\"10\",\"txType\":\"05\",\"ordTm\":\"20150301103529\"},{\"channelId\":\"3\",\"txAmt\":\"10\",\"txType\":\"05\",\"ordTm\":\"20150301103532\"},{\"channelId\":\"4\",\"txAmt\":\"10\",\"txType\":\"05\",\"ordTm\":\"20150301103532\"}]");
//        List list = JSONArray.toList((JSONArray)array);
//        int i = 0;
//        while (i < list.size()) {
//            HashMap<String, String> map = MerchantUtil.toHashMap(list.get(i));
//            for (Map.Entry<String, String> entry1 : map.entrySet()) {
//                String tmp = entry1.getKey();
//                if (StringUtils.equals((CharSequence)tmp, (CharSequence)"id")) {
//                    tmp = "\u5e8f\u53f7";
//                } else if (StringUtils.equals((CharSequence)tmp, (CharSequence)"txAmt")) {
//                    tmp = "\u91d1\u989d";
//                } else if (StringUtils.equals((CharSequence)tmp, (CharSequence)"txType")) {
//                    tmp = "\u4ea4\u6613\u7c7b\u578b";
//                } else if (StringUtils.equals((CharSequence)tmp, (CharSequence)"ordTm")) {
//                    tmp = "\u65f6\u95f4";
//                }
//                System.out.print(String.valueOf(tmp) + ":" + entry1.getValue() + ";  ");
//            }
//            System.out.println("\n");
//            ++i;
//        }
//    }
//}
