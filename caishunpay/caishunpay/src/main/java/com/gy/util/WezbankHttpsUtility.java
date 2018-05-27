/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.http.HttpEntity
 *  org.apache.http.HttpResponse
 *  org.apache.http.StatusLine
 *  org.apache.http.client.ClientProtocolException
 *  org.apache.http.client.config.RequestConfig
 *  org.apache.http.client.config.RequestConfig$Builder
 *  org.apache.http.client.methods.HttpGet
 *  org.apache.http.client.methods.HttpPost
 *  org.apache.http.client.methods.HttpUriRequest
 *  org.apache.http.entity.StringEntity
 *  org.apache.http.impl.client.CloseableHttpClient
 *  org.apache.http.params.HttpParams
 *  org.apache.http.util.EntityUtils
 *  org.apache.log4j.Logger
 */
package com.gy.util;

import com.common.wezbank.HttpClientUtils;
import com.gy.system.Environment;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.JAXBException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public final class WezbankHttpsUtility {
    static String proxyHost = "";
    static int proxyPort = 8080;
    static String proxyUsername = "";
    static String proxyPassword = "";
    private static Logger log = Logger.getLogger(WezbankHttpsUtility.class);
    static int httpConnectionTimeout = Environment.getIntProperty("http.ConnectionTimeout");
    static int httpReadTimeout = Environment.getIntProperty("http.ReadTimeout");

    static {
        httpConnectionTimeout = httpConnectionTimeout == 0 ? 30000 : httpConnectionTimeout;
        httpReadTimeout = httpReadTimeout == 0 ? 30000 : httpReadTimeout;
    }

    private WezbankHttpsUtility() {
    }

    public static HttpPost createPostRequest(Environment env, String xmlRequest) throws URISyntaxException, UnsupportedEncodingException, IOException, JAXBException {
        URI postUrl = null;
        HttpPost httpPost = null;
        if (xmlRequest != null) {
            postUrl = new URI(env.getBaseUrl());
            httpPost = new HttpPost(postUrl);
            httpPost.getParams().setBooleanParameter("http.protocol.expect-continue", false);
            httpPost.getParams().setIntParameter("http.connection.timeout", httpConnectionTimeout);
            httpPost.getParams().setIntParameter("http.socket.timeout", httpReadTimeout);
            httpPost.setHeader("Content-Type", String.valueOf(env.getContentType()) + "; charset=" + env.getCharset());
            Map<String, String> headerParam = env.getHeaderParam();
            if (headerParam != null) {
                for (Map.Entry<String, String> entry : headerParam.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            log.info((Object)("\u53d1\u9001\u62a5\u6587:" + xmlRequest));
            System.out.println("\u53d1\u9001\u62a5\u6587:" + xmlRequest);
            httpPost.setEntity((HttpEntity)new StringEntity(xmlRequest, env.getCharset()));
        }
        return httpPost;
    }

    public static String httpPost(Environment env, String contentBody, String clientJksPath, String keyStorePass, String serverJksPath) {
        StringBuilder buffer;
        block35 : {
            FileInputStream trustStoreInput;
            block36 : {
                FileInputStream keyStoreInput = null;
                trustStoreInput = null;
                KeyStore keyStore = null;
                KeyStore trustStore = null;
                HttpResponse response = null;
                buffer = new StringBuilder();
                try {
                    keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                    keyStoreInput = new FileInputStream(new File(clientJksPath));
                    keyStore.load(keyStoreInput, keyStorePass.toCharArray());
                    trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                    trustStoreInput = new FileInputStream(new File(serverJksPath));
                    trustStore.load(trustStoreInput, null);
                }
                catch (Throwable t) {
                    throw new RuntimeException("input KeyStore  fail", t);
                }
                CloseableHttpClient client = HttpClientUtils.createHttpClientWithCert(keyStore, keyStorePass, trustStore, 200, 5, 1000, 3000, proxyHost, proxyPort, proxyUsername, proxyPassword);
                log.info((Object)("https--post" + env.getBaseUrl()));
                try {
                    HttpPost httpPost = new HttpPost(new URI(env.getBaseUrl()));
                    httpPost.getParams().setIntParameter("http.connection.timeout", httpConnectionTimeout);
                    httpPost.getParams().setIntParameter("http.socket.timeout", httpReadTimeout);
                    httpPost.setHeader("Content-Type", String.valueOf(env.getContentType()) + "; charset=" + env.getCharset());
                    Map<String, String> headerParam = env.getHeaderParam();
                    if (headerParam != null) {
                        for (Map.Entry<String, String> entry : headerParam.entrySet()) {
                            httpPost.setHeader(entry.getKey(), entry.getValue());
                        }
                    }
                    log.info((Object)("\u53d1\u9001\u62a5\u6587:" + contentBody));
                    httpPost.setEntity((HttpEntity)new StringEntity(contentBody, env.getCharset()));
                    RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(40000).setConnectionRequestTimeout(40000).setSocketTimeout(60000).build();
                    httpPost.setConfig(requestConfig);
                    response = client.execute((HttpUriRequest)httpPost);
                    if (response != null && response.getStatusLine() != null) {
                        if (200 == response.getStatusLine().getStatusCode()) {
                            HttpEntity entity = response.getEntity();
                            InputStream instream = entity.getContent();
                            byte[] bytes = new byte[1024];
                            int c = 0;
                            while (-1 != (c = instream.read(bytes))) {
                                buffer.append(new String(bytes, 0, c, env.getCharset()));
                            }
                        }
                        System.out.println("httpcode:" + response.getStatusLine().getStatusCode());
                    }
                }
                catch (ClientProtocolException e) {
                    e.printStackTrace();
                    if (keyStoreInput != null) {
                        try {
                            keyStoreInput.close();
                        }
                        catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                    if (trustStoreInput != null) {
                        try {
                            trustStoreInput.close();
                        }
                        catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    }
                    break block35;
                }
                catch (Exception e) {
                    try {
                        e.printStackTrace();
                        break block35;
                    }
//                    catch (Throwable throwable) {
//                        throw throwable;
//                    }
                    finally {
                        if (keyStoreInput != null) {
                            try {
                                keyStoreInput.close();
                            }
                            catch (IOException e4) {
                                e4.printStackTrace();
                            }
                        }
                        if (trustStoreInput != null) {
                            try {
                                trustStoreInput.close();
                            }
                            catch (IOException e5) {
                                e5.printStackTrace();
                            }
                        }
                    }
                }
                if (keyStoreInput == null) break block36;
                try {
                    keyStoreInput.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (trustStoreInput != null) {
                try {
                    trustStoreInput.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        log.info((Object)("\u8fd4\u56de\u62a5\u6587:" + buffer.toString()));
        return buffer.toString();
    }

    public static String httpGet(String url, String clientJksPath, String keyStorePass, String serverJksPath) {
        String result;
        FileInputStream keyStoreInput = null;
        FileInputStream trustStoreInput = null;
        KeyStore keyStore = null;
        KeyStore trustStore = null;
        HttpResponse response = null;
        result = null;
        try {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStoreInput = new FileInputStream(new File(clientJksPath));
            keyStore.load(keyStoreInput, keyStorePass.toCharArray());
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStoreInput = new FileInputStream(new File(serverJksPath));
            trustStore.load(trustStoreInput, null);
        }
        catch (Throwable t) {
            throw new RuntimeException("input KeyStore  fail", t);
        }
        CloseableHttpClient client = HttpClientUtils.createHttpClientWithCert(keyStore, keyStorePass, trustStore, 200, 5, 1000, 3000, proxyHost, proxyPort, proxyUsername, proxyPassword);
        log.info((Object)("httpget\uff1a" + url));
        HttpGet httpGet = new HttpGet(url);
        try {
            try {
                response = client.execute((HttpUriRequest)httpGet);
                result = EntityUtils.toString((HttpEntity)response.getEntity());
                log.info((Object)("httpget----back\uff1a" + result));
            }
            catch (ClientProtocolException e) {
                e.printStackTrace();
                if (keyStoreInput != null) {
                    try {
                        keyStoreInput.close();
                    }
                    catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                if (trustStoreInput != null) {
                    try {
                        trustStoreInput.close();
                    }
                    catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
            }
            catch (IOException e) {
                block31 : {
                    e.printStackTrace();
                    if (keyStoreInput == null) break block31;
                    try {
                        keyStoreInput.close();
                    }
                    catch (IOException e4) {
                        e4.printStackTrace();
                    }
                }
                if (trustStoreInput != null) {
                    try {
                        trustStoreInput.close();
                    }
                    catch (IOException e5) {
                        e5.printStackTrace();
                    }
                }
            }
        }
        finally {
            if (keyStoreInput != null) {
                try {
                    keyStoreInput.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (trustStoreInput != null) {
                try {
                    trustStoreInput.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    private static <T extends Closeable> void tryClose(T closableObject) {
        if (closableObject != null) {
            try {
                closableObject.close();
            }
            catch (Exception e) {
                log.warn((Object)String.format("Exception closing '%s': '%s'", closableObject.getClass(), e.getMessage()));
            }
        }
    }
}
