/*
 * Decompiled with CFR 0_124.
 */
package com.trade.util.mishua;

import java.io.PrintStream;
import java.security.MessageDigest;

public class MD5Util {
    private static final String[] hexDigits = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        int i = 0;
        while (i < b.length) {
            resultSb.append(MD5Util.byteToHexString(b[i]));
            ++i;
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return String.valueOf(hexDigits[d1]) + hexDigits[d2];
    }

    public static String MD5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = charsetname == null || "".equals(charsetname) ? MD5Util.byteArrayToHexString(md.digest(resultString.getBytes())) : MD5Util.byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
        }
        catch (Exception md) {
            // empty catch block
        }
        return resultString;
    }

    public static void main(String[] args) {
        String str = MD5Util.MD5Encode("000201503190041024", "UTF-8");
        System.out.print(str.toUpperCase());
    }
}
