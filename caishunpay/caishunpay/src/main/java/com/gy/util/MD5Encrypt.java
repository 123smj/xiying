/*
 * Decompiled with CFR 0_124.
 */
package com.gy.util;

import com.gy.util.StringUtil;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Encrypt {

    public static byte[] getMessageBytes(String strSrc, String... charset) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md;
        String realChar = "utf-8";
        if (charset != null && charset.length > 0) {
            realChar = charset[0];
        }
        byte[] bt = strSrc.getBytes(realChar);
        md = MessageDigest.getInstance("MD5");
        md.update(bt);
        return md.digest();
    }

    public static /* varargs */ String getMessageDigest(String strSrc, String ... charset) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = null;
        String strDes = null;
        String ALGO_MD5 = "MD5";
        String realChar = "utf-8";
        if (charset != null && charset.length > 0) {
            realChar = charset[0];
        }
        byte[] bt = strSrc.getBytes(realChar);
        md = MessageDigest.getInstance("MD5");
        md.update(bt);
        strDes = StringUtil.byte2hex(md.digest());
        return strDes;
    }

    public static /* varargs */ String getKeyedDigest(String strSrc, String key, String ... charset) {
        try {
            String realChar = "utf-8";
            if (charset != null && charset.length > 0) {
                realChar = charset[0];
            }
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(strSrc.getBytes(realChar));
            String result = "";
            byte[] temp = md5.digest(key.getBytes(realChar));
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

    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        int i = 0;
        while (i < bts.length) {
            tmp = Integer.toHexString(bts[i] & 255);
            if (tmp.length() == 1) {
                des = String.valueOf(des) + "0";
            }
            des = String.valueOf(des) + tmp;
            ++i;
        }
        return des;
    }
}
