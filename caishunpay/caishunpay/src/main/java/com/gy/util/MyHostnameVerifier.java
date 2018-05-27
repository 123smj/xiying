/*
 * Decompiled with CFR 0_124.
 */
package com.gy.util;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class MyHostnameVerifier
implements HostnameVerifier {
    @Override
    public boolean verify(String hostname, SSLSession session) {
        if ("localhost".equals(hostname)) {
            return true;
        }
        return false;
    }
}
