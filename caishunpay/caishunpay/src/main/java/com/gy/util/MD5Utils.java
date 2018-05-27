/*
 * Decompiled with CFR 0_124.
 */
package com.gy.util;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class MD5Utils {
    public static String getKeyedDigest(String strSrc, String key) {
        try {
            strSrc = new String(strSrc.getBytes("GBK"), "UTF-8");
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(strSrc.getBytes("UTF-8"));
            String result = "";
            byte[] temp = md5.digest(key.getBytes("UTF-8"));
            int i = 0;
            while (i < temp.length) {
                result = String.valueOf(result) + Integer.toHexString(255 & temp[i] | -256).substring(6);
                ++i;
            }
            return result;
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getSignParam(Map<String, String> params) {
        StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
        MD5Utils.buildPayParams(buf, params, false);
        String result = buf.toString();
        return result;
    }

    public static String getSignParamNoSort(Map<String, String> params) {
        StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
        MD5Utils.buildPayParamsNoSort(buf, params, false);
        String result = buf.toString();
        return result;
    }

    public static void buildPayParams(StringBuilder sb, Map<String, String> payParams, boolean encoding) {
        ArrayList<String> keys = new ArrayList<String>(payParams.keySet());
        Collections.sort(keys);
        for (String key : keys) {
            sb.append(key).append("=");
            if (encoding) {
                sb.append(MD5Utils.urlEncode(payParams.get(key)));
            } else {
                sb.append(payParams.get(key));
            }
            sb.append("&");
        }
        sb.setLength(sb.length() - 1);
    }

    public static void buildPayParamsNoSort(StringBuilder sb, Map<String, String> payParams, boolean encoding) {
        ArrayList<String> keys = new ArrayList<String>(payParams.keySet());
        for (String key : keys) {
            sb.append(key).append("=");
            if (encoding) {
                sb.append(MD5Utils.urlEncode(payParams.get(key)));
            } else {
                sb.append(payParams.get(key));
            }
            sb.append("&");
        }
        sb.setLength(sb.length() - 1);
    }

    public static String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        }
        catch (Throwable e) {
            return str;
        }
    }

    public static String ecodeByMD5(String originstr) {
        String result = null;
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        if (originstr != null) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] source = originstr.getBytes("utf-8");
                md.update(source);
                byte[] tmp = md.digest();
                char[] str = new char[32];
                int i = 0;
                int j = 0;
                while (i < 16) {
                    byte b = tmp[i];
                    str[j++] = hexDigits[b >>> 4 & 15];
                    str[j++] = hexDigits[b & 15];
                    ++i;
                }
                result = new String(str);
            }
            catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static SecretKey createSecretKey(String algorithm) {
        SecretKey deskey = null;
        try {
            KeyGenerator keygen = KeyGenerator.getInstance(algorithm);
            deskey = keygen.generateKey();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return deskey;
    }

    public static String md5(String text) throws UnsupportedEncodingException {
        MessageDigest msgDigest = null;
        try {
            msgDigest = MessageDigest.getInstance("MD5");
            msgDigest.update(text.getBytes("UTF-8"));
        }
        catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("System doesn't support MD5 algorithm.");
        }
        catch (UnsupportedEncodingException e) {
            throw new UnsupportedEncodingException("encode error");
        }
        byte[] bytes = msgDigest.digest();
        String md5Str = new String();
        int i = 0;
        while (i < bytes.length) {
            byte tb = bytes[i];
            char tmpChar = (char)(tb >>> 4 & 15);
            char high = tmpChar >= '\n' ? (char)(97 + tmpChar - 10) : (char)(48 + tmpChar);
            md5Str = String.valueOf(md5Str) + high;
            tmpChar = (char)(tb & 15);
            char low = tmpChar >= '\n' ? (char)(97 + tmpChar - 10) : (char)(48 + tmpChar);
            md5Str = String.valueOf(md5Str) + low;
            ++i;
        }
        return md5Str;
    }

    public static void main(String[] args) {
        System.out.println(MD5Utils.ecodeByMD5("\u5e7f\u8c31"));
    }
}
