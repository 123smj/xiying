/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.http.HttpEntity
 *  org.apache.http.HttpResponse
 *  org.apache.http.StatusLine
 *  org.apache.http.client.ClientProtocolException
 *  org.apache.http.client.methods.HttpGet
 *  org.apache.http.client.methods.HttpUriRequest
 *  org.apache.http.impl.client.CloseableHttpClient
 *  org.apache.http.util.EntityUtils
 */
package com.common.wezbank;

import com.common.wezbank.HttpClientUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.security.KeyStore;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpClientDemo {
    static String proxyHost = "119.29.195.110";
    static int proxyPort = 8080;
    static String proxyUsername = "";
    static String proxyPassword = "";

    public static void main(String[] args) throws Exception {
        block28:
        {
            String url = "https://l.test-svrapi.webank.com/api/oauth2/access_token?app_id=W3438051&secret=GXXn1SItFP7iJM61eR11DN7vxYSTHxZljsdRZrcmRdfEa0JO6vVK8X8OndZR7suh&grant_type=client_credential&version=1.0.0";
            String clientJks = "E:/www.guoyinpay.com/webank_keystore.jks";
            String serverJks = "E:/www.guoyinpay.com/webank_truststore.jks";
            System.out.println("url : " + url);
            System.out.println("clientJks : " + clientJks);
            System.out.println("serverJks : " + serverJks);
            String keyStorePass = "www.guoyinpay.com";
            FileInputStream keyStoreInput = null;
            FileInputStream trustStoreInput = null;
            KeyStore keyStore = null;
            KeyStore trustStore = null;
            try {
                keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStoreInput = new FileInputStream(new File(clientJks));
                keyStore.load(keyStoreInput, keyStorePass.toCharArray());
                trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStoreInput = new FileInputStream(new File(serverJks));
                trustStore.load(trustStoreInput, null);
            } catch (Throwable t) {
                throw new RuntimeException("input KeyStore  fail", t);
            }
            CloseableHttpClient client = HttpClientUtils.createHttpClientWithCert(keyStore, keyStorePass, trustStore, 200, 5, 1000, 3000, proxyHost, proxyPort, proxyUsername, proxyPassword);
            HttpGet httpGet = new HttpGet(url);
            try {
                try {
                    HttpResponse response = client.execute((HttpUriRequest) httpGet);
                    System.out.println("----------------------------------------");
                    System.out.println((Object) response.getStatusLine());
                    System.out.println(EntityUtils.toString((HttpEntity) response.getEntity(), (Charset) Charset.defaultCharset()));
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                    if (keyStoreInput != null) {
                        try {
                            keyStoreInput.close();
                        } catch (IOException e2) {
                            throw new RuntimeException("key store close fail", e2);
                        }
                    }
                    if (trustStoreInput == null) break block28;
                    try {
                        trustStoreInput.close();
                    } catch (IOException e3) {
                        throw new RuntimeException("trust store close fail", e3);
                    }
                } catch (IOException e) {
                    block29:
                    {
                        e.printStackTrace();
                        if (keyStoreInput == null) break block29;
                        try {
                            keyStoreInput.close();
                        } catch (IOException e4) {
                            throw new RuntimeException("key store close fail", e4);
                        }
                    }
                    if (trustStoreInput == null) break block28;
                    try {
                        trustStoreInput.close();
                    } catch (IOException e5) {
                        throw new RuntimeException("trust store close fail", e5);
                    }
                }
            } finally {
                if (keyStoreInput != null) {
                    try {
                        keyStoreInput.close();
                    } catch (IOException e) {
                        throw new RuntimeException("key store close fail", e);
                    }
                }
                if (trustStoreInput != null) {
                    try {
                        trustStoreInput.close();
                    } catch (IOException e) {
                        throw new RuntimeException("trust store close fail", e);
                    }
                }
            }
        }
    }
}
