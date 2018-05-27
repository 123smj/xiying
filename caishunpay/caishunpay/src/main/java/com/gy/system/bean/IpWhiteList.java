/*
 * Decompiled with CFR 0_124.
 */
package com.gy.system.bean;

public class IpWhiteList {
    private String ip;
    private String addDate;
    private String flag;

    public IpWhiteList(String ip, String flag, String addDate) {
        this.ip = ip;
        this.addDate = addDate;
        this.flag = flag;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAddDate() {
        return this.addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    public String getFlag() {
        return this.flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
