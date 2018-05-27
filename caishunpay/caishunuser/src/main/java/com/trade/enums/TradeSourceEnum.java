/*
 * Decompiled with CFR 0_124.
 */
package com.trade.enums;

public enum TradeSourceEnum {
    ALIPAY("1", "\u652f\u4ed8\u5b9d"),
    WEPAY("2", "\u5fae\u4fe1\u652f\u4ed8"),
    WEJSPAY("21", "\u5fae\u4fe1\u516c\u4f17\u53f7\u652f\u4ed8"),
    BAIDUPAY("3", "\u767e\u5ea6\u652f\u4ed8"),
    QQPAY("4", "\u624b\u673aQQ\u652f\u4ed8"),
    JDPAY("5", "\u4eac\u4e1c"),
    NETPAY("6", "\u7f51\u94f6\u652f\u4ed8"),
    QUICKPAY("7", "\u5feb\u6377\u652f\u4ed8"),
    BANLANCE_DAIFU("8", "\u4f59\u989d\u4ee3\u4ed8"),
    DAIKOU("K", "\u4ee3\u6263"),
    DIANZI_DAIFU("9", "\u57ab\u8d44\u4ee3\u4ed8");

    private String code;
    private String memo;

    private TradeSourceEnum(String code, String memo) {
        this.code = code;
        this.memo = (String) memo;
    }

    public static TradeSourceEnum get(String code) {
        TradeSourceEnum[] enums;
        TradeSourceEnum[] arrtradeSourceEnum = enums = TradeSourceEnum.values();
        int n = arrtradeSourceEnum.length;
        int n2 = 0;
        while (n2 < n) {
            TradeSourceEnum perEnum = arrtradeSourceEnum[n2];
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
