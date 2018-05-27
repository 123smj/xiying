package com.manage.bean;

import java.util.Map;

public class RedirectInfo {
    private Map<String, String> paramters;
    private String methodName;
    private String redirectUrl;

    public Map<String, String> getParamters() {
        return paramters;
    }

    public void setParamters(Map<String, String> paramters) {
        this.paramters = paramters;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
