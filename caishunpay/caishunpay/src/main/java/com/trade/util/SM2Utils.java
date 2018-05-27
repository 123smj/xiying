/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  cfca.sm2.signature.SM2PrivateKey
 *  cfca.sm2rsa.common.PKIException
 *  cfca.util.Base64
 *  cfca.util.CertUtil
 *  cfca.util.EnvelopeUtil
 *  cfca.util.KeyUtil
 *  cfca.util.SignatureUtil2
 *  cfca.util.cipher.lib.JCrypto
 *  cfca.util.cipher.lib.Session
 *  cfca.x509.certificate.X509Cert
 *  cfca.x509.certificate.X509CertHelper
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 */
package com.trade.util;

import cfca.sm2.signature.SM2PrivateKey;
import cfca.sm2rsa.common.PKIException;
import cfca.util.Base64;
import cfca.util.CertUtil;
import cfca.util.EnvelopeUtil;
import cfca.util.KeyUtil;
import cfca.util.SignatureUtil2;
import cfca.util.cipher.lib.JCrypto;
import cfca.util.cipher.lib.Session;
import cfca.x509.certificate.X509Cert;
import cfca.x509.certificate.X509CertHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gy.system.SysParamUtil;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;

public class SM2Utils {
    private static Session session;
    private static String absolutePath;

    static {
        absolutePath = SM2Utils.class.getResource("/").getPath();
        try {
            JCrypto.getInstance().initialize("JSOFT_LIB", (Object)null);
            session = JCrypto.getInstance().openSession("JSOFT_LIB");
        }
        catch (PKIException e) {
            e.printStackTrace();
        }
    }

    public static /* varargs */ String getSign(String context, String priKeyAbsPath, String priKeyPWD, String ... charset) {
        String sign = "";
        String realChar = "utf-8";
        if (charset != null && charset.length > 0) {
            realChar = charset[0];
        }
        try {
            SM2PrivateKey priKey = KeyUtil.getPrivateKeyFromSM2((String)(String.valueOf(absolutePath) + priKeyAbsPath), (String)priKeyPWD);
            sign = new String(new SignatureUtil2().p1SignMessage("SM3withSM2", context.getBytes(realChar), (PrivateKey)priKey, session));
        }
        catch (PKIException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sign;
    }

    public static String sign(String sign, String context) {
        GsonBuilder builder = new GsonBuilder();
        builder.disableHtmlEscaping();
        Gson gson = builder.create();
        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("sign", sign);
        paramMap.put("body", context);
        String signInfo = gson.toJson(paramMap);
        return signInfo;
    }

    public static /* varargs */ String encrypt(String signContext, String certAbsPath, String ... charset) {
        X509Cert cert = null;
        try {
            cert = X509CertHelper.parse((String)(String.valueOf(absolutePath) + certAbsPath));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (PKIException e) {
            e.printStackTrace();
        }
        X509Cert[] certs = new X509Cert[]{cert};
        byte[] encryptedData = null;
        String realChar = "utf-8";
        if (charset != null && charset.length > 0) {
            realChar = charset[0];
        }
        try {
            encryptedData = EnvelopeUtil.envelopeMessage((byte[])signContext.getBytes(realChar), (String)"SM4/CBC/PKCS7Padding", (X509Cert[])certs);
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (PKIException e) {
            e.printStackTrace();
        }
        String encodeText = null;
        try {
            encodeText = new String(encryptedData, realChar);
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeText;
    }

    public static /* varargs */ String dncrypt(String encryptContext, String priKeyAbsPath, String ... charset) {
        String priKeyPWD = SysParamUtil.getParam("cust0001_pwd");
        String decodeText = null;
        String realChar = "utf-8";
        if (charset != null && charset.length > 0) {
            realChar = charset[0];
        }
        try {
            SM2PrivateKey priKey = KeyUtil.getPrivateKeyFromSM2((String)(String.valueOf(absolutePath) + priKeyAbsPath), (String)priKeyPWD);
            X509Cert cert = CertUtil.getCertFromSM2((String)(String.valueOf(absolutePath) + priKeyAbsPath));
            byte[] sourceData = EnvelopeUtil.openEvelopedMessage((byte[])encryptContext.getBytes(realChar), (PrivateKey)priKey, (X509Cert)cert, (Session)session);
            decodeText = new String(sourceData, realChar);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return decodeText;
    }

    public static /* varargs */ boolean signCheck(String body, String sign, String certAbsPath, String ... charset) {
        boolean isSignOK = false;
        String realChar = "utf-8";
        if (charset != null && charset.length > 0) {
            realChar = charset[0];
        }
        try {
            X509Cert cert = X509CertHelper.parse((String)(String.valueOf(absolutePath) + certAbsPath));
            PublicKey pubKey = cert.getPublicKey();
            isSignOK = new SignatureUtil2().p1VerifyMessage("SM3withSM2", body.getBytes(realChar), sign.getBytes(realChar), pubKey, session);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return isSignOK;
    }

    public static /* varargs */ String decodeBase64(String data, String ... charset) {
        String result = "";
        String realChar = "utf-8";
        if (charset != null && charset.length > 0) {
            realChar = charset[0];
        }
        try {
            result = new String(Base64.decode((byte[])data.getBytes(realChar)), realChar);
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static /* varargs */ String encodeBase64(String data, String ... charset) {
        String result = "";
        String realChar = "utf-8";
        if (charset != null && charset.length > 0) {
            realChar = charset[0];
        }
        try {
            result = new String(Base64.encode((byte[])data.getBytes(realChar)), realChar);
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
