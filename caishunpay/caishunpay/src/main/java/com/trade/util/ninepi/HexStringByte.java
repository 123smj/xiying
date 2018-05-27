/*
 * Decompiled with CFR 0_124.
 */
package com.trade.util.ninepi;

public class HexStringByte {
    public static String stringToHex(String bin) {
        char[] digital = "0123456789ABCDEF".toCharArray();
        StringBuffer sb = new StringBuffer("");
        byte[] bs = bin.getBytes();
        int i = 0;
        while (i < bs.length) {
            int bit = (bs[i] & 240) >> 4;
            sb.append(digital[bit]);
            bit = bs[i] & 15;
            sb.append(digital[bit]);
            ++i;
        }
        return sb.toString();
    }

    public static String hexToString(String hex) {
        String digital = "0123456789ABCDEF";
        char[] hex2char = hex.toCharArray();
        byte[] bytes = new byte[hex.length() / 2];
        int i = 0;
        while (i < bytes.length) {
            int temp = digital.indexOf(hex2char[2 * i]) * 16;
            bytes[i] = (byte)((temp += digital.indexOf(hex2char[2 * i + 1])) & 255);
            ++i;
        }
        return new String(bytes);
    }

    public static String byteToHex(byte[] b) {
        String hs = "";
        String tmp = "";
        int n = 0;
        while (n < b.length) {
            tmp = Integer.toHexString(b[n] & 255);
            hs = tmp.length() == 1 ? String.valueOf(hs) + "0" + tmp : String.valueOf(hs) + tmp;
            ++n;
        }
        tmp = null;
        return hs.toUpperCase();
    }

    public static String byteToHex2(byte[] b) {
        String hs = "";
        String tmp = "";
        int n = 0;
        while (n < b.length) {
            tmp = Integer.toHexString(b[n] & 255);
            hs = tmp.length() == 1 ? String.valueOf(hs) + "0x0" + tmp : String.valueOf(hs) + "0x" + tmp;
            ++n;
        }
        tmp = null;
        return hs;
    }

    public static byte[] hexToByte(byte[] b) {
        if (b.length % 2 != 0) {
            throw new IllegalArgumentException("\ufffd\ufffd\ufffd\ubce4\ufffd\u0232\ufffd\ufffd\ufffd\u017c\ufffd\ufffd");
        }
        byte[] b2 = new byte[b.length / 2];
        int n = 0;
        while (n < b.length) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte)Integer.parseInt(item, 16);
            n += 2;
        }
        b = null;
        return b2;
    }
}
