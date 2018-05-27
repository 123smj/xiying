/*
 * Decompiled with CFR 0_124.
 */
package com.tuser.bean;

import java.io.Serializable;

public class TblUserAdmin
        implements Serializable {
    private static final long serialVersionUID = 4075705956041082473L;
    private String adminId;
    private String password;
    private String createTime;
    private String updateTime;
    private int hashCode = Integer.MIN_VALUE;

    public TblUserAdmin() {
    }

    public TblUserAdmin(String password) {
        this.password = password;
    }

    public TblUserAdmin(String password, String createTime, String updateTime) {
        this.password = password;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public String getAdminId() {
        return this.adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
        this.hashCode = Integer.MIN_VALUE;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TblUserAdmin)) {
            return false;
        }
        TblUserAdmin tblFuncInf = (TblUserAdmin) obj;
        if (this.getAdminId() == null || tblFuncInf.getAdminId() == null) {
            return false;
        }
        return this.getAdminId().equals(tblFuncInf.getAdminId());
    }

    public int hashCode() {
        if (Integer.MIN_VALUE == this.hashCode) {
            if (this.getAdminId() == null) {
                return super.hashCode();
            }
            String hashStr = String.valueOf(this.getClass().getName()) + ":" + this.getAdminId().hashCode();
            this.hashCode = hashStr.hashCode();
        }
        return this.hashCode;
    }

    public String toString() {
        return super.toString();
    }
}
