/*
 * Decompiled with CFR 0_124.
 */
package com.trade.bean;

public class WezBankClient {
    private String access_token;
    private String ticket;
    private String app_id;
    private String nonce;
    private String version;
    private String jsonBody;
    private String sign;

    public String getAccess_token() {
        return this.access_token;
    }

    public void setAccess_token(String accessToken) {
        this.access_token = accessToken;
    }

    public String getTicket() {
        return this.ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getApp_id() {
        return this.app_id;
    }

    public void setApp_id(String appId) {
        this.app_id = appId;
    }

    public String getNonce() {
        return this.nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getJsonBody() {
        return this.jsonBody;
    }

    public void setJsonBody(String jsonBody) {
        this.jsonBody = jsonBody;
    }

    public String getSign() {
        return this.sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String toString() {
        return "WezBankClient [access_token=" + this.access_token + ", app_id=" + this.app_id + ", jsonBody=" + this.jsonBody + ", nonce=" + this.nonce + ", sign=" + this.sign + ", ticket=" + this.ticket + ", version=" + this.version + "]";
    }
}
