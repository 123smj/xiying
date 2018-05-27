/*
 * Decompiled with CFR 0_124.
 */
package com.trade.util.mishua;

public class Base64Utils {
    private static final char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
    private static int[] toInt = new int[128];

    static {
        int i = 0;
        while (i < ALPHABET.length) {
            Base64Utils.toInt[Base64Utils.ALPHABET[i]] = i++;
        }
    }

    public static String encode(byte[] buf) {
        int size = buf.length;
        char[] ar = new char[(size + 2) / 3 * 4];
        int a = 0;
        int i = 0;
        while (i < size) {
            byte b0 = buf[i++];
            byte b1 = i < size ? buf[i++] : 0;
            byte b2 = i < size ? buf[i++] : 0;
            int mask = 63;
            ar[a++] = ALPHABET[b0 >> 2 & mask];
            ar[a++] = ALPHABET[(b0 << 4 | (b1 & 255) >> 4) & mask];
            ar[a++] = ALPHABET[(b1 << 2 | (b2 & 255) >> 6) & mask];
            ar[a++] = ALPHABET[b2 & mask];
        }
        switch (size % 3) {
            case 1: {
                ar[--a] = 61;
            }
            case 2: {
                ar[--a] = 61;
            }
        }
        return new String(ar);
    }

    public static byte[] decode(String s) {
        int delta = s.endsWith("==") ? 2 : (s.endsWith("=") ? 1 : 0);
        byte[] buffer = new byte[s.length() * 3 / 4 - delta];
        int mask = 255;
        int index = 0;
        int i = 0;
        while (i < s.length()) {
            int c0 = toInt[s.charAt(i)];
            int c1 = toInt[s.charAt(i + 1)];
            buffer[index++] = (byte)((c0 << 2 | c1 >> 4) & mask);
            if (index >= buffer.length) {
                return buffer;
            }
            int c2 = toInt[s.charAt(i + 2)];
            buffer[index++] = (byte)((c1 << 4 | c2 >> 2) & mask);
            if (index >= buffer.length) {
                return buffer;
            }
            int c3 = toInt[s.charAt(i + 3)];
            buffer[index++] = (byte)((c2 << 6 | c3) & mask);
            i += 4;
        }
        return buffer;
    }
}
