/*
 * Decompiled with CFR 0_124.
 */
package com.manage.bean;

public class RateInfo {
    private String owner_no;
    private String rate_type;
    private Double debit_card_fee_value;
    private Double debit_card_max_fee;
    private Double credit_card_fee_value;
    private Double wechat_fee_value;
    private Double alipay_fee_value;
    private Double qq_fee_value;
    private Double quickpay_fee_value;
    private Double netpay_fee_value;
    private Integer single_extra_fee;
    private String update_time;
    private String opr_id;

    public String getOwner_no() {
        return this.owner_no;
    }

    public void setOwner_no(String ownerNo) {
        this.owner_no = ownerNo;
    }

    public String getRate_type() {
        return this.rate_type;
    }

    public void setRate_type(String rateType) {
        this.rate_type = rateType;
    }

    public Double getDebit_card_fee_value() {
        return this.debit_card_fee_value;
    }

    public void setDebit_card_fee_value(Double debitCardFeeValue) {
        this.debit_card_fee_value = debitCardFeeValue;
    }

    public Double getDebit_card_max_fee() {
        return this.debit_card_max_fee;
    }

    public void setDebit_card_max_fee(Double debitCardMaxFee) {
        this.debit_card_max_fee = debitCardMaxFee;
    }

    public Double getCredit_card_fee_value() {
        return this.credit_card_fee_value;
    }

    public void setCredit_card_fee_value(Double creditCardFeeValue) {
        this.credit_card_fee_value = creditCardFeeValue;
    }

    public Double getWechat_fee_value() {
        return this.wechat_fee_value;
    }

    public void setWechat_fee_value(Double wechatFeeValue) {
        this.wechat_fee_value = wechatFeeValue;
    }

    public Double getAlipay_fee_value() {
        return this.alipay_fee_value;
    }

    public void setAlipay_fee_value(Double alipayFeeValue) {
        this.alipay_fee_value = alipayFeeValue;
    }

    public Double getQuickpay_fee_value() {
        return this.quickpay_fee_value;
    }

    public void setQuickpay_fee_value(Double quickpayFeeValue) {
        this.quickpay_fee_value = quickpayFeeValue;
    }

    public Double getNetpay_fee_value() {
        return this.netpay_fee_value;
    }

    public void setNetpay_fee_value(Double netpayFeeValue) {
        this.netpay_fee_value = netpayFeeValue;
    }

    public String getUpdate_time() {
        return this.update_time;
    }

    public void setUpdate_time(String updateTime) {
        this.update_time = updateTime;
    }

    public String getOpr_id() {
        return this.opr_id;
    }

    public void setOpr_id(String oprId) {
        this.opr_id = oprId;
    }

    public Double getQq_fee_value() {
        return this.qq_fee_value;
    }

    public void setQq_fee_value(Double qqFeeValue) {
        this.qq_fee_value = qqFeeValue;
    }

    public Integer getSingle_extra_fee() {
        return this.single_extra_fee;
    }

    public void setSingle_extra_fee(Integer singleExtraFee) {
        this.single_extra_fee = singleExtraFee;
    }

    public String toString() {
        return "RateInfo [alipay_fee_value=" + this.alipay_fee_value + ", credit_card_fee_value=" + this.credit_card_fee_value + ", debit_card_fee_value=" + this.debit_card_fee_value + ", debit_card_max_fee=" + this.debit_card_max_fee + ", netpay_fee_value=" + this.netpay_fee_value + ", opr_id=" + this.opr_id + ", owner_no=" + this.owner_no + ", qq_fee_value=" + this.qq_fee_value + ", quickpay_fee_value=" + this.quickpay_fee_value + ", rate_type=" + this.rate_type + ", single_extra_fee=" + this.single_extra_fee + ", update_time=" + this.update_time + ", wechat_fee_value=" + this.wechat_fee_value + "]";
    }
}
