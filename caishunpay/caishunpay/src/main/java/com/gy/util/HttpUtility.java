/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.http.HttpEntity
 *  org.apache.http.client.methods.HttpPost
 *  org.apache.http.entity.StringEntity
 *  org.apache.http.params.HttpParams
 *  org.apache.log4j.Logger
 */
package com.gy.util;

import com.gy.system.Environment;
import com.trade.util.JsonUtil;
import com.trade.util.MD5Util;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public final class HttpUtility {
    private static Logger log = Logger.getLogger(HttpUtility.class);

    private HttpUtility() {
    }

    public static PostMethod getPostMethod(Environment env, String xmlRequest){
        PostMethod post = new PostMethod(env.getBaseUrl());
        post.setRequestBody(xmlRequest);
        String contentType = "";
        if (env.getContentType() != null) {
            contentType += env.getContentType();
            if (env.getCharset() != null) {
                contentType += "; charset=" + env.getCharset();
            }
        }
        post.setRequestHeader("Content-Type", contentType);
        if(env.getHeaderParam()!=null){
            for (Map.Entry<String,String> entry : env.getHeaderParam().entrySet())
            post.setRequestHeader(entry.getKey(),entry.getValue());
        }
        log.info("向 "+env.getBaseUrl() +"发送报文:" + xmlRequest);
        return post;
    }

    public static GetMethod getGetMethod(String Url) {
        return new GetMethod(Url);
    }

    public static String postData(Environment env, String xmlRequest) {
        String response = null;
        HttpClient httpclient = new HttpClient();
        PostMethod post = getPostMethod(env,xmlRequest);
        try {
            httpclient.executeMethod(post);
            response = post.getResponseBodyAsString();
        } catch (IOException e) {
            post.releaseConnection();
        }
        log.info("\u8fd4\u56de\u62a5\u6587:" + response);
        return response;
    }

    public static String getData(String url, Map<String, String> params) {
        String response = null;
        HttpClient httpclient = new HttpClient();
        if (params != null) {
            url = url + "?" + MD5Util.map2HttpParam(params);
        }
        GetMethod get = getGetMethod(url);
        try {
            httpclient.executeMethod(get);
            response = get.getResponseBodyAsString();
        } catch (IOException e) {
            get.releaseConnection();
        }
        log.info("\u8fd4\u56de\u62a5\u6587:" + response);
        return response;
    }

    public static Map<String, String> httpParam2map(String paramStr) {
        Map<String, String> map = new HashMap<>();
        for (String s : paramStr.split("&")) {
            String[] as = s.split("=");
            if (as.length == 1) {
                map.put(as[0], "");
            } else {
                try {
                    map.put(as[0], URLDecoder.decode(as[1], "UTF-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("when decode url : " + paramStr, e);
                }
            }
        }
        return map;
    }
}
