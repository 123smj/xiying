/*
 * Decompiled with CFR 0_124.
 */
package com.trade.util.ninepi;

import java.util.HashMap;
import java.util.Map;

public enum SignFieldEnum {
    RPM_BIND_CARD("rpmBindCard", "memberId|orderId|idType|idNo|userName|phone|cardNo|cardType", "expireDate|cvn2", "\u805a\u5408\u652f\u4ed8-\u5feb\u6377\u7ed1\u5361"),
    RPM_UNBIND_CARD("rpmUnbindCard", "memberId|contractId", "", "\u805a\u5408\u652f\u4ed8-\u89e3\u7ed1\u5361"),
    RPM_BANK_LIST("rpmBankList", "", "", "\u805a\u5408\u652f\u4ed8-\u67e5\u8be2\u652f\u6301\u7ed1\u5361\u7684\u94f6\u884c\u5217\u8868"),
    RPM_BANK_PAYMENT("rpmBankPayment", "pageReturnUrl|notifyUrl|merchantName|orderTime|orderId|totalAmount|currency|bankAbbr|cardType|payType|validNum|validUnit|goodsName", "memberId|showUrl|goodsId|goodsDesc", "B2C/B2B\u652f\u4ed8"),
    RPM_MEMBER_CARD_LIST("rpmMemberCardList", "memberId", "", "\u805a\u5408\u652f\u4ed8-\u67e5\u8be2\u7528\u6237\u7684\u7ed1\u5361\u4fe1\u606f"),
    RPM_CARD_INFO("rpmCardInfo", "cardNo", "", "\u805a\u5408\u652f\u4ed8-\u67e5\u8be2\u94f6\u884c\u5361\u4fe1\u606f"),
    RPM_QUICK_PAY_SMS("rpmQuickPaySms", "contractId|memberId", "", "\u805a\u5408\u652f\u4ed8-\u77ed\u4fe1\u4e0b\u53d1"),
    RPM_QUICK_PAY("rpmQuickPay", "contractId|memberId|orderId|payType|amount|currency|orderTime|clientIP|validUnit|validNum|offlineNotifyUrl", "goodsName|goodsDesc", "\u805a\u5408\u652f\u4ed8-\u5feb\u6377\u652f\u4ed8(\u5546\u6237\u81ea\u9a8c\u77ed\u4fe1)"),
    RPM_QUICK_PAY_INIT("rpmQuickPayInit", "contractId|memberId|orderId|payType|amount|currency|orderTime|clientIP|validUnit|validNum|offlineNotifyUrl", "goodsName|goodsDesc", "\u805a\u5408\u652f\u4ed8-\u5feb\u6377\u652f\u4ed8\u9884\u4e0b\u5355"),
    RPM_QUICK_PAY_COMMIT("rpmQuickPayCommit", "contractId|memberId|orderId|payType|amount|currency|orderTime|clientIP|validUnit|validNum|offlineNotifyUrl|checkCode", "goodsName|goodsDesc", "\u805a\u5408\u652f\u4ed8-\u5feb\u6377\u652f\u4ed8\u786e\u8ba4(\u6211\u65b9\u9a8c\u8bc1\u77ed\u4fe1)"),
    RPM_PAY_QUERY("rpmPayQuery", "orderId", "", "\u805a\u5408\u652f\u4ed8-\u652f\u4ed8\u67e5\u8be2"),
    RPM_REFUND("rpmRefund", "merchantId|oriOrderId|orderId|refundAmount", "", "\u805a\u5408\u652f\u4ed8-\u9000\u6b3e"),
    RPM_REFUND_QUERY("rpmRefundQuery", "merchantId|oriOrderId|orderId", "", "\u805a\u5408\u652f\u4ed8-\u9000\u6b3e\u67e5\u8be2"),
    RPM_STATEMENT("rpmStatement", "acDate|type", "", "\u805a\u5408\u652f\u4ed8-\u5bf9\u8d26\u5355\u4e0b\u8f7d"),
    SINGLE_PAYMENT("singleTransfer", "mcSequenceNo|mcTransDateTime|orderNo|amount|cardNo|accName|crdType", "accType|lBnkNo|lBnkNam|validPeriod|cvv2|cellPhone|remark|bnkRsv|capUse|remark1|remark2|remark3", "\u5355\u7b14\u4ee3\u4ed8\u5230\u94f6\u884c\u5361"),
    SINGLE_PAYMENT_NEW("capSingleTransfer", "mcSequenceNo|mcTransDateTime|orderNo|amount|cardNo|accName|crdType|callBackUrl", "accType|lBnkNo|lBnkNam|validPeriod|cvv2|cellPhone|remark|bnkRsv|capUse|remark1|remark2|remark3", "\u5355\u7b14\u4ee3\u4ed8\u5230\u94f6\u884c\u5361"),
    SINGLE_COLLECTION("capSingleCollection", "mcSequenceNo|mcTransDateTime|orderNo|accType|cardNo|accName|amount|crdType|idInfo|idType", "lBnkNo|lBnkNam|validPeriod|cvv2|cellPhone|remark|bnkRsv|capUse|reqReserved1|reqReserved2", "\u5355\u7b14\u4ee3\u6536\u4ece\u94f6\u884c\u5361"),
    CAP_ORDER_QUERY("capOrderQuery", "mcSequenceNo|mcTransDateTime|orderNo|amount", "remark1|remark2", "\u4ee3\u6536\u4ed8\u8ba2\u5355\u67e5\u8be2"),
    MERCHANT_ACCOUNT_QUERY("merchantAccountQuery", "", "remark1|remark2", "\u8d26\u6237\u67e5\u8be2"),
    CAP_STATEMENT_QUERY("capStatementQuery", "txnDate|checkTyp|txTyp|curPag|pageNum", "reqReserved", "\u4ee3\u6536\u4ed8\u5bf9\u8d26\u5355\u67e5\u8be2"),
    CAP_STATEMENT_FILE_DOWN("capStatementFileDown", "acDate|type", "", "\u59d4\u6258\u6536\u4ed8\u5bf9\u8d26\u5355\u4e0b\u8f7d"),
    CAP_BATCH_QUERY("capBatchQuery", "batchNo|pageSize|pageNo", "ordSts", "\u4ee3\u6536\u4ed8\u6279\u91cf\u67e5\u8be2"),
    QRC_GENERATE_MERCHANT("qrCodeGenerateByMerchant", "service", "qrType|mercId", "\u626b\u7801\u4ed8-\u56fa\u7801"),
    QRC_GENERATE_ORDER("qrCodeGenerateByOrder", "orderId|amount|goodsName|goodsDesc", "offlineNotifyUrl|qrType", "\u626b\u7801\u4ed8-\u6d3b\u7801");
    
    String service;
    String notEmptyFields;
    String emptyFields;
    String desc;
    static final String COMMON = "charset|version|service|signType|merchantId|requestTime|requestId|";
    static Map<String, SignFieldEnum> SignFieldMap;

    static {
        SignFieldMap = new HashMap<String, SignFieldEnum>();
        SignFieldEnum[] arrsignFieldEnum = SignFieldEnum.values();
        int n = arrsignFieldEnum.length;
        int n2 = 0;
        while (n2 < n) {
            SignFieldEnum signFieldEnum = arrsignFieldEnum[n2];
            SignFieldMap.put(signFieldEnum.getService(), signFieldEnum);
            ++n2;
        }
    }

    private SignFieldEnum(String service, String notEmptyFields,String emptyFields,String desc) {
        this.service = service;
        this.notEmptyFields = (String)notEmptyFields;
        this.emptyFields = emptyFields;
        this.desc = desc;
    }

    public void setEmptyFields(String emptyFields) {
        this.emptyFields = emptyFields;
    }

    public String getEmptyFields() {
        return this.emptyFields;
    }

    public void setNotEmptyFields(String notEmptyFields) {
        this.notEmptyFields = notEmptyFields;
    }

    public String getNotEmptyFields() {
        return this.notEmptyFields;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getService() {
        return this.service;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return this.desc;
    }
}
