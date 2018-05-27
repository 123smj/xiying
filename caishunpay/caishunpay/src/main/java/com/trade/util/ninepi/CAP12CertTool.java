/*
 * Decompiled with CFR 0_124.
 */
package com.trade.util.ninepi;

import com.trade.util.ninepi.SecurityException;
import com.trade.util.ninepi.SignedPack;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

public class CAP12CertTool {
    private static SignedPack signedPack;

    public CAP12CertTool(InputStream fileInputStream, String keyPass) throws SecurityException {
        signedPack = this.getP12(fileInputStream, keyPass);
    }

    public CAP12CertTool(String path, String keyPass) throws SecurityException, FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(new File(path));
        signedPack = this.getP12(fileInputStream, keyPass);
    }

    private SignedPack getP12(InputStream fileInputStream, String keyPass) throws SecurityException {
        SignedPack sp;
        sp = new SignedPack();
        try {
            try {
                KeyStore e = KeyStore.getInstance("PKCS12");
                char[] nPassword = null;
                nPassword = keyPass != null && !keyPass.trim().equals("") ? keyPass.toCharArray() : (char[])null;
                e.load(fileInputStream, nPassword);
                Enumeration<String> enum2 = e.aliases();
                String keyAlias = null;
                if (enum2.hasMoreElements()) {
                    keyAlias = enum2.nextElement();
                }
                PrivateKey priKey = (PrivateKey)e.getKey(keyAlias, nPassword);
                Certificate cert = e.getCertificate(keyAlias);
                PublicKey pubKey = cert.getPublicKey();
                sp.setCert((X509Certificate)cert);
                sp.setPubKey(pubKey);
                sp.setPriKey(priKey);
            }
            catch (Exception var18) {
                var18.printStackTrace();
                throw new SecurityException(var18.getMessage());
            }
        }
        finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                }
                catch (IOException iOException) {}
            }
        }
        return sp;
    }

    public X509Certificate getCert() {
        return signedPack.getCert();
    }

    public PublicKey getPublicKey() {
        return signedPack.getPubKey();
    }

    public PrivateKey getPrivateKey() {
        return signedPack.getPriKey();
    }

    public byte[] getSignData(byte[] indata) throws SecurityException {
        byte[] res = null;
        try {
            Signature e = Signature.getInstance("SHA256WITHRSA");
            e.initSign(this.getPrivateKey());
            e.update(indata);
            res = e.sign();
            return res;
        }
        catch (InvalidKeyException var4) {
            throw new SecurityException(var4.getMessage());
        }
        catch (NoSuchAlgorithmException var5) {
            throw new SecurityException(var5.getMessage());
        }
        catch (SignatureException var6) {
            throw new SecurityException(var6.getMessage());
        }
    }
}
