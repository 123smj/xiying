/*
 * Decompiled with CFR 0_124.
 */
package com.trade.enums;

/**
 * 疑似: 扫码支付渠道id
 */
public enum ChannelInfoEnum {
    guangda("guangda", "\u5a01\u5bcc\u901a"),
    zhongxin("zhongxin", "\u4e2d\u4fe1\u94f6\u884c"),
    guoyin("guoyin", "\u56fd\u94f6\u652f\u4ed8"),
    helibao("helibao", "\u5408\u5229\u5b9d"),
    mobao("mobao", "\u6469\u5b9d\u652f\u4ed8"),
    tfb("tfb", "\u56fd\u91c7"),
    wljr("wljr", "\u672a\u6765\u91d1\u878d"),//未来金融
    yaku("yaku", "\u96c5\u9177\u4e00\u7801\u901a"),
    ninepi("ninepi", "\u4e5d\u6d3e\u652f\u4ed8"), //九派支付
    hfbank("hfbank", "\u6052\u4e30\u94f6\u884c"),
    zhangctong("zhangctong", "\u638c\u8d22\u901a"),
    haibei("haibei", "海贝支付"),
    hengfutong("hengfutong", "恒付通"),
    pufabank("pufabank", "\u6d66\u53d1\u79fb\u52a8\u652f\u4ed8");
    
    private String code;
    private String memo;

    private ChannelInfoEnum(String code, String memo) {
        this.code = code;
        this.memo = (String)memo;
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
