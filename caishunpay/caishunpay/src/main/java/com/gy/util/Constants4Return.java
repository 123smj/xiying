/*
 * Decompiled with CFR 0_124.
 */
package com.gy.util;

import java.util.HashMap;
import java.util.Map;

public class Constants4Return {
    public static final Map<String, String> STATUS_TRANSELATE = new HashMap<String, String>(){
        {
            this.put("00000", "\u4ea4\u6613\u6210\u529f");
            this.put("50010", "\u7cfb\u7edf\u6d41\u6c34\u91cd\u590d");
            this.put("50050", "\u540e\u7aef\u672a\u8fd4\u56de");
            this.put("80000", "\u6570\u636e\u4fdd\u5b58\u5f02\u5e38");
            this.put("50000", "\u672a\u77e5\u4ea4\u6613\u5f02\u5e38");
            this.put("50005", "\u4ea4\u6613\u4e0d\u5b58\u5728");
        }
    };
    public static final String SUCCESS = "00000";
    public static final String NO_TRADE = "99999";
    public static final String FUNCODE_ERROR = "99998";
    public static final String RUNTIME_ERROR = "99997";
    public static final String SYSTEM_BUSY = "99996";
    public static final String SESSION_TIMEOUT = "99995";
    public static final String DATA_BYTE_TOO_LONG = "90000";
    public static final String COMMIT_ERROR = "80000";
    public static final String ROLLBACK_ERROR = "80001";
    public static final String UPDATE_NO_AFFECTED = "80002";
    public static final String DATABASE_ERROR = "80003";
    public static final String MCHT_NOT_EXIST = "60002";
    public static final String TRADE_SN_ORI_NOT_EXIST = "50005";
    public static final String TRADE_SN_REPEAT = "50010";
    public static final String SIGN_ERROR = "50007";
    public static final String TRADE_ERROR = "50000";
    public static final String TRADE_BACK_EMPTY_ERROR = "50050";
    public static final String PIN_NOT_ALLOW_EMPTY = "50009";
    public static final String RETURN_ERROR = "50008";

}
