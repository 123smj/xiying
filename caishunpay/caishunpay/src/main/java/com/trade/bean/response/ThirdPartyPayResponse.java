/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 */
package com.trade.bean.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.trade.enums.ResponseEnum;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
public class ThirdPartyPayResponse
extends Response {
    private String pay_url;
    private String img_url;
    private String merchantId;
    private String appid;
    private String pay_info;
    private String token_id;

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getPay_url() {
        return this.pay_url;
    }

    public void setPay_url(String codeUrl) {
        this.pay_url = codeUrl;
    }

    public String getPay_info() {
        return this.pay_info;
    }

    public void setPay_info(String payInfo) {
        this.pay_info = payInfo;
    }

    public String getToken_id() {
        return this.token_id;
    }

    public void setToken_id(String tokenId) {
        this.token_id = tokenId;
    }

    public String getMerchantId() {
        return this.merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getAppid() {
        return this.appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String toString() {
        return "ThirdPartyPayResponse [appid=" + this.appid + ", pay_url=" + this.pay_url + ", merchantId=" + this.merchantId + ", pay_info=" + this.pay_info + ", token_id=" + this.token_id + "]";
    }

    public static ThirdPartyPayResponse success(String codeUrl, String merchantId){
        ThirdPartyPayResponse response = new ThirdPartyPayResponse();
        response.pay_url = codeUrl;
        response.merchantId = merchantId;
        response.resultCode = ResponseEnum.SUCCESS.getCode();
        response.message = ResponseEnum.SUCCESS.getMemo();
        return response;
    }

    public static ThirdPartyPayResponse fail(String message) {
        ThirdPartyPayResponse response = new ThirdPartyPayResponse();
        response.resultCode = ResponseEnum.FAIL_SYSTEM.getCode();
        response.message = message;
        return response;
    }

    public static ThirdPartyPayResponse fail(ResponseEnum e) {
        ThirdPartyPayResponse response = new ThirdPartyPayResponse();
        response.resultCode = e.getCode();
        response.message = e.getMemo();
        return response;
    }



}
