/*
 * Decompiled with CFR 0_124.
 */
package com.trade.enums;

public enum OprTypeEnum {
    PARENT("1", "\u7cfb\u7edf\u7ba1\u7406\u5458"),
    BRANCH("2", "\u4ee3\u7406\u5546");

    private String code;
    private String memo;

    private OprTypeEnum(String code, String memo) {
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
