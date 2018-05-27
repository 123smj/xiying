/*
 * Decompiled with CFR 0_124.
 */
package com.tuser.bean;

import java.io.Serializable;

public class TradeUserMcht
        implements Serializable {
    private static final long serialVersionUID = 1L;
    public static String REF = "TblUserMcht";
    public static String PROP_MCHT_NO = "mchtNo";
    public static String PROP_PASSWORD = "password";
    public static String PROP_CREATE_TYPE = "createType";
    public static String PROP_CREATE_TIME = "createTime";
    public static String PROP_UPDATE_TIME = "updateTime";
    private int hashCode = Integer.MIN_VALUE;
    private String mchtNo;
    private String password;
    private String createType;
    private String createTime;
    private String updateTime;
    private String bindUUID;

    protected void initialize() {
    }

    public String getMchtNo() {
        return this.mchtNo;
    }

    public void setMchtNo(String mchtNo) {
        this.mchtNo = mchtNo;
        this.hashCode = Integer.MIN_VALUE;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreateType() {
        return this.createType;
    }

    public void setCreateType(String createType) {
        this.createType = createType;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getBindUUID() {
        return this.bindUUID;
    }

    public void setBindUUID(String bindUUID) {
        this.bindUUID = bindUUID;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TradeUserMcht)) {
            return false;
        }
        TradeUserMcht tuser = (TradeUserMcht) obj;
        if (this.getMchtNo() == null || tuser.getMchtNo() == null) {
            return false;
        }
        return this.getMchtNo().equals(tuser.getMchtNo());
    }

    public int hashCode() {
        if (Integer.MIN_VALUE == this.hashCode) {
            if (this.getMchtNo() == null) {
                return super.hashCode();
            }
            String hashStr = String.valueOf(this.getClass().getName()) + ":" + this.getMchtNo().hashCode();
            this.hashCode = hashStr.hashCode();
        }
        return this.hashCode;
    }

    public String toString() {
        return super.toString();
    }
}
