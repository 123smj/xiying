/*
 * Decompiled with CFR 0_124.
 */
package com.trade.util.yaku;

import com.trade.util.yaku.RSAUtil;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    public static void main(String[] args) {
        MD5Util test = new MD5Util();
        String content = "$signString1234567890qwertyuiopasdfghjklzxc";
        System.out.println(content);
        System.out.println(test.getMD5(content, "UTF-8"));
    }

    public String assembleMD5Resource(String signResource, String key) {
        StringBuilder signResourceCode = new StringBuilder();
        signResourceCode.append(RSAUtil.removeNull(signResource));
        signResourceCode.append("&key=" + key);
        return signResourceCode.toString();
    }

    public static String removeNull(String value) {
        value = String.valueOf(value) + "&";
        StringBuffer valueBuffer = new StringBuffer();
        int startIndex = 0;
        do {
            int equalIndex;
            int tempIndex;
            if ((equalIndex = value.indexOf("=", startIndex + 1)) + 1 != (tempIndex = value.indexOf("&", startIndex + 1)) && tempIndex > 0 && !value.substring(equalIndex + 1, tempIndex).toUpperCase().equals("NULL")) {
                valueBuffer.append(value.substring(startIndex, tempIndex));
                valueBuffer.append("&");
            }
            if (tempIndex == value.length() - 1) break;
            startIndex = tempIndex + 1;
        } while (true);
        valueBuffer.deleteCharAt(valueBuffer.length() - 1);
        return valueBuffer.toString();
    }

    public String getMD5(String str, String encode) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes(encode));
        }
        catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        int i = 0;
        while (i < byteArray.length) {
            if (Integer.toHexString(255 & byteArray[i]).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(255 & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(255 & byteArray[i]));
            }
            ++i;
        }
        return md5StrBuff.toString();
    }

    public String qrMD5(String plainText, String key) {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update((String.valueOf(plainText) + key).getBytes());
            byte[] b = md.digest();
            StringBuffer buf = new StringBuffer("");
            int offset = 0;
            while (offset < b.length) {
                int i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
                ++offset;
            }
            re_md5 = buf.toString();
            return re_md5;
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
