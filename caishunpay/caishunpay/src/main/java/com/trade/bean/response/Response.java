/*
 * Decompiled with CFR 0_124.
 */
package com.trade.bean.response;

import com.trade.enums.ResponseEnum;

public class Response {
    protected String resultCode;
    protected String message;
    protected String sign;
    protected String nonce;

    public String getResultCode() {
        return this.resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSign() {
        return this.sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getNonce() {
        return this.nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public static Response with(ResponseEnum responseEnum,String message){
         Response resp = with(responseEnum);
         resp.setMessage(message);
         return resp;
    }

    public static Response with(ResponseEnum responseEnum){
        Response resp = new Response();
        resp.setResultCode(responseEnum.getCode());
        resp.setMessage(responseEnum.getMemo());
        return resp;
    }
}
