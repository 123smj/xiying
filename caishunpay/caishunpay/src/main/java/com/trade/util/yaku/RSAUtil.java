/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.commons.codec.binary.Base64
 */
package com.trade.util.yaku;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;

public class RSAUtil {
    private String privateKey;
    private String publicKey;

    public RSAUtil() {
    }

    public RSAUtil(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPrivateKey() {
        return this.privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return this.publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    private PrivateKey privateKey() throws Exception {
        byte[] keyBytes = new byte[Base64.decodeBase64((String)this.privateKey).length];
        DataInputStream privateKeyFile = new DataInputStream(new ByteArrayInputStream(Base64.decodeBase64((String)this.privateKey)));
        privateKeyFile.readFully(keyBytes);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    public String sign(String value) throws Exception {
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initSign(this.privateKey());
        value = RSAUtil.removeNull(value);
        byte[] message = value.getBytes("UTF-8");
        signature.update(message);
        byte[] sigBytes = signature.sign();
        return Base64.encodeBase64String((byte[])sigBytes);
    }

    public String getRSA(String value) throws Exception {
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initSign(this.privateKey());
        value = RSAUtil.removeNull(value);
        byte[] message = value.getBytes("UTF-8");
        signature.update(message);
        byte[] sigBytes = signature.sign();
        return Base64.encodeBase64String((byte[])sigBytes);
    }

    public static String removeNull(String value) {
        System.out.println("remove data : " + value);
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

    public static void main(String[] args) throws Exception {
        RSAUtil rsa = new RSAUtil();
        System.out.println(rsa.dencrypt("eyJwYXJ0bmVyX2lkIjoiMjAwMDA2MjUzMTI2IiwicmVzcG9uc2VfY29kZSI6IklMTEVHQUxfQVJHVU1FTlQiLCJyZXNwb25zZV9tZXNzYWdlIjoi5Y C5pWw6Kej5a G5aSx6LSlLOivt ehruiupOWvhumSpemFjee9riIsInJlc3BvbnNlX3RpbWUiOiIyMDE3MDYyMDEzNTIyOCIsInNpZ25fdHlwZSI6IlJTQSIsInNpZ25fdmVyc2lvbiI6IjEuMCJ9", "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALqAF4eSBC+Ax4PzZBd0HXwqHW5HI+5E8EkGNPdDUfiUL2jyO9z8+TdsPubUZAqeSAWvUOVdWuqYFaaY1U8mmM1YdKTS84x/ZLEHxMHKEqLgkmG0XKPxtJHmtsQ0yJtCd1Ya1tr1+JS4pmxP2Uhmauvs1IwijgJxzKe+Qbs5JpX7AgMBAAECgYEAkGLu1ISVPPmkfWGHkijRUvmAA4qMJMG2jbKCexPheGOxOLyg3/vyLZheMnMoaJgWBmCuboSSCKFp0artKfQlwzB0h2ZvRcuj/uNEZnSYsRVivF/huHLhKWZLBqVCt5CtDx8H7h+X6SgGCmWGE546LofrR6TBU+O/+rOBC/F1h2ECQQDzZtG+6S0yXOKmK09NdgHe6tLHGCI+z9farVJRkBTsq4tp8BMvvldH3mPwzLfR2YtR9blSoDMpveNe//ensyhZAkEAxCdOH66ktnFV9VNya9MaIQdNr15btonOD3IN2n7I28MDeRKMrvo/s4cVpKo2Kbb2dOYSMafqcO/tMWocW1hmcwJBAKRDvsfty9/SSe/FCNcJDggoSCmvQuVLFazyDb0X1NdIimTrbbdp6LOLBb2sG15XR6v/fpuhnAXOrhUeAEBAK7kCQHDQGkJmcxzqugFKaPhShSiRxsAhiKXHQ9fmRMlxOZwK4Kh1XqwUCSb7fhsiOdxr+frph3U/mboygzx0RrLSeLkCQCNFirhPlaZrsul7Js8P+hOMiAg8ihfLcPS080i2Ai9Morzq8mbFQ5wTO5NfJCfJlrnwC0w7vxXVgiy6yRgnxvE="));
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        int n = 0;
        while (n < b.length) {
            stmp = Integer.toHexString(b[n] & 255);
            hs = stmp.length() == 1 ? String.valueOf(hs) + "0" + stmp : String.valueOf(hs) + stmp;
            if (n < b.length - 1) {
                hs = String.valueOf(hs);
            }
            ++n;
        }
        return hs.toUpperCase();
    }

    public static byte[] hex2byte(String str) {
        if (str == null) {
            return null;
        }
        int len = (str = str.trim()).length();
        if (len == 0 || len % 2 == 1) {
            return null;
        }
        byte[] b = new byte[len / 2];
        try {
            int i = 0;
            while (i < str.length()) {
                b[i / 2] = (byte)Integer.decode("0x" + str.substring(i, i + 2)).intValue();
                i += 2;
            }
            return b;
        }
        catch (Exception e) {
            return null;
        }
    }

    public String encrypt(String data, String key) throws Exception {
        byte[] keyBytes = RSAUtil.decryptBASE64(key);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(1, publicKey);
        byte[] bytes = data.getBytes("UTF-8");
        return Base64.encodeBase64String((byte[])cipher.doFinal(bytes));
    }

    public String dencrypt(String data, String key) throws Exception {
        byte[] keyBytes = RSAUtil.decryptBASE64(key);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(2, privateKey);
        return new String(cipher.doFinal(Base64.decodeBase64((String)data)), "UTF-8");
    }

    public static byte[] decryptBASE64(String key) throws Exception {
        return new BASE64Decoder().decodeBuffer(key);
    }
}
