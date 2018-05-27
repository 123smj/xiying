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
 *  org.apache.log4j.Logger
 *  org.springframework.cache.annotation.Cacheable
 *  org.springframework.stereotype.Service
 */
package com.gy.cache;

import com.common.wezbank.HttpClientUtils;
import com.gy.system.WezbankConfigUtil;
import com.gy.util.HttpsUtility;
import com.trade.bean.WezBankClient;
import com.trade.util.JsonUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class WezbankCache {
    static String proxyHost = "";
    static int proxyPort = 8080;
    static String proxyUsername = "";
    static String proxyPassword = "";
    private static Logger log = Logger.getLogger(WezbankCache.class);
    public static final String ACCESS_TOKEN_URL = "https://l.test-svrapi.webank.com/api/oauth2/access_token";
    public static final String TICKET_URL = "https://l.test-svrapi.webank.com/api/oauth2/api_ticket";

    @Cacheable(value = {"wezbankCache"})
    public WezBankClient getWezBankClient(String appId, String secret) {
        WezBankClient wezBankClient;
        block34:
        {
            FileInputStream trustStoreInput;
            block35:
            {
                wezBankClient = new WezBankClient();
                String url = "https://l.test-svrapi.webank.com/api/oauth2/access_token?app_id=" + appId + "&secret=" + secret + "&grant_type=client_credential&version=1.0.0";
                String clientJks = String.valueOf(HttpsUtility.absolutePath) + "cert/wezbank/webank_keystore.jks";
                String serverJks = String.valueOf(HttpsUtility.absolutePath) + "cert/wezbank/webank_truststore.jks";
                String keyStorePass = "www.guoyinpay.com";
                FileInputStream keyStoreInput = null;
                trustStoreInput = null;
                KeyStore keyStore = null;
                KeyStore trustStore = null;
                HttpResponse response = null;
                String result = null;
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
                log.info((Object) ("\u83b7\u53d6\u5fae\u4f17\u94f6\u884c\u4ee4\u724c\uff1a" + url));
                HttpGet httpGet = new HttpGet(url);
                try {
                    response = client.execute((HttpUriRequest) httpGet);
                    result = EntityUtils.toString((HttpEntity) response.getEntity());
                    Map accessResultMap = (Map) JsonUtil.parseJson(result);
                    if (accessResultMap != null && "0".equals(accessResultMap.get("code"))) {
                        wezBankClient.setAccess_token((String) accessResultMap.get("access_token"));
                        String ticket = this.getWezBankTicket(appId, (String) accessResultMap.get("access_token"));
                        wezBankClient.setTicket(ticket);
                        WezbankConfigUtil.setParam("access_token", (String) accessResultMap.get("access_token"));
                        WezbankConfigUtil.setParam("ticket", ticket);
                        WezbankConfigUtil.saveProps();
                    } else if ("400504".equals(accessResultMap.get("code"))) {
                        wezBankClient.setTicket(WezbankConfigUtil.getParam("ticket"));
                        wezBankClient.setAccess_token(WezbankConfigUtil.getParam("access_token"));
                    } else {
                        log.error((Object) ("\u83b7\u53d6\u5fae\u4f17\u94f6\u884c\u4ee4\u724c\u83b7\u53d6\u5f02\u5e38" + (Object) response.getStatusLine() + "----" + result));
                    }
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                    if (keyStoreInput != null) {
                        try {
                            keyStoreInput.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                    if (trustStoreInput != null) {
                        try {
                            trustStoreInput.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    }
                    break block34;
                } catch (IOException e) {
                    try {
                        e.printStackTrace();
                        break block34;
                    } catch (Throwable throwable) {
                        throw throwable;
                    } finally {
                        if (keyStoreInput != null) {
                            try {
                                keyStoreInput.close();
                            } catch (IOException e4) {
                                e4.printStackTrace();
                            }
                        }
                        if (trustStoreInput != null) {
                            try {
                                trustStoreInput.close();
                            } catch (IOException e5) {
                                e5.printStackTrace();
                            }
                        }
                    }
                }
                if (keyStoreInput == null) break block35;
                try {
                    keyStoreInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (trustStoreInput != null) {
                try {
                    trustStoreInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        log.info((Object) ("\u83b7\u53d6\u5fae\u4f17\u94f6\u884c\u4ee4\u724c:" + wezBankClient));
        return wezBankClient;
    }

    public String getWezBankTicket(String appId, String access_token) {
        String ticket;
        String url = "https://l.test-svrapi.webank.com/api/oauth2/api_ticket?app_id=" + appId + "&access_token=" + access_token + "&type=SIGN&version=1.0.0";
        String clientJks = String.valueOf(HttpsUtility.absolutePath) + "cert/wezbank/webank_keystore.jks";
        String serverJks = String.valueOf(HttpsUtility.absolutePath) + "cert/wezbank/webank_truststore.jks";
        String keyStorePass = "www.guoyinpay.com";
        FileInputStream keyStoreInput = null;
        FileInputStream trustStoreInput = null;
        KeyStore keyStore = null;
        KeyStore trustStore = null;
        HttpResponse response = null;
        String result = null;
        ticket = null;
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
        log.info((Object) ("\u83b7\u53d6\u5fae\u4f17\u94f6\u884c\u7968\u636e\uff1a" + url));
        HttpGet httpGet = new HttpGet(url);
        try {
            try {
                response = client.execute((HttpUriRequest) httpGet);
                result = EntityUtils.toString((HttpEntity) response.getEntity());
                Map<String, Object> ticketResultMap = JsonUtil.gsonParseJson(result);
                if (ticketResultMap != null && "0".equals(ticketResultMap.get("code"))) {
                    List maps = (List) ticketResultMap.get("tickets");
                    if (maps != null) {
                        ticket = (String) ((Map) maps.get(0)).get("value");
                    }
                } else {
                    log.error((Object) ("\u83b7\u53d6\u5fae\u4f17\u94f6\u884c\u7968\u636e\u83b7\u53d6\u5f02\u5e38" + (Object) response.getStatusLine() + "----" + result));
                }
                log.info((Object) ("\u83b7\u53d6\u5fae\u4f17\u94f6\u884c\u7968\u636e\u8fd4\u56de" + (Object) response.getStatusLine() + "----" + result));
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                if (keyStoreInput != null) {
                    try {
                        keyStoreInput.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                if (trustStoreInput != null) {
                    try {
                        trustStoreInput.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
            } catch (IOException e) {
                block34:
                {
                    e.printStackTrace();
                    if (keyStoreInput == null) break block34;
                    try {
                        keyStoreInput.close();
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                }
                if (trustStoreInput != null) {
                    try {
                        trustStoreInput.close();
                    } catch (IOException e5) {
                        e5.printStackTrace();
                    }
                }
            }
        } finally {
            if (keyStoreInput != null) {
                try {
                    keyStoreInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (trustStoreInput != null) {
                try {
                    trustStoreInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ticket;
    }
}
