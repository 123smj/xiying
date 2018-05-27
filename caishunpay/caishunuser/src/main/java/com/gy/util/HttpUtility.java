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
import com.gy.util.HttpCallTask;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.xml.bind.JAXBException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.HttpParams;
import org.apache.log4j.Logger;

public final class HttpUtility {
    private static Logger log = Logger.getLogger(HttpUtility.class);
    static int httpConnectionTimeout = Environment.getIntProperty("http.ConnectionTimeout");
    static int httpReadTimeout = Environment.getIntProperty("http.ReadTimeout");

    static {
        httpConnectionTimeout = httpConnectionTimeout == 0 ? 30000 : httpConnectionTimeout;
        httpReadTimeout = httpReadTimeout == 0 ? 30000 : httpReadTimeout;
    }

    private HttpUtility() {
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
            log.info((Object) ("\u53d1\u9001\u62a5\u6587:" + xmlRequest));
            System.out.println("\u53d1\u9001\u62a5\u6587:" + xmlRequest);
            httpPost.setEntity((HttpEntity) new StringEntity(xmlRequest, env.getCharset()));
        }
        return httpPost;
    }

    public static String postData(Environment env, String xmlRequest) {
        String response = null;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future future = executor.submit(new HttpCallTask(env, xmlRequest));
        executor.shutdown();
        try {
            response = (String) future.get(50000L, TimeUnit.MILLISECONDS);
            log.debug((Object) String.format("Response: '%s'", response));
        } catch (InterruptedException ie) {
            log.error((Object) String.format("Http call interrupted Message: '%s'", ie.getMessage()));
        } catch (ExecutionException ee) {
            log.error((Object) String.format("Execution error for http post Message: '%s'", ee.getMessage()));
        } catch (TimeoutException e) {
            e.printStackTrace();
            future.cancel(true);
        }
        log.info((Object) ("\u8fd4\u56de\u62a5\u6587:" + response));
        return response;
    }

    private static <T extends Closeable> void tryClose(T closableObject) {
        if (closableObject != null) {
            try {
                closableObject.close();
            } catch (Exception e) {
                log.warn((Object) String.format("Exception closing '%s': '%s'", closableObject.getClass(), e.getMessage()));
            }
        }
    }
}
