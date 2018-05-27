/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.commons.httpclient.ConnectTimeoutException
 *  org.apache.commons.httpclient.HttpClient
 *  org.apache.commons.httpclient.HttpClientError
 *  org.apache.commons.httpclient.HttpMethod
 *  org.apache.commons.httpclient.NameValuePair
 *  org.apache.commons.httpclient.methods.GetMethod
 *  org.apache.commons.httpclient.methods.PostMethod
 *  org.apache.commons.httpclient.params.HttpClientParams
 *  org.apache.commons.httpclient.params.HttpConnectionParams
 *  org.apache.commons.httpclient.protocol.Protocol
 *  org.apache.commons.httpclient.protocol.ProtocolSocketFactory
 *  org.apache.commons.io.IOUtils
 */
package com.trade.util.ninepi;

import com.trade.util.ninepi.HttpSendResult;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpClientError;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.io.IOUtils;

public class SimpleHttpsClient {
    private Map<Integer, Integer> registerPortList = new HashMap<Integer, Integer>();

    public SimpleHttpsClient() {
        Protocol.registerProtocol((String)"https", (Protocol)new Protocol("https", (ProtocolSocketFactory)new SimpleHttpsSocketFactory(), 443));
        this.registerPort(443);
    }

    public HttpSendResult postRequest(String url, Map<String, String> params, int timeout, String characterSet) {
        HttpSendResult result;
        if (characterSet == null || "".equals(characterSet)) {
            characterSet = "UTF-8";
        }
        result = new HttpSendResult();
        PostMethod postMethod = new PostMethod(url);
        postMethod.setRequestHeader("Connection", "close");
        postMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + characterSet);
        NameValuePair[] data = this.createNameValuePair(params);
        postMethod.setRequestBody(data);
        Integer port = this.getPort(url);
        if (this.isRegisterPort(port)) {
            Protocol client = new Protocol("https", (ProtocolSocketFactory)new SimpleHttpsSocketFactory(), port.intValue());
            Protocol.registerProtocol((String)"https ", (Protocol)client);
            this.registerPort(port);
        }
        HttpClient client1 = new HttpClient();
        client1.getParams().setSoTimeout(timeout);
        try {
            try {
                int ex = client1.executeMethod((HttpMethod)postMethod);
                InputStream is = postMethod.getResponseBodyAsStream();
                String responseBody = IOUtils.toString((InputStream)is, (String)characterSet);
                result.setStatus(ex);
                result.setResponseBody(responseBody);
            }
            catch (Exception var16) {
                var16.printStackTrace();
                postMethod.releaseConnection();
            }
        }
        finally {
            postMethod.releaseConnection();
        }
        return result;
    }

    public HttpSendResult postRequest(String url, Map<String, String> params, int timeout) {
        return this.postRequest(url, params, timeout, "UTF-8");
    }

    public HttpSendResult getRequest(String url, Map<String, String> params, int timeout, String characterSet) {
        HttpSendResult result;
        if (characterSet == null || "".equals(characterSet)) {
            characterSet = "UTF-8";
        }
        result = new HttpSendResult();
        Integer port = this.getPort(url);
        if (this.isRegisterPort(port)) {
            Protocol httpclient = new Protocol("https", (ProtocolSocketFactory)new SimpleHttpsSocketFactory(), port.intValue());
            Protocol.registerProtocol((String)"https ", (Protocol)httpclient);
            this.registerPort(port);
        }
        url = this.appendUrlParam(url, params);
        HttpClient httpclient1 = new HttpClient();
        GetMethod httpget = new GetMethod(url);
        try {
            try {
                int ex = httpclient1.executeMethod((HttpMethod)httpget);
                InputStream is = httpget.getResponseBodyAsStream();
                String responseBody = IOUtils.toString((InputStream)is, (String)characterSet);
                result.setStatus(ex);
                result.setResponseBody(responseBody);
            }
            catch (Exception var15) {
                var15.printStackTrace();
                httpget.releaseConnection();
            }
        }
        finally {
            httpget.releaseConnection();
        }
        return result;
    }

    public HttpSendResult getRequest(String url, Map<String, String> params, int timeout) {
        return this.getRequest(url, params, timeout, "UTF-8");
    }

    private boolean isRegisterPort(Integer port) {
        if (this.registerPortList.get(port) != null) {
            return true;
        }
        return false;
    }

    private void registerPort(Integer port) {
        this.registerPortList.put(port, port);
    }

    private Integer getPort(String uri) {
        try {
            URL e = new URL(uri);
            int port = e.getPort();
            if (port == -1) {
                port = uri.indexOf("https://") == 0 ? 443 : 80;
            }
            return port;
        }
        catch (MalformedURLException var4) {
            throw new RuntimeException(var4);
        }
    }

    private NameValuePair[] createNameValuePair(Map<String, String> params) {
        NameValuePair[] pairs = new NameValuePair[params.size()];
        int index = 0;
        for (String key : params.keySet()) {
            pairs[index++] = new NameValuePair(key, params.get(key));
        }
        return pairs;
    }

    private String appendUrlParam(String url, Map<String, String> params) {
        String result = "";
        result = url.contains("?") && url.contains("=") ? String.valueOf(url) + "&" : String.valueOf(url) + "?";
        for (String key : params.keySet()) {
            result = String.valueOf(result) + key + "=" + params.get(key) + "&";
        }
        if (result.charAt(result.length() - 1) == '&') {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    public class SimpleHttpsSocketFactory
    implements ProtocolSocketFactory {
        private SSLContext sslcontext;

        public SimpleHttpsSocketFactory() {
            this.sslcontext = null;
        }

        private SSLContext createEasySSLContext() {
            try {
                X509TrustManager e = new X509TrustManager(){

                    @Override
                    public void checkClientTrusted(X509Certificate[] ax509certificate, String s) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] ax509certificate, String s) throws CertificateException {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                };
                TrustManager[] trustMgrs = new TrustManager[]{e};
                SSLContext context = SSLContext.getInstance("SSL");
                context.init(null, trustMgrs, null);
                return context;
            }
            catch (Exception var4) {
                var4.printStackTrace();
                throw new HttpClientError(var4.toString());
            }
        }

        private SSLContext getSSLContext() {
            if (this.sslcontext == null) {
                this.sslcontext = this.createEasySSLContext();
            }
            return this.sslcontext;
        }

        public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort) throws IOException, UnknownHostException {
            return this.getSSLContext().getSocketFactory().createSocket(host, port, clientHost, clientPort);
        }

        public Socket createSocket(String host, int port, InetAddress localAddress, int localPort, HttpConnectionParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
            if (params == null) {
                throw new IllegalArgumentException("Parameters may not be null");
            }
            int timeout = params.getConnectionTimeout();
            SSLSocketFactory socketfactory = this.getSSLContext().getSocketFactory();
            if (timeout == 0) {
                return socketfactory.createSocket(host, port, localAddress, localPort);
            }
            Socket socket = socketfactory.createSocket();
            InetSocketAddress localaddr = new InetSocketAddress(localAddress, localPort);
            InetSocketAddress remoteaddr = new InetSocketAddress(host, port);
            socket.bind(localaddr);
            socket.connect(remoteaddr, timeout);
            return socket;
        }

        public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
            return this.getSSLContext().getSocketFactory().createSocket(host, port);
        }

        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
            return this.getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        public boolean equals(Object obj) {
            if (obj != null && obj.getClass().equals(SSLSocketFactory.class)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return SimpleHttpsSocketFactory.class.hashCode();
        }

    }

}
