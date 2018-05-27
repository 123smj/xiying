/*
 * Decompiled with CFR 0_124.
 */
package com.trade.util;

public final class Base64 {
    private static final int BASELENGTH = 128;
    private static final int LOOKUPLENGTH = 64;
    private static final int TWENTYFOURBITGROUP = 24;
    private static final int EIGHTBIT = 8;
    private static final int SIXTEENBIT = 16;
    private static final int FOURBYTE = 4;
    private static final int SIGN = -128;
    private static final char PAD = '=';
    private static final boolean fDebug = false;
    private static final byte[] base64Alphabet = new byte[128];
    private static final char[] lookUpBase64Alphabet = new char[64];

    static {
        int i = 0;
        while (i < 128) {
            Base64.base64Alphabet[i] = -1;
            ++i;
        }
        i = 90;
        while (i >= 65) {
            Base64.base64Alphabet[i] = (byte) (i - 65);
            --i;
        }
        i = 122;
        while (i >= 97) {
            Base64.base64Alphabet[i] = (byte) (i - 97 + 26);
            --i;
        }
        i = 57;
        while (i >= 48) {
            Base64.base64Alphabet[i] = (byte) (i - 48 + 52);
            --i;
        }
        Base64.base64Alphabet[43] = 62;
        Base64.base64Alphabet[47] = 63;
        i = 0;
        while (i <= 25) {
            Base64.lookUpBase64Alphabet[i] = (char) (65 + i);
            ++i;
        }
        i = 26;
        int j = 0;
        while (i <= 51) {
            Base64.lookUpBase64Alphabet[i] = (char) (97 + j);
            ++i;
            ++j;
        }
        i = 52;
        j = 0;
        while (i <= 61) {
            Base64.lookUpBase64Alphabet[i] = (char) (48 + j);
            ++i;
            ++j;
        }
        Base64.lookUpBase64Alphabet[62] = 43;
        Base64.lookUpBase64Alphabet[63] = 47;
    }

    private static boolean isWhiteSpace(char octect) {
        if (octect != ' ' && octect != '\r' && octect != '\n' && octect != '\t') {
            return false;
        }
        return true;
    }

    private static boolean isPad(char octect) {
        if (octect == '=') {
            return true;
        }
        return false;
    }

    private static boolean isData(char octect) {
        if (octect < '' && base64Alphabet[octect] != -1) {
            return true;
        }
        return false;
    }

    public static String encode(byte[] binaryData) {
        byte val1;
        if (binaryData == null) {
            return null;
        }
        int lengthDataBits = binaryData.length * 8;
        if (lengthDataBits == 0) {
            return "";
        }
        int fewerThan24bits = lengthDataBits % 24;
        int numberTriplets = lengthDataBits / 24;
        int numberQuartet = fewerThan24bits != 0 ? numberTriplets + 1 : numberTriplets;
        char[] encodedData = null;
        encodedData = new char[numberQuartet * 4];
        byte k = 0;
        byte l = 0;
        byte b1 = 0;
        byte b2 = 0;
        byte b3 = 0;
        int encodedIndex = 0;
        int dataIndex = 0;
        int i = 0;
        while (i < numberTriplets) {
            b1 = binaryData[dataIndex++];
            b2 = binaryData[dataIndex++];
            b3 = binaryData[dataIndex++];
            l = (byte) (b2 & 15);
            k = (byte) (b1 & 3);
            byte val12 = (b1 & -128) == 0 ? (byte) (b1 >> 2) : (byte) (b1 >> 2 ^ 192);
            byte val2 = (b2 & -128) == 0 ? (byte) (b2 >> 4) : (byte) (b2 >> 4 ^ 240);
            byte val3 = (b3 & -128) == 0 ? (byte) (b3 >> 6) : (byte) (b3 >> 6 ^ 252);
            encodedData[encodedIndex++] = lookUpBase64Alphabet[val12];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[val2 | k << 4];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[l << 2 | val3];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[b3 & 63];
            ++i;
        }
        if (fewerThan24bits == 8) {
            b1 = binaryData[dataIndex];
            k = (byte) (b1 & 3);
            val1 = (b1 & -128) == 0 ? (byte) (b1 >> 2) : (byte) (b1 >> 2 ^ 192);
            encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[k << 4];
            encodedData[encodedIndex++] = 61;
            encodedData[encodedIndex++] = 61;
        } else if (fewerThan24bits == 16) {
            b1 = binaryData[dataIndex];
            b2 = binaryData[dataIndex + 1];
            l = (byte) (b2 & 15);
            k = (byte) (b1 & 3);
            val1 = (b1 & -128) == 0 ? (byte) (b1 >> 2) : (byte) (b1 >> 2 ^ 192);
            byte val2 = (b2 & -128) == 0 ? (byte) (b2 >> 4) : (byte) (b2 >> 4 ^ 240);
            encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[val2 | k << 4];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[l << 2];
            encodedData[encodedIndex++] = 61;
        }
        return new String(encodedData);
    }

    public static byte[] decode(String encoded) {
        if (encoded == null) {
            return null;
        }
        char[] base64Data = encoded.toCharArray();
        int len = Base64.removeWhiteSpace(base64Data);
        int numberQuadruple = len / 4;
        if (numberQuadruple == 0) {
            return new byte[0];
        }
        byte[] decodedData = null;
        byte b1 = 0;
        byte b2 = 0;
        byte b3 = 0;
        byte b4 = 0;
        char d1 = '\u0000';
        char d2 = '\u0000';
        char d3 = '\u0000';
        char d4 = '\u0000';
        int i = 0;
        int encodedIndex = 0;
        int dataIndex = 0;
        decodedData = new byte[numberQuadruple * 3];
        while (i < numberQuadruple - 1) {
            if (!(Base64.isData(d1 = base64Data[dataIndex++]) && Base64.isData(d2 = base64Data[dataIndex++]) && Base64.isData(d3 = base64Data[dataIndex++]) && Base64.isData(d4 = base64Data[dataIndex++]))) {
                return null;
            }
            b1 = base64Alphabet[d1];
            b2 = base64Alphabet[d2];
            b3 = base64Alphabet[d3];
            b4 = base64Alphabet[d4];
            decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
            decodedData[encodedIndex++] = (byte) ((b2 & 15) << 4 | b3 >> 2 & 15);
            decodedData[encodedIndex++] = (byte) (b3 << 6 | b4);
            ++i;
        }
        if (!Base64.isData(d1 = base64Data[dataIndex++]) || !Base64.isData(d2 = base64Data[dataIndex++])) {
            return null;
        }
        b1 = base64Alphabet[d1];
        b2 = base64Alphabet[d2];
        d3 = base64Data[dataIndex++];
        d4 = base64Data[dataIndex++];
        if (!Base64.isData(d3) || !Base64.isData(d4)) {
            if (Base64.isPad(d3) && Base64.isPad(d4)) {
                if ((b2 & 15) != 0) {
                    return null;
                }
                byte[] tmp = new byte[i * 3 + 1];
                System.arraycopy(decodedData, 0, tmp, 0, i * 3);
                tmp[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
                return tmp;
            }
            if (!Base64.isPad(d3) && Base64.isPad(d4)) {
                b3 = base64Alphabet[d3];
                if ((b3 & 3) != 0) {
                    return null;
                }
                byte[] tmp = new byte[i * 3 + 2];
                System.arraycopy(decodedData, 0, tmp, 0, i * 3);
                tmp[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
                tmp[encodedIndex] = (byte) ((b2 & 15) << 4 | b3 >> 2 & 15);
                return tmp;
            }
            return null;
        }
        b3 = base64Alphabet[d3];
        b4 = base64Alphabet[d4];
        decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
        decodedData[encodedIndex++] = (byte) ((b2 & 15) << 4 | b3 >> 2 & 15);
        decodedData[encodedIndex++] = (byte) (b3 << 6 | b4);
        return decodedData;
    }

    private static int removeWhiteSpace(char[] data) {
        if (data == null) {
            return 0;
        }
        int newSize = 0;
        int len = data.length;
        int i = 0;
        while (i < len) {
            if (!Base64.isWhiteSpace(data[i])) {
                data[newSize++] = data[i];
            }
            ++i;
        }
        return newSize;
    }
}
