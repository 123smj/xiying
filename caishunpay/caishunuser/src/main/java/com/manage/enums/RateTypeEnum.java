/*
 * Decompiled with CFR 0_124.
 */
package com.manage.enums;

public enum RateTypeEnum {
    MCHT_RATE("0", "\u5546\u6237\u8d39\u7387"),
    COMPANY_RATE("1", "\u4ee3\u7406\u5546\u8d39\u7387"),
    CHANNEL_RATE("2", "\u901a\u9053\u8d39\u7387");

    private String code;
    private String memo;

    private RateTypeEnum(String code, String memo) {
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
