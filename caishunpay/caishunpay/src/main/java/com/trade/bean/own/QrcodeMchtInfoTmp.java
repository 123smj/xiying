/*
 * Decompiled with CFR 0_124.
 */
package com.trade.bean.own;

import java.io.Serializable;

/**
 * 商户审核材料数据类
 */
public class QrcodeMchtInfoTmp
implements Serializable {
    private static final long serialVersionUID = 1L;
    private String mchtNo;
    private String mchtName;
    private String companyName;
    private String channelMchtNo;
    private String secretKey;
    private String crtTime;
    private String channel_id;
    private String company_id;
    private String phone;
    private String identity_no;
    private String bank_card_no;
    private String bank_name;
    private String card_name;
    private String bank_no;
    private String lisence_addr;
    private String return_native_qrcode;
    private Double debit_card_fee_value;
    private Double debit_card_max_fee;
    private Double credit_card_fee_value;
    private Double wechat_fee_value;
    private Double alipay_fee_value;
    private Double qq_fee_value;
    private Double quickpay_fee_value;
    private Double netpay_fee_value;
    private Double aliwap_fee_value;
    private Double wechatwap_fee_value;
    private String ip_addr;
    private String update_time;
    private String update_opr;
    private String status;
    private String trade_source_list;
    private Integer single_extra_fee;
    private String jump_flag;
    private String jump_group;
    private String is_t1_liq;
    private String email;
    private Integer dfcard_day_limit;
    private String refuse_reason;
    private String recheck_time;
    private String recheck_opr;
    private Double unipay_qrcode_fee_value;

    public Double getUnipay_qrcode_fee_value() {
        return unipay_qrcode_fee_value;
    }

    public void setUnipay_qrcode_fee_value(Double unipay_qrcode_fee_value) {
        this.unipay_qrcode_fee_value = unipay_qrcode_fee_value;
    }

    public String getRecheck_opr() {
        return this.recheck_opr;
    }

    public void setRecheck_opr(String recheckOpr) {
        this.recheck_opr = recheckOpr;
    }

    public Integer getDfcard_day_limit() {
        return this.dfcard_day_limit;
    }

    public void setDfcard_day_limit(Integer dfcardDayLimit) {
        this.dfcard_day_limit = dfcardDayLimit;
    }

    public String getMchtNo() {
        return this.mchtNo;
    }

    public void setMchtNo(String mchtNo) {
        this.mchtNo = mchtNo;
    }

    public String getMchtName() {
        return this.mchtName;
    }

    public void setMchtName(String mchtName) {
        this.mchtName = mchtName;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getChannelMchtNo() {
        return this.channelMchtNo;
    }

    public void setChannelMchtNo(String channelMchtNo) {
        this.channelMchtNo = channelMchtNo;
    }

    public String getSecretKey() {
        return this.secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getCrtTime() {
        return this.crtTime;
    }

    public void setCrtTime(String crtTime) {
        this.crtTime = crtTime;
    }

    public String getChannel_id() {
        return this.channel_id;
    }

    public void setChannel_id(String channelId) {
        this.channel_id = channelId;
    }

    public String getCompany_id() {
        return this.company_id;
    }

    public void setCompany_id(String companyId) {
        this.company_id = companyId;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdentity_no() {
        return this.identity_no;
    }

    public void setIdentity_no(String identityNo) {
        this.identity_no = identityNo;
    }

    public String getBank_card_no() {
        return this.bank_card_no;
    }

    public void setBank_card_no(String bankCardNo) {
        this.bank_card_no = bankCardNo;
    }

    public String getBank_name() {
        return this.bank_name;
    }

    public void setBank_name(String bankName) {
        this.bank_name = bankName;
    }

    public String getCard_name() {
        return this.card_name;
    }

    public void setCard_name(String cardName) {
        this.card_name = cardName;
    }

    public String getBank_no() {
        return this.bank_no;
    }

    public void setBank_no(String bankNo) {
        this.bank_no = bankNo;
    }

    public String getLisence_addr() {
        return this.lisence_addr;
    }

    public void setLisence_addr(String lisenceAddr) {
        this.lisence_addr = lisenceAddr;
    }

    public String getReturn_native_qrcode() {
        return this.return_native_qrcode;
    }

    public void setReturn_native_qrcode(String returnNativeQrcode) {
        this.return_native_qrcode = returnNativeQrcode;
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

    public String getIp_addr() {
        return this.ip_addr;
    }

    public void setIp_addr(String ipAddr) {
        this.ip_addr = ipAddr;
    }

    public String getUpdate_time() {
        return this.update_time;
    }

    public void setUpdate_time(String updateTime) {
        this.update_time = updateTime;
    }

    public String getUpdate_opr() {
        return this.update_opr;
    }

    public void setUpdate_opr(String updateOpr) {
        this.update_opr = updateOpr;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTrade_source_list() {
        return this.trade_source_list;
    }

    public void setTrade_source_list(String tradeSourceList) {
        this.trade_source_list = tradeSourceList;
    }

    public Integer getSingle_extra_fee() {
        return this.single_extra_fee;
    }

    public void setSingle_extra_fee(Integer singleExtraFee) {
        this.single_extra_fee = singleExtraFee;
    }

    public String getIs_t1_liq() {
        return this.is_t1_liq;
    }

    public void setIs_t1_liq(String isT1Liq) {
        this.is_t1_liq = isT1Liq;
    }

    public String getJump_flag() {
        return this.jump_flag;
    }

    public void setJump_flag(String jumpFlag) {
        this.jump_flag = jumpFlag;
    }

    public String getJump_group() {
        return this.jump_group;
    }

    public void setJump_group(String jumpGroup) {
        this.jump_group = jumpGroup;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getQq_fee_value() {
        return this.qq_fee_value;
    }

    public void setQq_fee_value(Double qqFeeValue) {
        this.qq_fee_value = qqFeeValue;
    }

    public String getRefuse_reason() {
        return this.refuse_reason;
    }

    public void setRefuse_reason(String refuseReason) {
        this.refuse_reason = refuseReason;
    }

    public String getRecheck_time() {
        return this.recheck_time;
    }

    public void setRecheck_time(String recheckTime) {
        this.recheck_time = recheckTime;
    }

    public Double getAliwap_fee_value() {
        return this.aliwap_fee_value;
    }

    public void setAliwap_fee_value(Double aliwapFeeValue) {
        this.aliwap_fee_value = aliwapFeeValue;
    }

    public Double getWechatwap_fee_value() {
        return this.wechatwap_fee_value;
    }

    public void setWechatwap_fee_value(Double wechatwapFeeValue) {
        this.wechatwap_fee_value = wechatwapFeeValue;
    }

    public String toString() {
        return "QrcodeMchtInfoTmp [alipay_fee_value=" + this.alipay_fee_value + ", bank_card_no=" + this.bank_card_no + ", bank_name=" + this.bank_name + ", bank_no=" + this.bank_no + ", card_name=" + this.card_name + ", channelMchtNo=" + this.channelMchtNo + ", channel_id=" + this.channel_id + ", companyName=" + this.companyName + ", company_id=" + this.company_id + ", credit_card_fee_value=" + this.credit_card_fee_value + ", crtTime=" + this.crtTime + ", debit_card_fee_value=" + this.debit_card_fee_value + ", debit_card_max_fee=" + this.debit_card_max_fee + ", dfcard_day_limit=" + this.dfcard_day_limit + ", email=" + this.email + ", identity_no=" + this.identity_no + ", ip_addr=" + this.ip_addr + ", is_t1_liq=" + this.is_t1_liq + ", jump_flag=" + this.jump_flag + ", jump_group=" + this.jump_group + ", lisence_addr=" + this.lisence_addr + ", mchtName=" + this.mchtName + ", mchtNo=" + this.mchtNo + ", netpay_fee_value=" + this.netpay_fee_value + ", phone=" + this.phone + ", qq_fee_value=" + this.qq_fee_value + ", quickpay_fee_value=" + this.quickpay_fee_value + ", recheck_time=" + this.recheck_time + ", refuse_reason=" + this.refuse_reason + ", return_native_qrcode=" + this.return_native_qrcode + ", secretKey=" + this.secretKey + ", single_extra_fee=" + this.single_extra_fee + ", status=" + this.status + ", trade_source_list=" + this.trade_source_list + ", update_opr=" + this.update_opr + ", update_time=" + this.update_time + ", wechat_fee_value=" + this.wechat_fee_value + "]";
    }
}
