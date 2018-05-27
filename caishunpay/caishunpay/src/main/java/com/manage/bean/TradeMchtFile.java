/*
 * Decompiled with CFR 0_124.
 */
package com.manage.bean;

import java.io.Serializable;

public class TradeMchtFile
implements Serializable {
    private static final long serialVersionUID = 1L;
    private int fileId;//文件ID
    private String mchtNo;
    private String mchtFileType;
    private String mchtFileName;
    private String mchtFilePath;
    private String crtOprId;
    private String recCrtTs;
    private String updOprId;
    private String recUpdTs;

    public int getFileId() {
        return this.fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getMchtNo() {
        return this.mchtNo;
    }

    public void setMchtNo(String mchtNo) {
        this.mchtNo = mchtNo;
    }

    public String getMchtFileType() {
        return this.mchtFileType;
    }

    public void setMchtFileType(String mchtFileType) {
        this.mchtFileType = mchtFileType;
    }

    public String getMchtFileName() {
        return this.mchtFileName;
    }

    public void setMchtFileName(String mchtFileName) {
        this.mchtFileName = mchtFileName;
    }

    public String getMchtFilePath() {
        return this.mchtFilePath;
    }

    public void setMchtFilePath(String mchtFilePath) {
        this.mchtFilePath = mchtFilePath;
    }

    public String getCrtOprId() {
        return this.crtOprId;
    }

    public void setCrtOprId(String crtOprId) {
        this.crtOprId = crtOprId;
    }

    public String getRecCrtTs() {
        return this.recCrtTs;
    }

    public void setRecCrtTs(String recCrtTs) {
        this.recCrtTs = recCrtTs;
    }

    public String getUpdOprId() {
        return this.updOprId;
    }

    public void setUpdOprId(String updOprId) {
        this.updOprId = updOprId;
    }

    public String getRecUpdTs() {
        return this.recUpdTs;
    }

    public void setRecUpdTs(String recUpdTs) {
        this.recUpdTs = recUpdTs;
    }
}
