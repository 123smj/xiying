/*
 * Decompiled with CFR 0_124.
 */
package com.trade.bean;

public class WxNativeBack
extends ThirdPartyPayDetail {
    private String status;
    private String message;
    private String result_code;
    private String err_code;
    private String err_msg;
    private String code_url;
    private String code_img_url;

    @Override
    public String getStatus() {
        return this.status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getResult_code() {
        return this.result_code;
    }

    @Override
    public void setResult_code(String resultCode) {
        this.result_code = resultCode;
    }

    @Override
    public String getErr_code() {
        return this.err_code;
    }

    @Override
    public void setErr_code(String errCode) {
        this.err_code = errCode;
    }

    @Override
    public String getErr_msg() {
        return this.err_msg;
    }

    @Override
    public void setErr_msg(String errMsg) {
        this.err_msg = errMsg;
    }

    @Override
    public String getCode_url() {
        return this.code_url;
    }

    @Override
    public void setCode_url(String codeUrl) {
        this.code_url = codeUrl;
    }

    @Override
    public String getCode_img_url() {
        return this.code_img_url;
    }

    @Override
    public void setCode_img_url(String codeImgUrl) {
        this.code_img_url = codeImgUrl;
    }
}
