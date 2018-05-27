/*
 * Decompiled with CFR 0_124.
 */
package com.trade.util.ninepi;

import java.util.List;
import java.util.Map;

public class MrpException
extends Exception {
    private Map<Integer, List<String>> messageMap;
    static final int MRPCODE = 1;
    static final int MPPCODE = 2;

    public MrpException(Map<Integer, List<String>> messageMap) {
        this.messageMap = messageMap;
    }

    public void setMessageMap(Map<Integer, List<String>> messageMap) {
        this.messageMap = messageMap;
    }

    public Map<Integer, List<String>> getMessageMap() {
        return this.messageMap;
    }
}
