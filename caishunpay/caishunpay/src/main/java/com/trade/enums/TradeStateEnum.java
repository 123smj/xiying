/*
 * Decompiled with CFR 0_124.
 */
package com.trade.enums;
//交易状况
public enum TradeStateEnum {
    SUCCESS("SUCCESS", "\u4ea4\u6613\u6210\u529f"),//交易成功
    REFUND("REFUND", "\u8f6c\u5165\u9000\u6b3e"),//转入退款
    NOTPAY("NOTPAY", "\u672a\u652f\u4ed8"),//未支付
    CLOSED("CLOSED", "\u5df2\u5173\u95ed"),//已关闭
    CHECKING("CHECK", "审核中"),
    REFUSE("REFUSE", "\u5ba1\u6838\u62d2\u7edd"),//审核拒绝
    PRESUCCESS("PRESUCCESS", "\u9884\u4ea4\u6613\u6210\u529f"),//预交易成功
    PREERROR("PREERROR", "\u9884\u4ea4\u6613\u5931\u8d25"),//预交易失败
    PAYERROR("PAYERROR", "\u652f\u4ed8\u5931\u8d25");//支付失败
    
    private String code;
    private String memo;

    private TradeStateEnum(String code, String memo) {
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
