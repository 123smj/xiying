/*
 * Decompiled with CFR 0_124.
 */
package com.trade.enums;

public enum TradeStateEnum {
    SUCCESS("SUCCESS", "\u4ea4\u6613\u6210\u529f"),
    REFUND("REFUND", "\u8f6c\u5165\u9000\u6b3e"),
    NOTPAY("NOTPAY", "\u672a\u652f\u4ed8"),
    CLOSED("CLOSED", "\u5df2\u5173\u95ed"),
    PRESUCCESS("PRESUCCESS", "\u9884\u4ea4\u6613\u6210\u529f"),
    PREERROR("PREERROR", "\u9884\u4ea4\u6613\u5931\u8d25"),
    PAYERROR("PAYERROR", "\u652f\u4ed8\u5931\u8d25");

    private String code;
    private String memo;

    private TradeStateEnum(String code, String memo) {
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
