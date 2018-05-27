/*
 * Decompiled with CFR 0_124.
 */
package com.common.model;

import com.trade.enums.ResponseEnum;

public class Response<T> {
    private String code = ResponseEnum.SUCCESS.getCode();
    private String message = "OK";
    private T data;

    public Response() {
    }

    public Response(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String toString() {
        return "SOAResponse = {errorCode:" + this.code + ", message:" + this.message + ", data:}" + (this.data == null ? "null" : "[Object]");
    }
}
