/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  oracle.jdbc.driver.Message
 *  org.apache.commons.logging.Log
 *  org.apache.commons.logging.LogFactory
 *  org.apache.http.HttpEntity
 *  org.apache.http.StatusLine
 *  org.apache.http.client.ClientProtocolException
 *  org.apache.http.client.methods.CloseableHttpResponse
 *  org.apache.http.client.methods.HttpPost
 *  org.apache.http.client.methods.HttpUriRequest
 *  org.apache.http.conn.ClientConnectionManager
 *  org.apache.http.impl.client.DefaultHttpClient
 *  org.apache.http.params.HttpParams
 */
package com.gy.util;

import com.gy.system.Environment;
import com.gy.util.HttpUtility;
import com.gy.util.MySSLSocketFactory;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.concurrent.Callable;

import oracle.jdbc.driver.Message;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;

public class HttpCallTask
        implements Callable {
    private static Log logger = LogFactory.getLog(HttpCallTask.class);
    Environment env = null;
    Class classType = null;
    String requestContent = "";
    private Message errorMessage = null;

    public <T> HttpCallTask(Environment env, String requestContent) {
        this.env = env;
        this.requestContent = requestContent;
    }

    public String call() throws Exception {
        StringBuilder buffer;
        block12:
        {
            buffer = new StringBuilder();
            DefaultHttpClient httpCaller = null;
            try {
                HttpPost httppost = HttpUtility.createPostRequest(this.env, this.requestContent);
                httpCaller = (DefaultHttpClient) MySSLSocketFactory.getNewHttpClient();
                httpCaller.getParams().setParameter("http.connection.timeout", (Object) 40000);
                httpCaller.getParams().setParameter("http.socket.timeout", (Object) 40000);
                CloseableHttpResponse httpResponse = httpCaller.execute((HttpUriRequest) httppost);
                if (httpResponse != null && httpResponse.getStatusLine() != null) {
                    if (200 == httpResponse.getStatusLine().getStatusCode()) {
                        HttpEntity entity = httpResponse.getEntity();
                        InputStream instream = entity.getContent();
                        byte[] bytes = new byte[1024];
                        int c = 0;
                        while (-1 != (c = instream.read(bytes))) {
                            buffer.append(new String(bytes, 0, c, this.env.getCharset()));
                        }
                    }
                    System.out.println("httpcode:" + httpResponse.getStatusLine().getStatusCode());
                }
            } catch (ClientProtocolException httppost) {
                if (httpCaller != null) {
                    httpCaller.getConnectionManager().shutdown();
                }
                break block12;
            } catch (Exception ioe) {
                try {
                    ioe.printStackTrace();
                    break block12;
                } catch (Throwable throwable) {
                    throw throwable;
                } finally {
                    if (httpCaller != null) {
                        httpCaller.getConnectionManager().shutdown();
                    }
                }
            }
            if (httpCaller == null) break block12;
            httpCaller.getConnectionManager().shutdown();
        }
        return buffer.toString();
    }
}
