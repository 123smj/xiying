/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 *  org.apache.http.HttpHost
 *  org.apache.http.auth.AuthScope
 *  org.apache.http.auth.Credentials
 *  org.apache.http.auth.UsernamePasswordCredentials
 *  org.apache.http.client.CookieStore
 *  org.apache.http.client.CredentialsProvider
 *  org.apache.http.client.config.RequestConfig
 *  org.apache.http.client.config.RequestConfig$Builder
 *  org.apache.http.config.Registry
 *  org.apache.http.config.RegistryBuilder
 *  org.apache.http.config.SocketConfig
 *  org.apache.http.config.SocketConfig$Builder
 *  org.apache.http.conn.HttpClientConnectionManager
 *  org.apache.http.conn.socket.LayeredConnectionSocketFactory
 *  org.apache.http.conn.socket.PlainConnectionSocketFactory
 *  org.apache.http.conn.ssl.SSLConnectionSocketFactory
 *  org.apache.http.conn.ssl.TrustSelfSignedStrategy
 *  org.apache.http.impl.client.BasicCookieStore
 *  org.apache.http.impl.client.BasicCredentialsProvider
 *  org.apache.http.impl.client.CloseableHttpClient
 *  org.apache.http.impl.client.HttpClientBuilder
 *  org.apache.http.impl.client.HttpClients
 *  org.apache.http.impl.conn.PoolingHttpClientConnectionManager
 *  org.apache.http.ssl.SSLContextBuilder
 *  org.apache.http.ssl.SSLContexts
 *  org.apache.http.ssl.TrustStrategy
 */
package com.common.wezbank;

import java.security.KeyStore;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;

public class HttpClientUtils {
    public static CloseableHttpClient createHttpClientWithCert(KeyStore keyStore, String keyStorePassword, KeyStore trustStoreFile, int connMaxTotal, int connDefaultMaxPerRoute, int validateInactivityMillSeconds, int connEvictIdleConnectionsTimeoutMillSeconds, String proxyHost, int proxyPort, String proxyUsername, String proxyPassword) {
        SSLContext sslcontext = null;
        try {
            sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, keyStorePassword.toCharArray()).loadTrustMaterial(trustStoreFile, (TrustStrategy) new TrustSelfSignedStrategy()).build();
        } catch (Exception e) {
            throw new RuntimeException("key store fail", e);
        }
        HostnameVerifier allHostsValid = new HostnameVerifier() {

            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        Registry socketFactoryRegistry = RegistryBuilder.create().register("https", (Object) sslsf).register("http", (Object) PlainConnectionSocketFactory.INSTANCE).build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
        connManager.setDefaultSocketConfig(socketConfig);
        connManager.setValidateAfterInactivity(validateInactivityMillSeconds);
        connManager.setMaxTotal(connMaxTotal);
        connManager.setDefaultMaxPerRoute(connDefaultMaxPerRoute);
        BasicCookieStore cookieStore = new BasicCookieStore();
        RequestConfig defaultRequestConfig = RequestConfig.custom().setCookieSpec("default").setExpectContinueEnabled(true).build();
        HttpHost proxy = null;
        if (StringUtils.isNotEmpty((CharSequence) proxyHost)) {
            proxy = new HttpHost(proxyHost, proxyPort, "http");
        }
        BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
        if (StringUtils.isNotEmpty((CharSequence) proxyUsername) && StringUtils.isNotEmpty((CharSequence) proxyPassword)) {
            credsProvider.setCredentials(new AuthScope(proxyHost, proxyPort), (Credentials) new UsernamePasswordCredentials(proxyUsername, proxyPassword));
        }
        CloseableHttpClient httpclient = proxy == null ? HttpClients.custom().setConnectionManager((HttpClientConnectionManager) connManager).setDefaultCookieStore((CookieStore) cookieStore).setDefaultRequestConfig(defaultRequestConfig).evictExpiredConnections().evictIdleConnections((long) connEvictIdleConnectionsTimeoutMillSeconds, TimeUnit.MILLISECONDS).setSSLSocketFactory((LayeredConnectionSocketFactory) sslsf).build() : HttpClients.custom().setConnectionManager((HttpClientConnectionManager) connManager).setProxy(proxy).setDefaultCredentialsProvider((CredentialsProvider) credsProvider).setDefaultCookieStore((CookieStore) cookieStore).setDefaultRequestConfig(defaultRequestConfig).evictExpiredConnections().evictIdleConnections((long) connEvictIdleConnectionsTimeoutMillSeconds, TimeUnit.MILLISECONDS).setSSLSocketFactory((LayeredConnectionSocketFactory) sslsf).build();
        return httpclient;
    }
}
