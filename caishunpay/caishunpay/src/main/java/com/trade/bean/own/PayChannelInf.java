/*
 * Decompiled with CFR 0_124.
 */
package com.trade.bean.own;

import java.io.Serializable;
//支付通道
public class PayChannelInf implements Serializable {
    private static final long serialVersionUID = 1L;
    private String channel_id;
    private String secret_key;
    private String channel_mcht_no;
    private String t0Flag;
    private String channel_name;
    private String agtId;
    private String primary_key;
    private Double wechat_fee_value;
    private Double alipay_fee_value;
    private Double quickpay_fee_value;
    private Double netpay_fee_value;
    private String update_time;
    private String crt_time;
    private String update_opr;
    private String status;
    private String pass_word;
    private String phone;
    private String identity_no;
    private String bank_card_no;
    private String bank_name;
    private String card_name;
    private String address;
    private String check_time;
    private String check_message;
    private String jump_url;

    public String getJump_url() {
        return jump_url;
    }

    public void setJump_url(String jump_url) {
        this.jump_url = jump_url;
    }

    public PayChannelInf(String channelId, String channelMchtNo) {
        this.channel_id = channelId;
        this.channel_mcht_no = channelMchtNo;
    }

    public PayChannelInf() {
    }

    public String getChannel_id() {
        return this.channel_id;
    }

    public void setChannel_id(String channelId) {
        this.channel_id = channelId;
    }

    public String getSecret_key() {
        return this.secret_key;
    }

    public void setSecret_key(String secretKey) {
        this.secret_key = secretKey;
    }

    public String getChannel_mcht_no() {
        return this.channel_mcht_no;
    }

    public void setChannel_mcht_no(String channelMchtNo) {
        this.channel_mcht_no = channelMchtNo;
    }

    public String getChannel_name() {
        return this.channel_name;
    }

    public void setChannel_name(String channelName) {
        this.channel_name = channelName;
    }

    public String getT0Flag() {
        return this.t0Flag;
    }

    public void setT0Flag(String t0Flag) {
        this.t0Flag = t0Flag;
    }

    public String getAgtId() {
        return this.agtId;
    }

    public void setAgtId(String agtId) {
        this.agtId = agtId;
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

    public String getCrt_time() {
        return this.crt_time;
    }

    public String getPrimary_key() {
        return this.primary_key;
    }

    public void setPrimary_key(String primaryKey) {
        this.primary_key = primaryKey;
    }

    public void setCrt_time(String crtTime) {
        this.crt_time = crtTime;
    }

    public String getPass_word() {
        return this.pass_word;
    }

    public void setPass_word(String passWord) {
        this.pass_word = passWord;
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

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCheck_time() {
        return this.check_time;
    }

    public void setCheck_time(String checkTime) {
        this.check_time = checkTime;
    }

    public String getCheck_message() {
        return this.check_message;
    }

    public void setCheck_message(String checkMessage) {
        this.check_message = checkMessage;
    }
}
