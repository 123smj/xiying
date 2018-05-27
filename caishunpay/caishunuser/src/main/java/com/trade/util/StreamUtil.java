/*
 * Decompiled with CFR 0_124.
 */
package com.trade.util;

import java.io.BufferedInputStream;
import java.io.InputStream;

public class StreamUtil {
    public static String getString(InputStream is, String charset) {
        StringBuffer buffer;
        block18:
        {
            BufferedInputStream bis;
            buffer = new StringBuffer();
            bis = new BufferedInputStream(is);
            byte[] bytes = new byte[1024];
            int c = 0;
            try {
                try {
                    while (-1 != (c = bis.read(bytes))) {
                        buffer.append(new String(bytes, 0, c, charset));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (bis != null) {
                            bis.close();
                        }
                        break block18;
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    break block18;
                }
            } catch (Throwable throwable) {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (bis != null) {
                        bis.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                throw throwable;
            }
            try {
                if (is != null) {
                    is.close();
                }
                if (bis != null) {
                    bis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return buffer.toString();
    }
}
