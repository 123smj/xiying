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
            Arrays.fill(temp, (byte)0);
            System.arraycopy(key, 0, temp, 0, key.length);
            key = temp;
        }
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        IvParameterSpec iv = new IvParameterSpec(new byte[16]);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(1, (Key)secretKey, iv);
        byte[] tgtBytes = cipher.doFinal(content);
        return tgtBytes;
    }

    public static byte[] decodeAES(byte[] key, byte[] content) throws Exception {
        int base = 16;
        if (key.length % base != 0) {
            int groups = key.length / base + (key.length % base != 0 ? 1 : 0);
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte)0);
            System.arraycopy(key, 0, temp, 0, key.length);
            key = temp;
        }
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        IvParameterSpec iv = new IvParameterSpec(new byte[16]);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(2, (Key)secretKey, iv);
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
        }
        finally {
            if (fis != null) {
                try {
                    fis.close();
                }
                catch (Exception exception) {}
            }
            if (bos != null) {
                try {
                    bos.close();
                }
                catch (Exception exception) {}
            }
            if (fos != null) {
                try {
                    fos.close();
                }
                catch (Exception exception) {}
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
        }
        finally {
            if (fis != null) {
                try {
                    fis.close();
                }
                catch (Exception exception) {}
            }
            if (bos != null) {
                try {
                    bos.close();
                }
                catch (Exception exception) {}
            }
            if (fos != null) {
                try {
                    fos.close();
                }
                catch (Exception exception) {}
            }
        }
    }

    public static void main(String[] args) throws Exception {
        String key = "1FDD2547FA4FB61F";
        String data = "versionId=001&businessType=1401&insCode=&merId=818310048160000&orderId=Qk2017042000001243847205&transDate=20170420150647&transAmount=0.01&transCurrency=156&cardByName=5Y+25bu65paH&cardByNo=6225768722687439&cardType=00&expireDate=2108&CVV=026&bankCode=null&openBankName=\u62db\u5546\u94f6\u884c&cerType=01&cerNumber=362324199107253051&mobile=15217928112&isAcceptYzm=00&pageNotifyUrl=&backNotifyUrl=&orderDesc=&instalTransFlag=01&instalTransNums=&dev=&fee=&signType=MD5&signData=D71475ABB871E82518305A2EE50A9DE7";
        byte[] resultByte = AesUtil.encodeAES(key.getBytes(), data.getBytes());
        String result = StringUtil.byte2hex(resultByte);
        System.out.println(result);
        System.out.println(new String(AesUtil.decodeAES(key.getBytes("GBK"), StringUtil.hex2byte(result.getBytes("GBK")))));
    }
}

