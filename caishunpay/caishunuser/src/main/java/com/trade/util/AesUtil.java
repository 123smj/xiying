/*
 * Decompiled with CFR 0_124.
 */
package com.trade.util;

import com.gy.util.StringUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.net.ftp.FtpClient;

public class AesUtil {
    public static byte[] encodeAES(byte[] key, byte[] content) throws Exception {
        int base = 16;
        if (key.length % base != 0) {
            int groups = key.length / base + (key.length % base != 0 ? 1 : 0);
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(key, 0, temp, 0, key.length);
            key = temp;
        }
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        IvParameterSpec iv = new IvParameterSpec(new byte[16]);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(1, (Key) secretKey, iv);
        byte[] tgtBytes = cipher.doFinal(content);
        return tgtBytes;
    }

    public static byte[] decodeAES(byte[] key, byte[] content) throws Exception {
        int base = 16;
        if (key.length % base != 0) {
            int groups = key.length / base + (key.length % base != 0 ? 1 : 0);
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(key, 0, temp, 0, key.length);
            key = temp;
        }
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        IvParameterSpec iv = new IvParameterSpec(new byte[16]);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(2, (Key) secretKey, iv);
        byte[] tgtBytes = cipher.doFinal(content);
        return tgtBytes;
    }

    public static void encodeAESFile(byte[] key, String plainFilePath, String secretFilePath) throws Exception {
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(plainFilePath);
            bos = new ByteArrayOutputStream(fis.available());
            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, count);
            }
            bos.flush();
            byte[] bytes = AesUtil.encodeAES(key, bos.toByteArray());
            fos = new FileOutputStream(secretFilePath);
            fos.write(bytes);
            fos.flush();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception exception) {
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (Exception exception) {
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception exception) {
                }
            }
        }
    }

    public static void decodeAESFile(byte[] key, String plainFilePath, String secretFilePath) throws Exception {
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(secretFilePath);
            bos = new ByteArrayOutputStream(fis.available());
            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, count);
            }
            bos.flush();
            byte[] bytes = AesUtil.decodeAES(key, bos.toByteArray());
            fos = new FileOutputStream(plainFilePath);
            fos.write(bytes);
            fos.flush();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception exception) {
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (Exception exception) {
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception exception) {
                }
            }
        }
    }
}

