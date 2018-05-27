/*
 * Decompiled with CFR 0_124.
 */
package com.trade.util;

public abstract class ConvertUtils {
    public static String toHex(byte[] input) {
        if (input == null) {
            return null;
        }
        StringBuffer output = new StringBuffer(input.length * 2);
        int i = 0;
        while (i < input.length) {
            int current = input[i] & 255;
            if (current < 16) {
                output.append("0");
            }
            output.append(Integer.toString(current, 16));
            ++i;
        }
        return output.toString();
    }
}
