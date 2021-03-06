/*
 * Decompiled with CFR 0_124.
 */
package com.trade.bean.own;

import com.gy.util.StringUtil;
import com.trade.enums.TradeSource;

/***
 * 三方支付请求
 */
public class PayRequest {
    private String remoteAddr;
    private String merchantId;
    private String chanelId;
    private String notifyUrl;
    private String tradeSn;
    private Integer orderAmount;
    private String goodsName;
    private String expirySecond;
    private String is_raw;
    private String sub_appid;
    private String sub_openid;
    private String callback_url;
    private String t0Flag;
    private String idcard;
    private String realname;
    private String bankcardnum;
    private String bankname;
    private String subbranch;
    private String pcnaps;
    private Integer free;
    private TradeSource tradeSource;
    private String rootUrl;
    private String clientIp;
    private String nonce;

    public String getNonce() {
        return nonce;
    }
    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String checkPram() {
        if(StringUtil.isEmpty(this.nonce)){
            return "随机数未设置";
        }
        if (StringUtil.isEmpty(this.merchantId)) {
            return "\u5546\u6237\u53f7\u6709\u8bef";
        }
        if (StringUtil.isEmpty(this.tradeSn)) {
            return "\u8ba2\u5355\u53f7\u6709\u8bef";
        }
        if (this.orderAmount == null || this.orderAmount <= 0) {
            return "\u4ea4\u6613\u91d1\u989d\u6709\u8bef";
        }
        if (StringUtil.isEmpty(this.goodsName)) {
            return "\u5546\u54c1\u540d\u79f0\u6709\u8bef";
        }
        if (StringUtil.isEmpty(this.notifyUrl)) {
            return "\u901a\u77e5\u5730\u5740\u6709\u8bef";
        }
        if (this.tradeSource == null) {
            return "交易类型有误";
        }
        return "00";
    }


    public String checkQueryParam() {
        if(StringUtil.isEmpty(this.nonce)){
            return "随机数未设置";
        }
        if (StringUtil.isEmpty(this.merchantId)) {
            return "\u5546\u6237\u53f7\u6709\u8bef";
        }
        if (StringUtil.isEmpty(this.tradeSn)) {
            return "\u8ba2\u5355\u53f7\u6709\u8bef";
        }
        return null;
    }

    public String getMerchantId() {
        return this.merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getChanelId() {
        return this.chanelId;
    }

    public void setChanelId(String chanelId) {
        this.chanelId = chanelId;
    }

    public String getNotifyUrl() {
        return this.notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getTradeSn() {
        return this.tradeSn;
    }

    public void setTradeSn(String tradeSn) {
        this.tradeSn = tradeSn;
    }

    public String getGoodsName() {
        return this.goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getOrderAmount() {
        return this.orderAmount;
    }

    public void setOrderAmount(Integer orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getExpirySecond() {
        return this.expirySecond;
    }

    public void setExpirySecond(String expirySecond) {
        this.expirySecond = expirySecond;
    }

    public String getRemoteAddr() {
        return this.remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getIs_raw() {
        return this.is_raw;
    }

    public void setIs_raw(String isRaw) {
        this.is_raw = isRaw;
    }

    public String getCallback_url() {
        return this.callback_url;
    }

    public void setCallback_url(String callbackUrl) {
        this.callback_url = callbackUrl;
    }

    public String getT0Flag() {
        return this.t0Flag;
    }

    public void setT0Flag(String t0Flag) {
        this.t0Flag = t0Flag;
    }

    public String getIdcard() {
        return this.idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getRealname() {
        return this.realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getBankcardnum() {
        return this.bankcardnum;
    }

    public void setBankcardnum(String bankcardnum) {
        this.bankcardnum = bankcardnum;
    }

    public Integer getFree() {
        return this.free;
    }

    public void setFree(Integer free) {
        this.free = free;
    }

    public String getSub_openid() {
        return this.sub_openid;
    }

    public void setSub_openid(String subOpenid) {
        this.sub_openid = subOpenid;
    }

    public String getBankname() {
        return this.bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getSubbranch() {
        return this.subbranch;
    }

    public void setSubbranch(String subbranch) {
        this.subbranch = subbranch;
    }

    public String getPcnaps() {
        return this.pcnaps;
    }

    public void setPcnaps(String pcnaps) {
        this.pcnaps = pcnaps;
    }

    public TradeSource getTradeSource() {
        return this.tradeSource;
    }

    public void setTradeSource(TradeSource tradeSource) {
        this.tradeSource = tradeSource;
    }

    public String getRootUrl() {
        return this.rootUrl;
    }

    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    public String getSub_appid() {
        return this.sub_appid;
    }

    public void setSub_appid(String subAppid) {
        this.sub_appid = subAppid;
    }

    public String toString() {
        return "PayRequest [bankcardnum=" + this.bankcardnum + ", bankname=" + this.bankname + ", callback_url=" + this.callback_url + ", chanelId=" + this.chanelId + ", expirySecond=" + this.expirySecond + ", free=" + this.free + ", goodsName=" + this.goodsName + ", merchantId=" + this.merchantId + ", idcard=" + this.idcard + ", is_raw=" + this.is_raw + ", notifyUrl=" + this.notifyUrl + ", orderAmount=" + this.orderAmount + ", pcnaps=" + this.pcnaps + ", realname=" + this.realname + ", remoteAddr=" + this.remoteAddr + ", rootUrl=" + this.rootUrl + ", sub_appid=" + this.sub_appid + ", sub_openid=" + this.sub_openid + ", subbranch=" + this.subbranch + ", t0Flag=" + this.t0Flag + ", tradeSn=" + this.tradeSn + ", tradeSource=" + this.tradeSource + "]";
    }


}
