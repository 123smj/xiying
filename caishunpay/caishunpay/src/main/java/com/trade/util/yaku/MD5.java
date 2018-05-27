/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.commons.codec.digest.DigestUtils
 */
package com.trade.util.yaku;

import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import org.apache.commons.codec.digest.DigestUtils;

public class MD5 {
    public static String sign(String text, String key, String charset) throws Exception {
        text = String.valueOf(text) + key;
        return DigestUtils.md5Hex((byte[])MD5.getContentBytes(text, charset));
    }

    public static String sign(String text, PrivateKey key, String charset) throws Exception {
        throw new UnsupportedOperationException();
    }

    public static boolean verify(String text, String sign, String key, String charset) throws Exception {
        String mysign = DigestUtils.md5Hex((byte[])MD5.getContentBytes(text = String.valueOf(text) + key, charset));
        if (mysign.equals(sign)) {
            return true;
        }
        return false;
    }

    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException("\u7b7e\u540d\u8fc7\u7a0b\u4e2d\u51fa\u73b0\u9519\u8bef,\u6307\u5b9a\u7684\u7f16\u7801\u96c6\u4e0d\u5bf9,\u60a8\u76ee\u524d\u6307\u5b9a\u7684\u7f16\u7801\u96c6\u662f:" + charset);
        }
    }
}
