/*
 * Decompiled with CFR 0_124.
 */
package com.trade.enums;

public enum AccountEnum {
    ACCOUNT_COMPLETE("00", "\u5df2\u5165\u8d26"),
    ACCOUNT_NEVER("01", "\u672a\u5165\u8d26"),
    ACCOUNT_ERROR("02", "\u5165\u8d26\u5931\u8d25");
    
    private String code;
    private String memo;

    private AccountEnum(String code, String memo) {
        this.code = code;
        this.memo = (String)memo;
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
