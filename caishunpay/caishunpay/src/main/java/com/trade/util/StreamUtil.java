/*
 * Decompiled with CFR 0_124.
 */
package com.trade.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {
    public static String getString(InputStream inputStream, String encoding) {
        try {
            return IOUtils.toString(inputStream, encoding);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
