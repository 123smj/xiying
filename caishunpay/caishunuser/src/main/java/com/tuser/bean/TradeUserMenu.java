/*
 * Decompiled with CFR 0_124.
 */
package com.tuser.bean;

import java.io.Serializable;

public class TradeUserMenu
        implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer funcId;
    private Integer funcParentId;
    private String funcType;
    private String funcName;
    private String pageName;
    private String pageUrl;
    private String recUpdOpr;
    private String recCrtTs;
    private String recUpdTs;

    public Integer getFuncId() {
        return this.funcId;
    }

    public void setFuncId(Integer funcId) {
        this.funcId = funcId;
    }

    public Integer getFuncParentId() {
        return this.funcParentId;
    }

    public void setFuncParentId(Integer funcParentId) {
        this.funcParentId = funcParentId;
    }

    public String getFuncType() {
        return this.funcType;
    }

    public void setFuncType(String funcType) {
        this.funcType = funcType;
    }

    public String getFuncName() {
        return this.funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public String getPageName() {
        return this.pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getPageUrl() {
        return this.pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getRecUpdOpr() {
        return this.recUpdOpr;
    }

    public void setRecUpdOpr(String recUpdOpr) {
        this.recUpdOpr = recUpdOpr;
    }

    public String getRecCrtTs() {
        return this.recCrtTs;
    }

    public void setRecCrtTs(String recCrtTs) {
        this.recCrtTs = recCrtTs;
    }

    public String getRecUpdTs() {
        return this.recUpdTs;
    }

    public void setRecUpdTs(String recUpdTs) {
        this.recUpdTs = recUpdTs;
    }
}
