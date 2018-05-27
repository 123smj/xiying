/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 */
package com.trade.bean.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.trade.bean.response.Response;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ResponseCode
        extends Response {
    private String code_url;
    private String gymchtId;
    private String appid;
    private String pay_info;
    private String token_id;

    public String getCode_url() {
        return this.code_url;
    }

    public void setCode_url(String codeUrl) {
        this.code_url = codeUrl;
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

    public String getGymchtId() {
        return this.gymchtId;
    }

    public void setGymchtId(String gymchtId) {
        this.gymchtId = gymchtId;
    }

    public String getAppid() {
        return this.appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String toString() {
        return "ResponseCode [appid=" + this.appid + ", code_url=" + this.code_url + ", gymchtId=" + this.gymchtId + ", pay_info=" + this.pay_info + ", token_id=" + this.token_id + "]";
    }
}
