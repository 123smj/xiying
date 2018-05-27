/*
 * Decompiled with CFR 0_124.
 */
package com.trade.bean;

import java.io.Serializable;

public class TimerTaskBean
implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer taskId;
    private String taskName;
    private String taskDesc;
    private String startTime;
    private String endTime;
    private String status;
    private String period;
    private String crtTime;
    private String classFullname;
    private String methodName;

    public Integer getTaskId() {
        return this.taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDesc() {
        return this.taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCrtTime() {
        return this.crtTime;
    }

    public void setCrtTime(String crtTime) {
        this.crtTime = crtTime;
    }

    public String getClassFullname() {
        return this.classFullname;
    }

    public void setClassFullname(String classFullname) {
        this.classFullname = classFullname;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getPeriod() {
        return this.period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
