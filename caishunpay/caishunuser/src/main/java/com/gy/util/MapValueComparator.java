/*
 * Decompiled with CFR 0_124.
 */
package com.gy.util;

import java.util.Comparator;
import java.util.Map;

public class MapValueComparator
        implements Comparator<Map.Entry<String, String>> {
    @Override
    public int compare(Map.Entry<String, String> me1, Map.Entry<String, String> me2) {
        return me1.getValue().compareTo(me2.getValue());
    }
}
