/*
 * Decompiled with CFR 0_124.
 */
package com.trade.enums;

public enum ChannelInfoEnum {
    guangda("guangda", "\u5a01\u5bcc\u901a"),
    helibao("helibao", "\u5408\u5229\u5b9d"),
    mobao("mobao", "\u6469\u5b9d\u652f\u4ed8"),
    tfb("tfb", "\u56fd\u91c7"),
    wljr("wljr", "\u672a\u6765\u91d1\u878d"),
    hfbank("hfbank", "\u6052\u4e30\u94f6\u884c");

    private String code;
    private String memo;

    private ChannelInfoEnum(String code, String memo) {
        this.code = code;
        this.memo = (String) memo;
    }

    public static ChannelInfoEnum get(String code) {
        ChannelInfoEnum[] enums;
        ChannelInfoEnum[] arrchannelInfoEnum = enums = ChannelInfoEnum.values();
        int n = arrchannelInfoEnum.length;
        int n2 = 0;
        while (n2 < n) {
            ChannelInfoEnum perEnum = arrchannelInfoEnum[n2];
            if (perEnum.getCode().equals(code)) {
                return perEnum;
            }
            ++n2;
        }
        return null;
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
