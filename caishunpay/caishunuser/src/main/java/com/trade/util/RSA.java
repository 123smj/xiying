/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.ArrayUtils
 *  org.bouncycastle.jce.provider.BouncyCastleProvider
 */
package com.trade.util;

import com.trade.util.Base64;
import com.trade.util.Base64Helibao;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Enumeration;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.misc.BASE64Encoder;

public class RSA {
    public static final String NOPADDING = "RSA/NONE/NoPadding";
    public static final String RSANONEPKCS1PADDING = "RSA/NONE/PKCS1Padding";
    public static final String RSAECBPKCS1PADDING = "RSA/ECB/PKCS1Padding";
    public static final String PROVIDER = "BC";

    static {
        Security.addProvider((Provider) new BouncyCastleProvider());
    }

    public static boolean verifySign(byte[] data, byte[] sign, PublicKey publicKey) {
        try {
            Signature signature = Signature.getInstance("MD5withRSA");
            signature.initVerify(publicKey);
            signature.update(data);
            boolean result = signature.verify(sign);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("verifySign fail!", e);
        }
    }

    public static boolean verifySign(String data, String sign, PublicKey pubicKey) {
        try {
            byte[] dataByte = data.getBytes("UTF-8");
            byte[] signByte = Base64Helibao.decode(sign.getBytes("UTF-8"));
            return RSA.verifySign(dataByte, signByte, pubicKey);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("verifySign fail! data[" + data + "] sign[" + sign + "]", e);
        }
    }

    public static byte[] sign(byte[] data, PrivateKey key) {
        try {
            Signature signature = Signature.getInstance("MD5withRSA");
            signature.initSign(key);
            signature.update(data);
            return signature.sign();
        } catch (Exception e) {
            throw new RuntimeException("sign fail!", e);
        }
    }

    public static String sign(String data, PrivateKey key) {
        try {
            byte[] dataByte = data.getBytes("UTF-8");
            return new String(Base64.encode(RSA.sign(dataByte, key)));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("sign fail!", e);
        }
    }

    public static byte[] encrypt(byte[] data, Key key, String padding) {
        try {
            Cipher cipher = Cipher.getInstance(padding, "BC");
            cipher.init(1, key);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException("encrypt fail!", e);
        }
    }

    public static String encryptToBase64(String data, Key key, String padding) {
        try {
            return new String(Base64.encode(RSA.encrypt(data.getBytes("UTF-8"), key, padding)));
        } catch (Exception e) {
            throw new RuntimeException("encrypt fail!", e);
        }
    }

    public static byte[] decrypt(byte[] data, Key key, String padding) {
        try {
            Cipher cipher = Cipher.getInstance(padding, "BC");
            cipher.init(2, key);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException("encrypt fail!", e);
        }
    }

    public static String decryptFromBase64(String data, Key key, String padding) {
        try {
            return new String(RSA.decrypt(Base64Helibao.decode(data.getBytes()), key, padding), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("encrypt fail!", e);
        }
    }

    public static void createKeyPairs(int size) throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
        generator.initialize(size, new SecureRandom());
        KeyPair pair = generator.generateKeyPair();
        PublicKey pubKey = pair.getPublic();
        PrivateKey privKey = pair.getPrivate();
        byte[] pk = pubKey.getEncoded();
        byte[] privk = privKey.getEncoded();
        String strpk = new String(Base64Helibao.encodeBase64(pk));
        String strprivk = new String(Base64Helibao.encodeBase64(privk));
        System.out.println("\u516c\u94a5:" + Arrays.toString(pk));
        System.out.println("\u79c1\u94a5:" + Arrays.toString(privk));
        System.out.println("\u516c\u94a5Base64\u7f16\u7801:" + strpk);
        System.out.println("\u79c1\u94a5Base64\u7f16\u7801:" + strprivk);
    }

    public static PublicKey getPublicKey(String base64EncodePublicKey) throws Exception {
        KeyFactory keyf = KeyFactory.getInstance("RSA", "BC");
        X509EncodedKeySpec pubX509 = new X509EncodedKeySpec(Base64Helibao.decodeBase64(base64EncodePublicKey.getBytes()));
        PublicKey pubkey = keyf.generatePublic(pubX509);
        return pubkey;
    }

    public static PrivateKey getPrivateKey(String base64EncodePrivateKey) throws Exception {
        KeyFactory keyf = KeyFactory.getInstance("RSA", "BC");
        PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64Helibao.decodeBase64(base64EncodePrivateKey.getBytes()));
        PrivateKey privkey = keyf.generatePrivate(priPKCS8);
        return privkey;
    }

    public static byte[] encode(String encodeString, Key key, String padding) throws Exception {
        Cipher cipher = Cipher.getInstance(padding, "BC");
        cipher.init(1, key);
        byte[] bytes = encodeString.getBytes("UTF-8");
        byte[] encodedByteArray = new byte[]{};
        int i = 0;
        while (i < bytes.length) {
            byte[] subarray = ArrayUtils.subarray((byte[]) bytes, (int) i, (int) (i + 117));
            byte[] doFinal = cipher.doFinal(subarray);
            encodedByteArray = ArrayUtils.addAll((byte[]) encodedByteArray, (byte[]) doFinal);
            i += 117;
        }
        return encodedByteArray;
    }

    public static String encodeToBase64(String data, Key key, String padding) {
        try {
            return new String(Base64.encode(RSA.encode(data, key, padding)));
        } catch (Exception e) {
            throw new RuntimeException("encrypt fail!", e);
        }
    }

    public static String decode(byte[] decodeByteArray, Key key, String padding) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, NoSuchProviderException {
        Cipher cipher = Cipher.getInstance(padding, "BC");
        cipher.init(2, key);
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < decodeByteArray.length) {
            byte[] doFinal = cipher.doFinal(ArrayUtils.subarray((byte[]) decodeByteArray, (int) i, (int) (i + 128)));
            sb.append(new String(doFinal));
            i += 128;
        }
        return sb.toString();
    }

    public static String decodeFromBase64(String data, Key key, String padding) {
        try {
            return new String(RSA.decode(Base64Helibao.decode(data.getBytes()), key, padding).getBytes(), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("encrypt fail!", e);
        }
    }

    public static String getKeyString(Key key) throws Exception {
        byte[] keyBytes = key.getEncoded();
        String s = new BASE64Encoder().encode(keyBytes);
        return s;
    }

    public static String getKeyStringByCer(String path) throws Exception {
        CertificateFactory cff = CertificateFactory.getInstance("X.509");
        FileInputStream fis1 = new FileInputStream(path);
        Certificate cf = cff.generateCertificate(fis1);
        PublicKey pk1 = cf.getPublicKey();
        String key = RSA.getKeyString(pk1);
        System.out.println("public:\n" + key);
        return key;
    }

    public static String getKeyStringByPfx(String strPfx, String strPassword) {
        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            FileInputStream fis = new FileInputStream(strPfx);
            char[] nPassword = null;
            nPassword = strPassword == null || strPassword.trim().equals("") ? (char[]) null : strPassword.toCharArray();
            ks.load(fis, nPassword);
            fis.close();
            System.out.println("keystore type=" + ks.getType());
            Enumeration<String> enumas = ks.aliases();
            String keyAlias = null;
            if (enumas.hasMoreElements()) {
                keyAlias = enumas.nextElement();
                System.out.println("alias=[" + keyAlias + "]");
            }
            System.out.println("is key entry=" + ks.isKeyEntry(keyAlias));
            PrivateKey prikey = (PrivateKey) ks.getKey(keyAlias, nPassword);
            Certificate cert = ks.getCertificate(keyAlias);
            PublicKey pubkey = cert.getPublicKey();
            String basePrikey = RSA.getKeyString(prikey);
            System.out.println("cert class = " + cert.getClass().getName());
            System.out.println("cert = " + cert);
            System.out.println("public key = " + pubkey);
            System.out.println("private key = " + prikey);
            System.out.println("pubkey key = " + RSA.getKeyString(pubkey));
            System.out.println("prikey key = " + RSA.getKeyString(prikey));
            System.out.println("pubkey key length = " + RSA.getKeyString(pubkey).length());
            System.out.println("prikey key length = " + RSA.getKeyString(prikey).length());
            return basePrikey;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        String sign = RSA.sign("&Transfer&DF2017051500000296893621&C1800000002&0.01&CMBCHINA&6214830202948815&\u53f6\u5efa\u6587&B2C&&PAYER&true&\u7ed3\u7b97\u6b3e", RSA.getPrivateKey("MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAM+PcQN9JmrYJ+sKwI9kmBLAsBS6HHDT2Sgkh8BYbyTKovSMQat1II6l42HhUH6lr7bXwnRUw8I4qrNBZjz4cWZBi+vgkSL/1f1M/erW27t61DobvpgDpZQTtmQ7IDsiLuY7C7We+WwaBcddnju74ij3FPWCpgYBHGwdv5wwzRxdAgMBAAECgYBAzSreiPsujm/gDQpTeneUGz6eKgDpJOr+gnEzlyiUFwPLT+LM0hOpFZepHnxQHhB/CFu4kCJSB/kbYAa4cGSOlPo8zBLCfNajClZMLaKMAIb+0TmYNAnVcadC/4fXibzAW0zRS2/OK4H7wWUVEYyC66m+ieBaH5Jt/72+e6aYTQJBAPjjhGanLk22ml8i5+MzN94RBQStbGNxI6xtBXoKEIB2W/INPddZ877e7tknh+fVvctTZlE4Q5V1TT2ZL4wzke8CQQDVfaE9Cbc+aeg3Mb+Ap64tCK4WTHhWzHySN7VGTLdeF41ZjqTrIS7SSQyZOPOt/lMfFgXO0EnSdCqL+aexXFJzAkBeHyxi5bZNDVEzyS+IbEYkZKtRKYRj1tV2z4PSsxuqeRgsYXWRiyLye7w3wwtSUTKFQfTfojdsvf+H2/ZvPtFhAkAMygfctjZKAOIuXEaSmHjwrbJwF4il+n4D7F5ppbLeah7HnKn4g/ZgFowwqZ6/b5rfI9yZNRUXDGp4FC6di2BNAkB572zRbBT5Ot9mx9xVg6g/t0s3+LLEs1LBFEWQatRR9oC6qUzGNKTnZ/d5254ngnYXSRaQEZT698cJQV7kvmg4"));
        System.out.println(sign);
    }
}
