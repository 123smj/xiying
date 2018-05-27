/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.commons.codec.binary.Base64
 */
package com.trade.util.yaku;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import org.apache.commons.codec.binary.Base64;

public class RSA {
    public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
    public static final String KEY_ALGORITHM = "RSA";
    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static final int MAX_DECRYPT_BLOCK = 128;
    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    public static Map<String, Object> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();
        HashMap<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put("RSAPublicKey", publicKey);
        keyMap.put("RSAPrivateKey", privateKey);
        return keyMap;
    }

    public static void main(String[] args) throws Exception {
        Map<String, Object> genKeyPair = RSA.genKeyPair();
        String base64publicKey = RSA.getPublicKey(genKeyPair);
        System.out.println("\u516c\u94a5 \n" + base64publicKey);
        String base64privateKey = RSA.getPrivateKey(genKeyPair);
        System.out.println("\u79c1\u94a5\n" + base64privateKey);
        String passwd = "cat123113";
        String charsetName = "utf-8";
        String encryptByPublicKey = Base64.encodeBase64String((byte[])RSA.encryptByPublicKey(passwd.getBytes(charsetName), base64publicKey));
        System.out.println("\u52a0\u5bc6\n" + encryptByPublicKey);
        byte[] decryptByPrivateKey = RSA.decryptByPrivateKey(Base64.decodeBase64((String)"Hdh0WNtZWyRZuDilPkDZv0HmREEgdMjfEkGi1Kvm68J45Ax592N6LnaieA9BQJ5pMS1ayIu2oJhvHhpt/iAWOU6k/mdnIjjdGFmZyYJTV9WETPOnu/xuSwcUOemxQ95QKKbv5MDQdko7/WJRTnC9tyMXmwJspj10XTn9BvFsxSM="), "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMFTKy0DE/nI9uzuQrTKh3FFk4h6pA6JJzCAYyLSf0NzTi+5i47RMbcxMeItNvW3OWj8+LAmRMmxjX4M118VoB3em6nTU0A0kyfwtqX8dE+4/C35/Y9fiH8orPSO/CF6oiRoCvSRP3C4wjs4fJLIPcv+0BBfZn+WIw0NsDW2OQ1lAgMBAAECgYA/8CzIb0stAp1AETD4sD8JZHR93+ngcNYIQX4IJ0w1163VNO2GJ4PkzZ1s631Q2O9g3MG3KID5oAJm3QJiDTnt88bnG6pP76X0Wn+E2wiFj3HJwMuT+b7i2eivjLSTpv597l9saIM6x3mbvXUF+Xhi2PUMY5sYRpFDEua4ue0LoQJBAOguHAMavFVTr0WkJ+O1Zkbr+HQDFQa5jn1eFgvsv5gz/BWv8vtlhyXDhULkxHQIhE5fkR8ItXYCTX0giwWD770CQQDVKJR60ssFYu1vy41us1qYBO1pSTYxzHz8AVZgJU+VL4mM1q4baZmDEcJWGig6LDqra8rSP4gVUI0PQi4jJTrJAkAY/nW3g9ZIXTTeC1jb83gqJFbfrkFCMxF6v3kiGX9alCYL85/1ni1ZTF35IIVhdFVB1pnZvGdEZ+UNlkZA9r4FAkBNCQZoQSg4QSF4ZsMtf8o86IL4qwnYA4Qj+0PBKZrSWsTGTovLwmVFdjSas4dYRsXJUAKT63v94AeqvQs5jmnpAkEAn3UpqlKwI5qatVd3vG7LpvxZGf667sZVYep4SoF1JNyA6L6VWZtot3GNpQvHbUWpvdlv27uIbQjgr5Y+5i+8OQ==");
        String string = new String(decryptByPrivateKey, "utf-8");
        System.out.println("\u89e3\u5bc6\u540e\n" + string);
        String text = "_input_charset=UTF-8&deposit_amount=361.00&deposit_status=SUCCESS&inner_trade_no=121462435441575330216&notify_id=201605050006405601&notify_time=20160505160420&notify_type=deposit_status_sync&outer_trade_no=052016050516061149749497&version=1.0";
        String sign = "S3uVxluuqjjp8hxKuQlaqWe4YFjRKm8t/G83uc7zvhSqgzFH9f65IIZDV7c3PGnP0w9d0UoqlGq4KVGimf5Q8eGHPhqtqs45zWBPdZ7gyBlc0H2bmqPJrZmhSXcw/YlZMiC7lmtFOjXv42jxSsMM/bYD9lVFqK/FmCzCrtbBdXg=";
        String charset = "UTF-8";
        base64publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDv0rdsn5FYPn0EjsCPqDyIsYRawNWGJDRHJBcdCldodjM5bpve+XYb4Rgm36F6iDjxDbEQbp/HhVPj0XgGlCRKpbluyJJt8ga5qkqIhWoOd/Cma1fCtviMUep21hIlg1ZFcWKgHQoGoNX7xMT8/0bEsldaKdwxOlv3qGxWfqNV5QIDAQAB";
        if (RSA.verify(text, sign, base64publicKey, charset)) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
    }

    public static String sign(String text, String privateKey, String charset) throws Exception {
        byte[] keyBytes = Base64.decodeBase64((String)privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initSign(privateK);
        signature.update(RSA.getContentBytes(text, charset));
        byte[] result = signature.sign();
        return Base64.encodeBase64String((byte[])result);
    }

    public static String sign(String text, PrivateKey privateKey, String charset) throws SignatureException, InvalidKeyException {
        try {
            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initSign(privateKey);
            signature.update(RSA.getContentBytes(text, charset));
            byte[] result = signature.sign();
            return Base64.encodeBase64String((byte[])result);
        }
        catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static boolean verify(String text, String sign, String publicKey, String charset) throws Exception {
        byte[] keyBytes = Base64.decodeBase64((String)publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initVerify(publicK);
        signature.update(RSA.getContentBytes(text, charset));
        return signature.verify(Base64.decodeBase64((String)sign));
    }

    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64((String)privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(2, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        int i = 0;
        while (inputLen - offSet > 0) {
            byte[] cache = inputLen - offSet > 128 ? cipher.doFinal(encryptedData, offSet, 128) : cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            out.write(cache, 0, cache.length);
            offSet = ++i * 128;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64((String)publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(2, publicK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        int i = 0;
        while (inputLen - offSet > 0) {
            byte[] cache = inputLen - offSet > 128 ? cipher.doFinal(encryptedData, offSet, 128) : cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            out.write(cache, 0, cache.length);
            offSet = ++i * 128;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64((String)publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(1, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        int i = 0;
        while (inputLen - offSet > 0) {
            byte[] cache = inputLen - offSet > 117 ? cipher.doFinal(data, offSet, 117) : cipher.doFinal(data, offSet, inputLen - offSet);
            out.write(cache, 0, cache.length);
            offSet = ++i * 117;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    public static byte[] encryptByPublicKey(byte[] data, Certificate cert) throws Exception {
        PublicKey uk = cert.getPublicKey();
        Cipher cipher = Cipher.getInstance(uk.getAlgorithm());
        cipher.init(1, uk);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        int i = 0;
        while (inputLen - offSet > 0) {
            byte[] cache = inputLen - offSet > 117 ? cipher.doFinal(data, offSet, 117) : cipher.doFinal(data, offSet, inputLen - offSet);
            out.write(cache, 0, cache.length);
            offSet = ++i * 117;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64((String)privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(1, privateK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        int i = 0;
        while (inputLen - offSet > 0) {
            byte[] cache = inputLen - offSet > 117 ? cipher.doFinal(data, offSet, 117) : cipher.doFinal(data, offSet, inputLen - offSet);
            out.write(cache, 0, cache.length);
            offSet = ++i * 117;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException("\u7b7e\u540d\u8fc7\u7a0b\u4e2d\u51fa\u73b0\u9519\u8bef,\u6307\u5b9a\u7684\u7f16\u7801\u96c6\u4e0d\u5bf9,\u60a8\u76ee\u524d\u6307\u5b9a\u7684\u7f16\u7801\u96c6\u662f:" + charset);
        }
    }

    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key)keyMap.get("RSAPrivateKey");
        return Base64.encodeBase64String((byte[])key.getEncoded());
    }

    public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key)keyMap.get("RSAPublicKey");
        return Base64.encodeBase64String((byte[])key.getEncoded());
    }
}
