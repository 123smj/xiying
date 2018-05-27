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
import com.gy.util.MyHostnameVerifier;
import com.gy.util.WezbankHttpsUtility;
import com.trade.util.RSAUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.bind.JAXBException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.HttpParams;
import org.apache.log4j.Logger;
import sun.misc.BASE64Encoder;

public final class HttpsUtility {
    static String proxyHost = "119.29.195.110";
    static int proxyPort = 8080;
    static String proxyUsername = "";
    static String proxyPassword = "";
    public static final String absolutePath = RSAUtils.class.getResource("/").getPath();
    private static Logger log = Logger.getLogger(HttpsUtility.class);
    static int httpConnectionTimeout = Environment.getIntProperty("http.ConnectionTimeout");
    static int httpReadTimeout = Environment.getIntProperty("http.ReadTimeout");

    static {
        httpConnectionTimeout = httpConnectionTimeout == 0 ? 30000 : httpConnectionTimeout;
        httpReadTimeout = httpReadTimeout == 0 ? 30000 : httpReadTimeout;
    }

    private HttpsUtility() {
    }

    public static KeyStore getKeyStore(String password, String keyStorePath) throws Exception {
        KeyStore ks = KeyStore.getInstance("JKS");
        FileInputStream is = new FileInputStream(keyStorePath);
        ks.load(is, password.toCharArray());
        is.close();
        return ks;
    }

    public static SSLContext getSSLContext(String password, String keyStorePath, String trustStorePath) throws Exception {
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        KeyStore keyStore = HttpsUtility.getKeyStore(password, keyStorePath);
        keyManagerFactory.init(keyStore, password.toCharArray());
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        KeyStore trustStore = HttpsUtility.getKeyStore(null, trustStorePath);
        trustManagerFactory.init(trustStore);
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
        return ctx;
    }

    public static void initHttpsURLConnection(String password, String keyStorePath, String trustStorePath) throws Exception {
        SSLContext sslContext = null;
        MyHostnameVerifier hnv = new MyHostnameVerifier();
        try {
            sslContext = HttpsUtility.getSSLContext(password, keyStorePath, trustStorePath);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        if (sslContext != null) {
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        }
        HttpsURLConnection.setDefaultHostnameVerifier(hnv);
    }

    public static String post(String httpsUrl, String xmlStr) {
        HttpsURLConnection urlCon = null;
        String line = null;
        try {
            urlCon = (HttpsURLConnection) new URL(httpsUrl).openConnection();
            urlCon.setDoInput(true);
            urlCon.setDoOutput(true);
            urlCon.setRequestMethod("POST");
            urlCon.setRequestProperty("Content-Length", String.valueOf(xmlStr.getBytes().length));
            urlCon.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            urlCon.setUseCaches(false);
            urlCon.getOutputStream().write(xmlStr.getBytes("utf-8"));
            urlCon.getOutputStream().flush();
            urlCon.getOutputStream().close();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return line;
    }

    public static String get(String attachmentURL) {
        InputStream inputStream = null;
        URLConnection urlConnection = null;
        URL url = null;
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        byte[] byteChunk = new byte[4096];
        int noOfBytes = 0;
        String result = "";
        try {
            String fileName = attachmentURL.substring(attachmentURL.lastIndexOf("/") + 1);
            String urlPath = attachmentURL.substring(0, attachmentURL.lastIndexOf("/") + 1);
            fileName = URLEncoder.encode(fileName, "UTF-8");
            if (fileName.contains("+")) {
                fileName = fileName.replace("+", "%20");
            }
            url = new URL(String.valueOf(urlPath) + fileName);
            urlConnection = url.openConnection();
            urlConnection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            while ((noOfBytes = inputStream.read(byteChunk)) > 0) {
                byteOutputStream.write(byteChunk, 0, noOfBytes);
                result = String.valueOf(result) + new String(byteChunk, 0, noOfBytes);
            }
            return new BASE64Encoder().encode(byteOutputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
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
        System.out.println("\u8fd4\u56de\u62a5\u6587:" + response);
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

    public static void main(String[] args) throws Exception {
        String password = "www.guoyinpay.com";
        String keyStorePath = "E:/www.guoyinpay.com/webank_keystore.jks";
        String trustStorePath = "E:/www.guoyinpay.com/webank_truststore.jks";
        String httpsUrl = "https://l.test-svrapi.webank.com/api/oauth2/access_token?app_id=W3438051&secret=GXXn1SItFP7iJM61eR11DN7vxYSTHxZljsdRZrcmRdfEa0JO6vVK8X8OndZR7suh&grant_type=client_credential&version=1.0.0";
        WezbankHttpsUtility.httpPost(Environment.createEnvironment(httpsUrl, "utf-8", "application/json"), "", keyStorePath, password, trustStorePath);
    }
}
