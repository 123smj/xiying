/*
 * Decompiled with CFR 0_124.
 */
package com.trade.enums;

public enum DfStateEnum {
    SUCCESS("00", "\u4ee3\u4ed8\u6210\u529f"),
    NOTPAY("09", "\u672a\u4ee3\u4ed8"),
    PAYING("01", "\u4ee3\u4ed8\u4e2d"),
    PAYERROR("02", "\u4ee3\u4ed8\u5931\u8d25");
    
    private String code;
    private String memo;

    private DfStateEnum(String code, String memo) {
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
