/*
 * Decompiled with CFR 0_124.
 */
package com.trade.util;

import com.trade.util.Disguiser;

import java.io.UnsupportedEncodingException;

public class Base64Helibao {
    static final int CHUNK_SIZE = 76;
    static final byte[] CHUNK_SEPARATOR = "\r\n".getBytes();
    static final int BASELENGTH = 255;
    static final int LOOKUPLENGTH = 64;
    static final int EIGHTBIT = 8;
    static final int SIXTEENBIT = 16;
    static final int TWENTYFOURBITGROUP = 24;
    static final int FOURBYTE = 4;
    static final int SIGN = -128;
    static final byte PAD = 61;
    private static byte[] base64Alphabet = new byte[255];
    private static byte[] lookUpBase64Alphabet = new byte[64];

    static {
        int i = 0;
        while (i < 255) {
            Base64Helibao.base64Alphabet[i] = -1;
            ++i;
        }
        i = 90;
        while (i >= 65) {
            Base64Helibao.base64Alphabet[i] = (byte) (i - 65);
            --i;
        }
        i = 122;
        while (i >= 97) {
            Base64Helibao.base64Alphabet[i] = (byte) (i - 97 + 26);
            --i;
        }
        i = 57;
        while (i >= 48) {
            Base64Helibao.base64Alphabet[i] = (byte) (i - 48 + 52);
            --i;
        }
        Base64Helibao.base64Alphabet[43] = 62;
        Base64Helibao.base64Alphabet[47] = 63;
        i = 0;
        while (i <= 25) {
            Base64Helibao.lookUpBase64Alphabet[i] = (byte) (65 + i);
            ++i;
        }
        i = 26;
        int j = 0;
        while (i <= 51) {
            Base64Helibao.lookUpBase64Alphabet[i] = (byte) (97 + j);
            ++i;
            ++j;
        }
        i = 52;
        j = 0;
        while (i <= 61) {
            Base64Helibao.lookUpBase64Alphabet[i] = (byte) (48 + j);
            ++i;
            ++j;
        }
        Base64Helibao.lookUpBase64Alphabet[62] = 43;
        Base64Helibao.lookUpBase64Alphabet[63] = 47;
    }

    public static void main(String[] args) {
    }

    private static boolean isBase64(byte octect) {
        if (octect == 61) {
            return true;
        }
        if (base64Alphabet[octect] == -1) {
            return false;
        }
        return true;
    }

    public static boolean isArrayByteBase64(byte[] arrayOctect) {
        int length = (arrayOctect = Base64Helibao.discardWhitespace(arrayOctect)).length;
        if (length == 0) {
            return true;
        }
        int i = 0;
        while (i < length) {
            if (!Base64Helibao.isBase64(arrayOctect[i])) {
                return false;
            }
            ++i;
        }
        return true;
    }

    public static byte[] encodeBase64(byte[] binaryData) {
        return Base64Helibao.encodeBase64(binaryData, false);
    }

    public static byte[] encodeBase64Chunked(byte[] binaryData) {
        return Base64Helibao.encodeBase64(binaryData, true);
    }

    public static byte[] decode(byte[] pArray) {
        return Base64Helibao.decodeBase64(pArray);
    }

    public static byte[] encodeBase64(byte[] binaryData, boolean isChunked) {
        byte val2;
        byte val1;
        int lengthDataBits = binaryData.length * 8;
        int fewerThan24bits = lengthDataBits % 24;
        int numberTriplets = lengthDataBits / 24;
        byte[] encodedData = null;
        int encodedDataLength = 0;
        int nbrChunks = 0;
        encodedDataLength = fewerThan24bits != 0 ? (numberTriplets + 1) * 4 : numberTriplets * 4;
        if (isChunked) {
            nbrChunks = CHUNK_SEPARATOR.length == 0 ? 0 : (int) Math.ceil((float) encodedDataLength / 76.0f);
            encodedDataLength += nbrChunks * CHUNK_SEPARATOR.length;
        }
        encodedData = new byte[encodedDataLength];
        byte k = 0;
        byte l = 0;
        byte b1 = 0;
        byte b2 = 0;
        byte b3 = 0;
        int encodedIndex = 0;
        int dataIndex = 0;
        int i = 0;
        int nextSeparatorIndex = 76;
        int chunksSoFar = 0;
        i = 0;
        while (i < numberTriplets) {
            dataIndex = i * 3;
            b1 = binaryData[dataIndex];
            b2 = binaryData[dataIndex + 1];
            b3 = binaryData[dataIndex + 2];
            l = (byte) (b2 & 15);
            k = (byte) (b1 & 3);
            val1 = (b1 & -128) == 0 ? (byte) (b1 >> 2) : (byte) (b1 >> 2 ^ 192);
            val2 = (b2 & -128) == 0 ? (byte) (b2 >> 4) : (byte) (b2 >> 4 ^ 240);
            byte val3 = (b3 & -128) == 0 ? (byte) (b3 >> 6) : (byte) (b3 >> 6 ^ 252);
            encodedData[encodedIndex] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex + 1] = lookUpBase64Alphabet[val2 | k << 4];
            encodedData[encodedIndex + 2] = lookUpBase64Alphabet[l << 2 | val3];
            encodedData[encodedIndex + 3] = lookUpBase64Alphabet[b3 & 63];
            if (isChunked && (encodedIndex += 4) == nextSeparatorIndex) {
                System.arraycopy(CHUNK_SEPARATOR, 0, encodedData, encodedIndex, CHUNK_SEPARATOR.length);
                nextSeparatorIndex = 76 * (chunksSoFar + 1) + ++chunksSoFar * CHUNK_SEPARATOR.length;
                encodedIndex += CHUNK_SEPARATOR.length;
            }
            ++i;
        }
        dataIndex = i * 3;
        if (fewerThan24bits == 8) {
            b1 = binaryData[dataIndex];
            k = (byte) (b1 & 3);
            val1 = (b1 & -128) == 0 ? (byte) (b1 >> 2) : (byte) (b1 >> 2 ^ 192);
            encodedData[encodedIndex] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex + 1] = lookUpBase64Alphabet[k << 4];
            encodedData[encodedIndex + 2] = 61;
            encodedData[encodedIndex + 3] = 61;
        } else if (fewerThan24bits == 16) {
            b1 = binaryData[dataIndex];
            b2 = binaryData[dataIndex + 1];
            l = (byte) (b2 & 15);
            k = (byte) (b1 & 3);
            val1 = (b1 & -128) == 0 ? (byte) (b1 >> 2) : (byte) (b1 >> 2 ^ 192);
            val2 = (b2 & -128) == 0 ? (byte) (b2 >> 4) : (byte) (b2 >> 4 ^ 240);
            encodedData[encodedIndex] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex + 1] = lookUpBase64Alphabet[val2 | k << 4];
            encodedData[encodedIndex + 2] = lookUpBase64Alphabet[l << 2];
            encodedData[encodedIndex + 3] = 61;
        }
        if (isChunked && chunksSoFar < nbrChunks) {
            System.arraycopy(CHUNK_SEPARATOR, 0, encodedData, encodedDataLength - CHUNK_SEPARATOR.length, CHUNK_SEPARATOR.length);
        }
        return encodedData;
    }

    public static byte[] decodeBase64(byte[] base64Data) {
        if ((base64Data = Base64Helibao.discardNonBase64(base64Data)).length == 0) {
            return new byte[0];
        }
        int numberQuadruple = base64Data.length / 4;
        byte[] decodedData = null;
        byte b1 = 0;
        byte b2 = 0;
        byte b3 = 0;
        byte b4 = 0;
        byte marker0 = 0;
        byte marker1 = 0;
        int encodedIndex = 0;
        int dataIndex = 0;
        int lastData = base64Data.length;
        while (base64Data[lastData - 1] == 61) {
            if (--lastData != 0) continue;
            return new byte[0];
        }
        decodedData = new byte[lastData - numberQuadruple];
        int i = 0;
        while (i < numberQuadruple) {
            dataIndex = i * 4;
            marker0 = base64Data[dataIndex + 2];
            marker1 = base64Data[dataIndex + 3];
            b1 = base64Alphabet[base64Data[dataIndex]];
            b2 = base64Alphabet[base64Data[dataIndex + 1]];
            if (marker0 != 61 && marker1 != 61) {
                b3 = base64Alphabet[marker0];
                b4 = base64Alphabet[marker1];
                decodedData[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
                decodedData[encodedIndex + 1] = (byte) ((b2 & 15) << 4 | b3 >> 2 & 15);
                decodedData[encodedIndex + 2] = (byte) (b3 << 6 | b4);
            } else if (marker0 == 61) {
                decodedData[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
            } else if (marker1 == 61) {
                b3 = base64Alphabet[marker0];
                decodedData[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
                decodedData[encodedIndex + 1] = (byte) ((b2 & 15) << 4 | b3 >> 2 & 15);
            }
            encodedIndex += 3;
            ++i;
        }
        return decodedData;
    }

    static byte[] discardWhitespace(byte[] data) {
        byte[] groomedData = new byte[data.length];
        int bytesCopied = 0;
        int i = 0;
        while (i < data.length) {
            switch (data[i]) {
                case 9:
                case 10:
                case 13:
                case 32: {
                    break;
                }
                default: {
                    groomedData[bytesCopied++] = data[i];
                }
            }
            ++i;
        }
        byte[] packedData = new byte[bytesCopied];
        System.arraycopy(groomedData, 0, packedData, 0, bytesCopied);
        return packedData;
    }

    static byte[] discardNonBase64(byte[] data) {
        byte[] groomedData = new byte[data.length];
        int bytesCopied = 0;
        int i = 0;
        while (i < data.length) {
            if (Base64Helibao.isBase64(data[i])) {
                groomedData[bytesCopied++] = data[i];
            }
            ++i;
        }
        byte[] packedData = new byte[bytesCopied];
        System.arraycopy(groomedData, 0, packedData, 0, bytesCopied);
        return packedData;
    }

    public static byte[] encode(byte[] pArray) {
        return Base64Helibao.encodeBase64(pArray, false);
    }

    public static String encode(String str) throws UnsupportedEncodingException {
        String baseStr = new String(Base64Helibao.encode(str.getBytes("UTF-8")));
        String tempStr = Disguiser.disguise(str).toUpperCase();
        String result = String.valueOf(tempStr) + baseStr;
        return new String(Base64Helibao.encode(result.getBytes("UTF-8")));
    }

    public static String decode(String cryptoStr) throws UnsupportedEncodingException {
        if (cryptoStr.length() < 40) {
            return "";
        }
        try {
            String tempStr = new String(Base64Helibao.decode(cryptoStr.getBytes("UTF-8")));
            String result = tempStr.substring(40, tempStr.length());
            return new String(Base64Helibao.decode(result.getBytes("UTF-8")));
        } catch (ArrayIndexOutOfBoundsException ex) {
            return "";
        }
    }
}
