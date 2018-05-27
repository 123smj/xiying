/*
 * Decompiled with CFR 0_124.
 */
package com.trade.util;

import com.trade.util.Base64;
import com.trade.util.RSAUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSASignature {
    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    public static String sign(String content, String privateKey, String encode) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initSign(priKey);
            signature.update(content.getBytes(encode));
            byte[] signed = signature.sign();
            return Base64.encode(signed);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String sign(String content, String privateKey) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initSign(priKey);
            signature.update(content.getBytes());
            byte[] signed = signature.sign();
            return Base64.encode(signed);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        String sign = RSASignature.sign("&Transfer&DF2017051500000296893621&C1800000002&0.01&CMBCHINA&6214830202948815&\u53f6\u5efa\u6587&B2C&&PAYER&true&\u7ed3\u7b97\u6b3e", RSASignature.loadPrivateKeyByFile(String.valueOf(RSAUtils.absolutePath) + "/cert/helibao/gypay_private_key_to_helibao.pem"));
        System.out.println("sign:" + sign);
    }

    public static boolean doCheck(String content, String sign, String publicKey, String encode) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decode(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initVerify(pubKey);
            signature.update(content.getBytes(encode));
            boolean bverify = signature.verify(Base64.decode(sign));
            return bverify;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean doCheck(String content, String sign, String publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decode(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initVerify(pubKey);
            signature.update(content.getBytes());
            boolean bverify = signature.verify(Base64.decode(sign));
            return bverify;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String loadPrivateKeyByFile(String path) throws Exception {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                if (readLine.charAt(0) == '-') continue;
                sb.append(readLine);
            }
            br.close();
            return sb.toString();
        } catch (IOException e) {
            throw new Exception("\u79c1\u94a5\u6570\u636e\u8bfb\u53d6\u9519\u8bef");
        } catch (NullPointerException e) {
            throw new Exception("\u79c1\u94a5\u8f93\u5165\u6d41\u4e3a\u7a7a");
        }
    }
}
