/*
 * Decompiled with CFR 0_124.
 */
package com.trade.enums;

public enum CardTypeEnum {
    CREDIT("00", "\u8d37\u8bb0\u5361"),
    DEBIT("01", "\u501f\u8bb0\u5361"),
    QUASI_CREDIT("02", "\u51c6\u8d37\u8bb0\u5361");

    private String code;
    private String memo;

    private CardTypeEnum(String code, String memo) {
        this.code = code;
        this.memo = (String) memo;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
