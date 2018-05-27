/*
 * Decompiled with CFR 0_124.
 */
package com.trade.util;

import com.trade.util.Base64;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.net.URL;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import javax.crypto.Cipher;

public class RSAUtils {
    public static final String GC_PUBLIC_KEY_PATH = "/cert/gczf_rsa_public.pem";
    public static final String PRIVATE_KEY_PATH = "/cert/gypay_pkcs8_rsa_private_key.pem";
    public static final String HELIBAO_PUBLIC_KEY_PATH = "/cert/helibao/helibao_rsa_public.pem";
    public static final String HELIBAO_PRIVATE_KEY_PATH = "/cert/helibao/gypay_private_key_to_helibao.pem";
    public static final String QIYEPAY_PUBLIC_KEY_PATH = "/cert/qiyepay/qiye_public_key.pem";
    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "SHA1WithRSA";
    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static final int MAX_DECRYPT_BLOCK = 128;
    public static final String MD5_KEY = "12345";
    public static String absolutePath = RSAUtils.class.getResource("/").getPath();

    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] digest = RSAUtils.sha1(data);
        byte[] encryptData = RSAUtils.encryptByPrivateKey(digest, privateKey);
        return Base64.encode(encryptData);
    }

    private static byte[] sha1(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md = null;
        md = MessageDigest.getInstance("SHA-1");
        byte[] digest = md.digest(data);
        return digest;
    }

    public static String getMD5Str(String str) {
        return RSAUtils.getMD5Str(str, "UTF-8");
    }

    public static String getMD5Str(String str, String encode) {
        if (str == null) {
            return null;
        }
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes(encode));
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
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

    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
        byte[] encryptData;
        byte[] digest = RSAUtils.sha1(data);
        if (Arrays.equals(digest, encryptData = RSAUtils.decryptByPublicKey(Base64.decode(sign), publicKey))) {
            return true;
        }
        return false;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        int i = 0;
        while (i < src.length) {
            int v = src[i] & 255;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
            ++i;
        }
        return stringBuilder.toString();
    }

    public static String decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decode(privateKey);
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
        return new String(decryptedData, "UTF-8");
    }

    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws Exception {
        byte[] keyBytes = Base64.decode(publicKey);
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

    public static String encryptByPublicKey(byte[] data, String publicKey) throws Exception {
        byte[] keyBytes = Base64.decode(publicKey);
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
        return Base64.encode(encryptedData);
    }

    public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decode(privateKey);
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

    public static String loadPublicKey(String fileName) {
        BufferedReader br = null;
        try {
            String publickey;
            br = new BufferedReader(new FileReader(fileName));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                if (readLine.charAt(0) == '-') continue;
                sb.append(readLine);
                sb.append('\r');
            }
            byte[] buffer = Base64.decode(sb.toString());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            RSAPublicKey rsaPublicKey = (RSAPublicKey)keyFactory.generatePublic(keySpec);
            String string = publickey = Base64.encode(rsaPublicKey.getEncoded());
            return string;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                br.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String loadPrivateKey(String filename) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filename));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                if (readLine.charAt(0) == '-') continue;
                sb.append(readLine);
                sb.append('\r');
            }
            byte[] buffer = Base64.decode(sb.toString());
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] binaryData = ((RSAPrivateKey)keyFactory.generatePrivate(keySpec)).getEncoded();
            String string = Base64.encode(binaryData);
            return string;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                br.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static /* varargs */ String encrypt(String paramstr, String ... publicKeyPath) {
        String keyPath = "/cert/gczf_rsa_public.pem";
        if (publicKeyPath != null && publicKeyPath.length > 0) {
            keyPath = publicKeyPath[0];
        }
        String publickey = RSAUtils.loadPublicKey(String.valueOf(absolutePath) + keyPath);
        String cipherData = null;
        try {
            cipherData = RSAUtils.encryptByPublicKey(paramstr.getBytes("UTF-8"), publickey);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return cipherData;
    }

    public static /* varargs */ String decryptResponseData(String cipherData, String ... privateKeyPath) {
        String keyPath = "/cert/gypay_pkcs8_rsa_private_key.pem";
        if (privateKeyPath != null && privateKeyPath.length > 0) {
            keyPath = privateKeyPath[0];
        }
        String privatekey = RSAUtils.loadPrivateKey(String.valueOf(absolutePath) + keyPath);
        try {
            String result = RSAUtils.decryptByPrivateKey(Base64.decode(cipherData), privatekey);
            System.out.println("\u89e3\u5bc6\u7ed3\u679c:" + result);
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getTfbPublicKeyPath(String channelMchtNo) {
        return "/cert/tfb/" + channelMchtNo + "_gczf_rsa_public.pem";
    }

    public static String getTfbPrivateKeyPath(String channelMchtNo) {
        return "/cert/tfb/" + channelMchtNo + "_gypay_pkcs8_rsa_private_key.pem";
    }

    public static void main(String[] args) {
        String back = "GfNKfUxSyPdRYebq34IlzJ++n/l/GR/pMghmTQ3a7r3cJpHbemT4qwFRPTWUuXK7qOXiuHSGfXIY\nlQv9eZE5EwmIoE9v8rC0Zc7KUYDUoo5r8r+faAEBMcfAMcA/nYgpfIyUcSvf88dI+dyGxUjRKqhw\n+Ekp6SfPRjmsHipGhqBXooY0SETuB2jJcxgYXq1gIUXODEGS38ZX8/kEzIgd2UPh/i4eNBf7Zb3D\nYNgiI42qpXcrae82IMc+BZ9Z/kGHQ6yty/h8J8ykba7lU3K0MvkkLknBNVm6Po0cPwj2wTSDcFtJ\nE57M7QzxHAwwvcV+hKQMV1VkRQHXqdAS1bI8qw==";
        RSAUtils.decryptResponseData(back, "/cert/qiyepay/qiye_public_key.pem");
    }
}
