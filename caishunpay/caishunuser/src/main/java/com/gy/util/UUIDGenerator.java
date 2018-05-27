/*
 * Decompiled with CFR 0_124.
 */
package com.gy.util;

import com.gy.util.DateUtil;

import java.io.PrintStream;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class UUIDGenerator {
    private static final String str62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int pixLen = 36;
    private static volatile int pixOne = 0;
    private static volatile int pixTwo = 0;
    private static volatile int pixThree = 0;
    private static volatile int pixFour = 0;
    private static final AtomicInteger ATOM_INT = new AtomicInteger(0);
    private static final int MAX_36 = 1679616;
    private static final char[] DIGITS64 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_".toCharArray();

    public static String getUUID() {
        String s = UUID.randomUUID().toString();
        return s.replace("-", "");
    }

    public static String getUUID_22() {
        UUID u = UUID.randomUUID();
        return String.valueOf(UUIDGenerator.toIDString(u.getMostSignificantBits())) + UUIDGenerator.toIDString(u.getLeastSignificantBits());
    }

    public static String getOrderIdByUUId() {
        return UUIDGenerator.getOrderIdByUUId("");
    }

    public static String getOrderIdByUUId(String prefix) {
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {
            hashCodeV = -hashCodeV;
        }
        return String.valueOf(prefix) + DateUtil.getCurrentDay() + String.format("%010d", hashCodeV);
    }

    public static String getOrderIdByUUId(int length) {
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {
            hashCodeV = -hashCodeV;
        }
        return String.valueOf(DateUtil.getCurrentDay()) + String.format(new StringBuilder("%0").append(length - 8).append("d").toString(), hashCodeV);
    }

    private static String toIDString(long l) {
        char[] buf = "00000000000".toCharArray();
        int length = 11;
        long least = 63L;
        do {
            buf[--length] = DIGITS64[(int) (l & least)];
        } while ((l >>>= 6) != 0L);
        return new String(buf);
    }

    public static final synchronized String create15() {
        StringBuilder sb = new StringBuilder(15);
        sb.append(Long.toHexString(System.currentTimeMillis()));
        if (++pixFour == 36) {
            pixFour = 0;
            if (++pixThree == 36) {
                pixThree = 0;
                if (++pixTwo == 36) {
                    pixTwo = 0;
                    if (++pixOne == 36) {
                        pixOne = 0;
                    }
                }
            }
        }
        return sb.append("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(pixOne)).append("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(pixTwo)).append("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(pixThree)).append("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(pixFour)).toString();
    }

    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        System.out.println(UUIDGenerator.getUUID());
        int i = 0;
        while (i < 10) {
            System.out.println(UUIDGenerator.getOrderIdByUUId("yejianwen"));
            ++i;
        }
        System.out.println(System.currentTimeMillis() - time);
    }
}
