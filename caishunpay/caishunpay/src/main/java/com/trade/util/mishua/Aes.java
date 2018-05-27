/*
 * Decompiled with CFR 0_124.
 */
package com.trade.util.mishua;

import com.trade.util.mishua.Base64Utils;
import java.io.PrintStream;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Aes {
    public static String encrypt(String sSrc, String sKey) throws Exception {
        if (sSrc != null && !"".equals(sSrc)) {
            if (sKey == null || "".equals(sKey.trim())) {
                sKey = "0102030405060708";
            }
            String ivParameter = "0102030405060708";
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] raw = sKey.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(1, (Key)skeySpec, iv);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
            return Base64Utils.encode(encrypted);
        }
        return "";
    }

    public static String decrypt(String sSrc, String sKey) throws Exception {
        try {
            if (sSrc != null && !"".equals(sSrc)) {
                if (sKey == null || "".equals(sKey.trim())) {
                    sKey = "0102030405060708";
                }
                String ivParameter = "0102030405060708";
                byte[] raw = sKey.getBytes("ASCII");
                SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
                cipher.init(2, (Key)skeySpec, iv);
                byte[] encrypted1 = Base64Utils.decode(sSrc);
                System.out.println(encrypted1.length);
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original, "utf-8");
                return originalString;
            }
            return "";
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
