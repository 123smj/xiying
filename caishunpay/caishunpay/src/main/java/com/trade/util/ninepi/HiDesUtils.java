/*
 * Decompiled with CFR 0_124.
 */
package com.trade.util.ninepi;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class HiDesUtils {
    public static String desEnCode(String srcStr) {
        try {
            Key localException = HiDesUtils.jdMethod_super("cputest".getBytes());
            Cipher localCipher = Cipher.getInstance("DES");
            localCipher.init(1, localException);
            return HiDesUtils.byteArr2HexStr(localCipher.doFinal(srcStr.getBytes()));
        }
        catch (Exception var6) {
            var6.printStackTrace();
            return "0";
        }
    }

    public static String desDeCode(String desStr) {
        try {
            Key localException = HiDesUtils.jdMethod_super("cputest".getBytes());
            Cipher localCipher = Cipher.getInstance("DES");
            localCipher.init(2, localException);
            return new String(localCipher.doFinal(HiDesUtils.hexStr2ByteArr(desStr)));
        }
        catch (Exception var6) {
            var6.printStackTrace();
            return "0";
        }
    }

    private static Key jdMethod_super(byte[] paramArrayOfByte) throws Exception {
        byte[] arrayOfByte = new byte[8];
        int localSecretKeySpec = 0;
        while (localSecretKeySpec < paramArrayOfByte.length && localSecretKeySpec < arrayOfByte.length) {
            arrayOfByte[localSecretKeySpec] = paramArrayOfByte[localSecretKeySpec];
            ++localSecretKeySpec;
        }
        SecretKeySpec var3 = new SecretKeySpec(arrayOfByte, "DES");
        return var3;
    }

    public static String byteArr2HexStr(byte[] paramArrayOfByte) throws Exception {
        int i = paramArrayOfByte.length;
        StringBuffer localStringBuffer = new StringBuffer(i * 2);
        int j = 0;
        while (j < i) {
            int k = paramArrayOfByte[j];
            while (k < 0) {
                k += 256;
            }
            if (k < 16) {
                localStringBuffer.append("0");
            }
            localStringBuffer.append(Integer.toString(k, 16));
            ++j;
        }
        return localStringBuffer.toString();
    }

    public static byte[] hexStr2ByteArr(String paramString) throws Exception {
        byte[] arrayOfByte1 = paramString.getBytes();
        int i = arrayOfByte1.length;
        byte[] arrayOfByte2 = new byte[i / 2];
        int j = 0;
        while (j < i) {
            String str = new String(arrayOfByte1, j, 2);
            arrayOfByte2[j / 2] = (byte)Integer.parseInt(str, 16);
            j += 2;
        }
        return arrayOfByte2;
    }
}
