/*
 * Decompiled with CFR 0_124.
 */
package com.trade.util;

import com.trade.util.ConvertUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Disguiser {
    public static final String ENCODE = "UTF-8";
    private static final String KEY = "8data998mnwepxugnk03-2zirb";

    public static String disguiseMD5(String message) {
        if (message == null) {
            return null;
        }
        return Disguiser.disguiseMD5(message, "UTF-8");
    }

    public static String disguise(String message) {
        return Disguiser.disguise(String.valueOf(message) + "8data998mnwepxugnk03-2zirb", "UTF-8");
    }

    public static String disguise(String message, String encoding) {
        byte[] value;
        message = message.trim();
        try {
            value = message.getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            value = message.getBytes();
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        return ConvertUtils.toHex(md.digest(value));
    }

    public static String disguiseMD5(String message, String encoding) {
        byte[] value;
        if (message == null || encoding == null) {
            return null;
        }
        message = message.trim();
        try {
            value = message.getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            value = message.getBytes();
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        return ConvertUtils.toHex(md.digest(value));
    }

    public static void main(String[] args) {
    }
}
