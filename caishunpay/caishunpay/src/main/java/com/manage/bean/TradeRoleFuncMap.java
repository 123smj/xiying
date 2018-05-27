/*
 * Decompiled with CFR 0_124.
 */
package com.manage.bean;

import com.manage.bean.TradeRoleFuncMapPK;
import java.io.Serializable;

public class TradeRoleFuncMap
implements Serializable {
    private TradeRoleFuncMapPK id;
    private String recUpdOpr;
    private String recCrtTs;
    private String recUpdTs;

    public TradeRoleFuncMapPK getId() {
        return this.id;
    }

    public void setId(TradeRoleFuncMapPK id) {
        this.id = id;
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
