/*
 * Decompiled with CFR 0_124.
 */
package com.trade.enums;

public enum MchtStatusEnum {
    NORMAL("00", "\u6b63\u5e38"),
    ADD("01", "\u65b0\u589e\u5f85\u5ba1\u6838"),
    FREEZE("02", "\u51bb\u7ed3"),
    UPDATE("03", "\u4fee\u6539\u5f85\u5ba1\u6838"),
    FREEZE_TO_CHECK("04", "\u51bb\u7ed3\u5f85\u5ba1\u6838"),
    UPDATE_REFUSE("08", "\u4fee\u6539\u5ba1\u6838\u62d2\u7edd"),
    ADD_REFUSE("09", "\u65b0\u589e\u5ba1\u6838\u62d2\u7edd");
    
    private String code;
    private String memo;

    private MchtStatusEnum(String code, String memo) {
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

    public String toString() {
        return this.code;
    }
}
