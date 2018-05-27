/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.bouncycastle.util.encoders.Base64
 */
package com.trade.util.ninepi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.bouncycastle.util.encoders.Base64;

public class Base64Utils {
    private static final int CACHE_SIZE = 1024;

    public static byte[] decode(String base64) throws Exception {
        return Base64.decode((byte[])base64.getBytes());
    }

    public static String encode(byte[] bytes) throws Exception {
        return new String(Base64.encode((byte[])bytes));
    }

    public static String encodeFile(String filePath) throws Exception {
        byte[] bytes = Base64Utils.fileToByte(filePath);
        return Base64Utils.encode(bytes);
    }

    public static void decodeToFile(String filePath, String base64) throws Exception {
        byte[] bytes = Base64Utils.decode(base64);
        Base64Utils.byteArrayToFile(bytes, filePath);
    }

    public static byte[] fileToByte(String filePath) throws Exception {
        byte[] data;
        data = new byte[]{};
        File file = new File(filePath);
        FileInputStream in = null;
        ByteArrayOutputStream out = null;
        if (file.exists()) {
            try {
                int nRead1;
                in = new FileInputStream(file);
                out = new ByteArrayOutputStream(2048);
                byte[] cache = new byte[1024];
                while ((nRead1 = in.read(cache)) != -1) {
                    out.write(cache, 0, nRead1);
                    out.flush();
                }
                data = out.toByteArray();
            }
            finally {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }
        }
        return data;
    }

    public static void byteArrayToFile(byte[] bytes, String filePath) throws Exception {
        ByteArrayInputStream in = null;
        FileOutputStream out = null;
        try {
            int nRead1;
            in = new ByteArrayInputStream(bytes);
            File destFile = new File(filePath);
            if (!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdirs();
            }
            destFile.createNewFile();
            out = new FileOutputStream(destFile);
            byte[] cache = new byte[1024];
            while ((nRead1 = in.read(cache)) != -1) {
                out.write(cache, 0, nRead1);
                out.flush();
            }
        }
        finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }
}
