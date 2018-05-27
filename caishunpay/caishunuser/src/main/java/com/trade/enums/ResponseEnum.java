/*
 * Decompiled with CFR 0_124.
 */
package com.trade.enums;

public enum ResponseEnum {
    SUCCESS("00000", "\u8bf7\u6c42\u6210\u529f"),
    SUCCESS_BUT_TIME_OUT("00001", "\u8bf7\u6c42\u8d85\u65f6\uff0c\u8bf7\u91cd\u8bd5"),
    FAIL_SYSTEM("10000", "\u672a\u77e5\u5f02\u5e38"),
    FAIL_PARAM("10001", "\u53c2\u6570\u9519\u8bef"),
    FAIL_UPDATE("10002", "\u66f4\u65b0\u5931\u8d25"),
    FAIL_DELETE("10003", "\u5220\u9664\u5931\u8d25"),
    FAIL_SIGN("10004", "\u7b7e\u540d\u9519\u8bef"),
    FAIL_ORDER_NO_REPEAT("10005", "\u6d41\u6c34\u53f7\u91cd\u590d"),
    FAIL_PAY_REPEAT("10015", "\u8bf7\u52ff\u91cd\u590d\u4ea4\u6613"),
    FAIL_ORDER_NOT_EXIST("10006", "\u4ea4\u6613\u4e0d\u5b58\u5728"),
    FAIL_MCHT_NOT_EXIST("10007", "\u5546\u6237\u4e0d\u5b58\u5728"),
    FAIL_INVALID_TRADE("10008", "\u65e0\u6548\u4ea4\u6613"),
    FAIL_MCHT_FREEZE("10009", "\u5546\u6237\u88ab\u51bb\u7ed3"),
    OUT_AMOUNT_LIMIT("11001", "\u8d85\u51fa\u9650\u989d"),
    OUT_DFDAYAMOUNT_LIMIT("11002", "\u8d85\u51fa\u4ee3\u4ed8\u5355\u5361\u65e5\u9650\u989d"),
    UNAUTHOR_ERROR("20001", "\u672a\u6388\u6743\u5546\u6237"),
    ERROR_CHANNEL("20002", "\u5546\u6237\u6e20\u9053\u672a\u77e5"),
    ERROR_UNSUPPORT_T0("20003", "\u5546\u6237\u6682\u4e0d\u652f\u6301T0"),
    ERROR_UNSUPPORT_T1("20004", "\u5546\u6237\u6682\u4e0d\u652f\u6301T1"),
    ERROR_UNSUPPORT("20009", "\u4ea4\u6613\u6682\u4e0d\u652f\u6301"),
    BACK_EXCEPTION("30001", "\u540e\u7aef\u8fd4\u56de\u5f02\u5e38"),
    BACK_SIGN_ERROR("30009", "\u901a\u9053\u7b7e\u540d\u5f02\u5e38"),
    BALANCE_EMPTY("90001", "\u8d26\u6237\u4f59\u989d\u4e0d\u8db3"),
    BALANCE_EXCEPTION("90009", "\u8d26\u6237\u5f02\u5e38"),
    PAY_TYPE_ERROR("40001", "\u652f\u4ed8\u65b9\u5f0f\u5f02\u5e38"),
    PAY_TYPE_UNSUPPORT("40002", "\u652f\u4ed8\u65b9\u5f0f\u6682\u672a\u5f00\u901a"),
    PAY_TIME_ERROR("40009", "\u975e\u8425\u4e1a\u65f6\u95f4\uff0c\u6682\u4e0d\u53d7\u7406"),
    DAIFU_ERROR("50001", "\u4ee3\u4ed8\u5931\u8d25"),
    IP_FORBIDDEN("20005", "ip_forbidden");

    private String code;
    private String memo;

    private ResponseEnum(String code, String memo) {
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
