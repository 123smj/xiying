/*
 * Decompiled with CFR 0_124.
 */
package com.trade.util.ninepi;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

public class SimpleHttpsSocketFactory
implements X509TrustManager {
    SimpleHttpsSocketFactory() {
    }

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
}
