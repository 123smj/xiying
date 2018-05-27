/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.commons.codec.binary.Base64
 *  org.apache.http.HttpEntity
 *  org.apache.http.client.entity.UrlEncodedFormEntity
 *  org.apache.http.client.methods.CloseableHttpResponse
 *  org.apache.http.client.methods.HttpPost
 *  org.apache.http.client.methods.HttpUriRequest
 *  org.apache.http.conn.ClientConnectionManager
 *  org.apache.http.conn.routing.HttpRoutePlanner
 *  org.apache.http.conn.scheme.SchemeRegistry
 *  org.apache.http.conn.ssl.AllowAllHostnameVerifier
 *  org.apache.http.conn.ssl.SSLSocketFactory
 *  org.apache.http.conn.ssl.X509HostnameVerifier
 *  org.apache.http.impl.client.DefaultHttpClient
 *  org.apache.http.impl.conn.ProxySelectorRoutePlanner
 *  org.apache.http.message.BasicNameValuePair
 *  org.apache.http.params.HttpParams
 *  org.apache.http.util.EntityUtils
 */
package com.trade.util.yaku;

import com.gy.util.StringUtil;
import com.trade.util.yaku.RSAUtil;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.ProxySelector;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.ProxySelectorRoutePlanner;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

public class Tools {
    private static String EQUALS = "equals";
    private static String EQUALSIGNORECASE = "equalsIgnoreCase";
    private static String ENDSWITH = "endsWith";
    private static String STARTSWITH = "startsWith";
    private static String CONTAINS = "contains";
    private HashMap<String, String> header = new HashMap();

    public String post(String url, String request) {
        String returnString;
        returnString = "";
        DefaultHttpClient client = new DefaultHttpClient();
        ProxySelectorRoutePlanner routePlanner = new ProxySelectorRoutePlanner(client.getConnectionManager().getSchemeRegistry(), ProxySelector.getDefault());
        client.setRoutePlanner((HttpRoutePlanner)routePlanner);
        client.getParams().setParameter("http.connection.timeout", (Object)60000);
        client.getParams().setParameter("http.socket.timeout", (Object)60000);
        HttpPost method = new HttpPost(url);
        method.setHeader("Content-Type", "application/x-www-form-urlencoded");
        if (!this.header.isEmpty()) {
            Set<String> keysSet = this.header.keySet();
            for (String key : keysSet) {
                String value = this.header.get(key);
                method.addHeader(key, value);
            }
        }
        try {
            try {
                if (!request.equals("")) {
                    String[] values;
                    ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
                    String[] arrstring = values = request.split("&");
                    int n = arrstring.length;
                    int value = 0;
                    while (value < n) {
                        String value2 = arrstring[value];
                        String[] keyValue = value2.split("=");
                        BasicNameValuePair pair = null;
                        pair = keyValue.length != 2 ? new BasicNameValuePair(keyValue[0], "") : new BasicNameValuePair(keyValue[0], keyValue[1]);
                        params.add(pair);
                        ++value;
                    }
                    method.setEntity((HttpEntity)new UrlEncodedFormEntity(params, "UTF-8"));
                }
                SSLSocketFactory.getSocketFactory().setHostnameVerifier((X509HostnameVerifier)new AllowAllHostnameVerifier());
                CloseableHttpResponse response = client.execute((HttpUriRequest)method);
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    returnString = EntityUtils.toString((HttpEntity)responseEntity);
                }
                EntityUtils.consume((HttpEntity)responseEntity);
            }
            catch (Exception e) {
                e.printStackTrace();
                client.getConnectionManager().shutdown();
            }
        }
        finally {
            client.getConnectionManager().shutdown();
        }
        return returnString;
    }

    public String sortStringWithSeparator(String[] inputString, String separator) {
        Arrays.sort(inputString);
        StringBuilder outputString = new StringBuilder("");
        int i = 0;
        while (i < inputString.length) {
            outputString.append(inputString[i]);
            outputString.append(separator);
            ++i;
        }
        String request = outputString.substring(0, outputString.length() - 1);
        return request.toString();
    }

    public String sortMapWithSeparator(Map<String, String> inputMap, String separator) {
        Set<String> set = inputMap.keySet();
        ArrayList<String> list = new ArrayList<String>();
        for (String str : set) {
            String k = String.valueOf(str) + "=" + inputMap.get(str) + separator;
            list.add(k);
        }
        Object[] s = new String[list.size()];
        s = list.toArray(s);
        Arrays.sort(s);
        StringBuilder outputString = new StringBuilder("");
        int i = 0;
        while (i < s.length) {
            outputString.append((String)s[i]);
            ++i;
        }
        String request = outputString.substring(0, outputString.length() - 1);
        System.out.println(request);
        return request.toString();
    }

    public String removeFromString(String inputString, String orgSeparator, String finalSeparator, String type, String key) {
        String[] arrayString = inputString.split(orgSeparator);
        ArrayList<String> list = new ArrayList<String>();
        int i = 0;
        while (i < arrayString.length) {
            if (EQUALS.equalsIgnoreCase(type) && !arrayString[i].equals(key)) {
                list.add(arrayString[i]);
            }
            if (EQUALSIGNORECASE.equalsIgnoreCase(type) && !arrayString[i].equalsIgnoreCase(key)) {
                list.add(arrayString[i]);
            }
            if (ENDSWITH.equalsIgnoreCase(type) && !arrayString[i].endsWith(key)) {
                list.add(arrayString[i]);
            }
            if (STARTSWITH.equalsIgnoreCase(type) && !arrayString[i].startsWith(key)) {
                list.add(arrayString[i]);
            }
            if (CONTAINS.equalsIgnoreCase(type) && !arrayString[i].contains(key)) {
                list.add(arrayString[i]);
            }
            ++i;
        }
        StringBuilder outputString = new StringBuilder("");
        int i2 = 0;
        while (i2 < list.size()) {
            outputString.append((String)list.get(i2));
            outputString.append(finalSeparator);
            ++i2;
        }
        String finalString = outputString.substring(0, outputString.length() - 1);
        return finalString;
    }

    public String returnSystemDate() {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String systemCurrDate = df.format(date);
        return systemCurrDate;
    }

    public static String textEncode(String text, String codeType) throws IOException {
        text = URLEncoder.encode(text, codeType);
        return text;
    }

    public String textDncode(String text, String codeType) throws IOException {
        text = URLDecoder.decode(text, codeType);
        return text;
    }

    public String getDecode(String key) throws IOException {
        byte[] s = Base64.decodeBase64((byte[])key.getBytes("utf-8"));
        return new String(s);
    }

    public String encode(String str) throws UnsupportedEncodingException {
        return URLEncoder.encode(str, "UTF-8");
    }

    public String sha256Encrypt(String strSrc) {
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = strSrc.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(bt);
            strDes = Tools.bytes2Hex(md.digest());
        }
        catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        int i = 0;
        while (i < bts.length) {
            tmp = Integer.toHexString(bts[i] & 255);
            if (tmp.length() == 1) {
                des = String.valueOf(des) + "0";
            }
            des = String.valueOf(des) + tmp;
            ++i;
        }
        return des;
    }

    public String quickPayEncrypt(String quickyPayString, String encryptKey) {
        int k;
        StringBuilder quickPayString = new StringBuilder("");
        RSAUtil rsa = new RSAUtil();
        String headString = quickyPayString.substring(0, quickyPayString.lastIndexOf("^") + 1);
        quickPayString.append(headString);
        String str = quickyPayString.substring(quickyPayString.lastIndexOf("^") + 1, quickyPayString.length());
        String[] splitQuickPayString = str.split(",");
        int i = 0;
        while (i < splitQuickPayString.length) {
            if (!splitQuickPayString[i].isEmpty()) {
                if (i == 1 || i == 2 || i == 6 || i == 7 || i == 8 || i == 9) {
                    if (i == 1 || i == 2 || i == 6 || i == 7 || i == 8) {
                        try {
                            quickPayString.append(String.valueOf(rsa.encrypt(splitQuickPayString[i], encryptKey)) + "replaceFlag");
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (i == 9) {
                        try {
                            quickPayString.append(String.valueOf(rsa.encrypt(splitQuickPayString[i], encryptKey)) + "replaceFlag");
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    quickPayString.append(String.valueOf(splitQuickPayString[i]) + "replaceFlag");
                }
            } else {
                quickPayString.append("replaceFlag");
            }
            ++i;
        }
        int i2 = k = splitQuickPayString.length;
        while (i2 < 11) {
            quickPayString.append("replaceFlag");
            ++i2;
        }
        return quickPayString.toString();
    }

    public String collectMethodEncrypt(String collectMethod, String encryptKey) {
        StringBuilder collectMethodString = new StringBuilder("");
        collectMethodString.append(collectMethod.substring(0, collectMethod.lastIndexOf("^") + 1));
        RSAUtil rsa = new RSAUtil();
        String extend = collectMethod.substring(collectMethod.lastIndexOf("^") + 1, collectMethod.length());
        String[] splitExtend = extend.split(",");
        int i = 0;
        while (i < splitExtend.length) {
            if (!splitExtend[i].isEmpty()) {
                if (i == 1 || i == 2 || i == 7) {
                    if (i == 1 || i == 2) {
                        try {
                            collectMethodString.append(String.valueOf(rsa.encrypt(splitExtend[i], encryptKey)) + "replaceFlag");
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (i == 7) {
                        collectMethodString.append(splitExtend[i]);
                    }
                } else {
                    collectMethodString.append(String.valueOf(splitExtend[i]) + "replaceFlag");
                }
            } else {
                collectMethodString.append("replaceFlag");
            }
            ++i;
        }
        return collectMethodString.toString();
    }

    public String tradeListEncrypt(String tradeList, String encryptKey) {
        StringBuilder tradeListString = new StringBuilder("");
        RSAUtil rsa = new RSAUtil();
        if (tradeList.indexOf("$") == -1) {
            String[] splitByPara = tradeList.split("~");
            int tailLenght = 0;
            if (splitByPara.length == 4) {
                tailLenght = splitByPara[2].length() + splitByPara[3].length() + 3;
            } else if (splitByPara.length == 3) {
                tailLenght = splitByPara[2].length() + 3;
            }
            String tailString = tradeList.substring(tradeList.length() - tailLenght, tradeList.length());
            String headString = tradeList.substring(0, tradeList.lastIndexOf("^") + 1);
            tradeListString.append(headString);
            String extend = tradeList.substring(tradeList.lastIndexOf("^") + 1, tradeList.length() - tailLenght);
            String[] splitExtend = extend.split(",");
            int i = 0;
            while (i < splitExtend.length) {
                if (!splitExtend[i].isEmpty()) {
                    if (i == 1 || i == 2 || i == 7) {
                        if (i == 1 || i == 2) {
                            try {
                                tradeListString.append(String.valueOf(rsa.encrypt(splitExtend[i], encryptKey)) + "replaceFlag");
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (i == 7) {
                            tradeListString.append(splitExtend[i]);
                        }
                    } else {
                        tradeListString.append(String.valueOf(splitExtend[i]) + "replaceFlag");
                    }
                } else {
                    tradeListString.append("replaceFlag");
                }
                ++i;
            }
            tradeListString.append(tailString);
        } else {
            String[] entry = tradeList.split("\\$");
            int k = 0;
            while (k < entry.length) {
                String[] splitByPara = entry[k].split("~");
                int tailLenght = 0;
                if (splitByPara.length == 4) {
                    tailLenght = splitByPara[2].length() + splitByPara[3].length() + 3;
                } else if (splitByPara.length == 3) {
                    tailLenght = splitByPara[2].length() + 3;
                }
                String tailString = entry[k].substring(entry[k].length() - tailLenght, entry[k].length());
                String headString = entry[k].substring(0, entry[k].lastIndexOf("^") + 1);
                tradeListString.append(headString);
                String extend = entry[k].substring(entry[k].lastIndexOf("^") + 1, entry[k].length() - tailLenght);
                String[] splitExtend = extend.split(",");
                int i = 0;
                while (i < splitExtend.length) {
                    if (!splitExtend[i].isEmpty()) {
                        if (i == 1 || i == 2 || i == 7) {
                            if (i == 1 || i == 2) {
                                try {
                                    tradeListString.append(String.valueOf(rsa.encrypt(splitExtend[i], encryptKey)) + "replaceFlag");
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if (i == 7) {
                                tradeListString.append(splitExtend[i]);
                            }
                        } else {
                            tradeListString.append(String.valueOf(splitExtend[i]) + "replaceFlag");
                        }
                    } else {
                        tradeListString.append("replaceFlag");
                    }
                    ++i;
                }
                tradeListString.append(String.valueOf(tailString) + "$");
                ++k;
            }
        }
        return tradeListString.toString().substring(0, tradeListString.length() - 1);
    }

    public String detailListEncrypt(String detailList, String encryptKey) {
        StringBuilder detailListString = new StringBuilder("");
        RSAUtil rsa = new RSAUtil();
        if (detailList.indexOf("|") == -1) {
            String[] encryptList = detailList.split("\\^");
            int i = 0;
            while (i < encryptList.length) {
                if (i == 1 || i == 2 || i == 3) {
                    if (i == 1 || i == 3) {
                        try {
                            detailListString.append(String.valueOf(rsa.encrypt(encryptList[i], encryptKey)) + "replaceFlag");
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (i == 2 && !encryptList[2].isEmpty()) {
                        try {
                            detailListString.append(String.valueOf(rsa.encrypt(encryptList[i], encryptKey)) + "replaceFlag");
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (i == 2 && encryptList[2].isEmpty()) {
                        detailListString.append("replaceFlag");
                    }
                } else {
                    detailListString.append(String.valueOf(encryptList[i]) + "replaceFlag");
                }
                ++i;
            }
            return detailListString.toString();
        }
        String[] entry = detailList.split("\\|");
        System.out.println(entry[0]);
        System.out.println(entry[1]);
        int k = 0;
        while (k < entry.length) {
            String[] encryptList = entry[k].split("\\^");
            int i = 0;
            while (i < encryptList.length) {
                if (i == 1 || i == 2 || i == 3) {
                    if (i == 1 || i == 3) {
                        try {
                            detailListString.append(String.valueOf(rsa.encrypt(encryptList[i], encryptKey)) + "replaceFlag");
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (i == 2 && !encryptList[2].isEmpty()) {
                        try {
                            detailListString.append(String.valueOf(rsa.encrypt(encryptList[i], encryptKey)) + "replaceFlag");
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (i == 2 && encryptList[2].isEmpty()) {
                        detailListString.append("replaceFlag");
                    }
                } else {
                    detailListString.append(String.valueOf(encryptList[i]) + "replaceFlag");
                }
                ++i;
            }
            detailListString.append("flagReplace");
            ++k;
        }
        return detailListString.toString().substring(0, detailListString.length() - 11);
    }

    public String getFormData(Map<String, String> formData) {
        if (formData == null) {
            return "";
        }
        StringBuffer sBuffer = new StringBuffer();
        for (String key : formData.keySet()) {
            sBuffer.append("<input type=\"hidden\" name=\"").append(key).append("\" value=\"").append(formData.get(key)).append("\" />");
        }
        return sBuffer.toString();
    }

    public String getHtmlData(String formData, String bankPostUrl) {
        String method = "POST";
        if (formData == null || formData.isEmpty()) {
            method = "GET";
        }
        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append("<html><head></head><body><form id=\"frmBankID\" name=\"frmBankName\" method=\"").append(method).append("\" action=\"").append(bankPostUrl).append("\">").append(formData).append("</form><script language=\"javascript\">document.getElementById(\"frmBankID\").submit();</script></body></html>");
        return sbBuffer.toString();
    }

    public String base64Decode(String data) {
        byte[] dataByte = data.getBytes();
        return new String(Base64.decodeBase64((byte[])dataByte));
    }

    public static String calculateSignPlain(Map<String, String> params) {
        TreeMap<String, String> sortedParams = new TreeMap<String, String>(params);
        sortedParams.remove("sign");
        sortedParams.remove("sign_type");
        sortedParams.remove("sign_version");
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> e : sortedParams.entrySet()) {
            if (StringUtil.isEmpty(e.getValue())) continue;
            sb.append(e.getKey()).append("=").append(e.getValue()).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static void main(String[] args) {
        Tools tool = new Tools();
        System.out.println(tool.detailListEncrypt("batch001^\u9a6c\u4e91\u98de^123^6217001210023432972^\u4e2d\u56fd\u5efa\u8bbe\u94f6\u884c^CCB^\u4e0a\u6d77^\u4e0a\u6d77\u5e02^\u4e2d\u56fd\u5efa\u8bbe\u94f6\u884c\u80a1\u4efd\u6709\u9650\u516c\u53f8\u4e0a\u6d77\u5206\u884c^0.01^C^DEBIT^", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDv0rdsn5FYPn0EjsCPqDyIsYRawNWGJDRHJBcdCldodjM5bpve+XYb4Rgm36F6iDjxDbEQbp/HhVPj0XgGlCRKpbluyJJt8ga5qkqIhWoOd/Cma1fCtviMUep21hIlg1ZFcWKgHQoGoNX7xMT8/0bEsldaKdwxOlv3qGxWfqNV5QIDAQAB"));
    }
}
