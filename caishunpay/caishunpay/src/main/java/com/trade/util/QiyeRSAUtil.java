/*
 * Decompiled with CFR 0_124.
 */
package com.trade.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class QiyeRSAUtil {
    private static final Integer KEY_SIZE = 1024;
    private static final Integer MAX_ENCRYPT_SIZE = 117;
    private static final Integer MAX_DECRYPT_SIZE = 128;

    public static void generateKeyPair(String path) throws IOException {
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyPairGenerator.initialize(KEY_SIZE);
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        byte[] publicKeyBytes = publicKey.getEncoded();
        byte[] privateKeyBytes = privateKey.getEncoded();
        String publicKeyString = QiyeRSAUtil.base64Encode(publicKeyBytes);
        String privateKeyString = QiyeRSAUtil.base64Encode(privateKeyBytes);
        byte[] publicKeyBase64Bytes = publicKeyString.getBytes();
        byte[] privateKeyBase64Bytes = privateKeyString.getBytes();
        File publicKeyFile = new File(String.valueOf(path) + "public.key");
        File privateKeyFile = new File(String.valueOf(path) + "private.key");
        FileOutputStream publicKeyFos = new FileOutputStream(publicKeyFile);
        FileOutputStream privateKeyFos = new FileOutputStream(privateKeyFile);
        publicKeyFos.write(publicKeyBase64Bytes);
        privateKeyFos.write(privateKeyBase64Bytes);
        publicKeyFos.flush();
        publicKeyFos.close();
        privateKeyFos.flush();
        privateKeyFos.close();
    }

    public static byte[] encrypt(String publicKeyStr, byte[] needEncrypt) {
        byte[] encrypt = null;
        try {
            PublicKey publicKey = QiyeRSAUtil.getPublicKey(publicKeyStr);
            encrypt = QiyeRSAUtil.encrypt(publicKey, needEncrypt);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return encrypt;
    }

    public static byte[] decrypt(String privateKeyStr, String needStr) {
        byte[] decrypt = null;
        try {
            byte[] needDecrypt = QiyeRSAUtil.base64Decode(needStr);
            PrivateKey privateKey = QiyeRSAUtil.getPrivateKey(privateKeyStr);
            decrypt = QiyeRSAUtil.decrypt(privateKey, needDecrypt);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return decrypt;
    }

    public static byte[] decryptPub(String privateKeyStr, String needStr) {
        byte[] decrypt = null;
        try {
            byte[] needDecrypt = QiyeRSAUtil.base64Decode(needStr);
            PublicKey publicKey = QiyeRSAUtil.getPublicKey(privateKeyStr);
            decrypt = QiyeRSAUtil.decrypt(publicKey, needDecrypt);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return decrypt;
    }

    public static byte[] sign(String privateKeyStr, byte[] needSign) {
        byte[] encrypt = null;
        try {
            PrivateKey privateKey = QiyeRSAUtil.getPrivateKey(privateKeyStr);
            encrypt = QiyeRSAUtil.encrypt(privateKey, needSign);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return encrypt;
    }

    public static byte[] verify(String publicKeyStr, byte[] needVerify) {
        byte[] decrypt = null;
        try {
            PublicKey publicKey = QiyeRSAUtil.getPublicKey(publicKeyStr);
            decrypt = QiyeRSAUtil.decrypt(publicKey, needVerify);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return decrypt;
    }

    private static byte[] encrypt(Key key, byte[] needEncryptBytes) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IOException {
        if (needEncryptBytes == null) {
            return null;
        }
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(1, key);
        ByteArrayInputStream iis = new ByteArrayInputStream(needEncryptBytes);
        ByteArrayOutputStream oos = new ByteArrayOutputStream();
        int restLength = needEncryptBytes.length;
        while (restLength > 0) {
            int readLength = restLength < MAX_ENCRYPT_SIZE ? restLength : MAX_ENCRYPT_SIZE;
            restLength -= readLength;
            byte[] readBytes = new byte[readLength];
            iis.read(readBytes);
            byte[] append = cipher.doFinal(readBytes);
            oos.write(append);
        }
        byte[] encryptedBytes = oos.toByteArray();
        return encryptedBytes;
    }

    private static byte[] decrypt(Key key, byte[] needDecryptBytes) throws IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        if (needDecryptBytes == null) {
            return null;
        }
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(2, key);
        ByteArrayInputStream iis = new ByteArrayInputStream(needDecryptBytes);
        ByteArrayOutputStream oos = new ByteArrayOutputStream();
        int restLength = needDecryptBytes.length;
        while (restLength > 0) {
            int readLength = restLength < MAX_DECRYPT_SIZE ? restLength : MAX_DECRYPT_SIZE;
            restLength -= readLength;
            byte[] readBytes = new byte[readLength];
            iis.read(readBytes);
            byte[] append = cipher.doFinal(readBytes);
            oos.write(append);
        }
        byte[] decryptedBytes = oos.toByteArray();
        return decryptedBytes;
    }

    private static PublicKey getPublicKey(String publicKeyStr) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] publicKeyBytes = QiyeRSAUtil.base64Decode(publicKeyStr);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    private static PrivateKey getPrivateKey(String privateKeyStr) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] privateKeyBytes = QiyeRSAUtil.base64Decode(privateKeyStr);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    public static String base64Encode(byte[] needEncode) {
        String encoded = null;
        if (needEncode != null) {
            encoded = new BASE64Encoder().encode(needEncode);
        }
        return encoded;
    }

    public static byte[] base64Decode(String needDecode) throws IOException {
        byte[] decoded = null;
        if (needDecode != null) {
            decoded = new BASE64Decoder().decodeBuffer(needDecode);
        }
        return decoded;
    }

    public static String readFile(String filePath, String charSet) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        try {
            FileChannel fileChannel = fileInputStream.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int)fileChannel.size());
            fileChannel.read(byteBuffer);
            byteBuffer.flip();
            String string = new String(byteBuffer.array(), charSet);
            return string;
        }
        finally {
            fileInputStream.close();
        }
    }

    public static void main(String[] args) {
        String result = null;
        result = new String(QiyeRSAUtil.decryptPub("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCBF4Q5gja23Vl9xQxP0WKSCK1sEWWrVETj+suUj872rpZEQPe1xrFb0IY0lbaO5vZZixs0ledjLWofrug28s6rd+xNdapECtM+kr35h+4L10scPMVlCwGDQImodbWnK+qJlK3dRzj4jdX/KhRzo5yIbY0P9TEPOVLy/wJdp68nYQIDAQAB", "MvvUbtOiuyqfeIXqDjfHA9umBYOsCl1dxrCjN7rkpEvu7EmoJt/ywI2F91nOGXeIIo05HZbRl3my7jp5PHPlBw8D0SLlB39SDjBfZWTMkGFHTOm3w752MMxq6XNnDUapUHtsh56RbixontTuipffLns3H2PUbfUz8+5OjtNurXE="));
        System.out.println(result);
    }
}
