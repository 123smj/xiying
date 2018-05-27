/*
 * Decompiled with CFR 0_124.
 */
package com.common.dwr;

import com.common.dwr.SelectDynamicMode;
import com.common.dwr.SelectSqlMode;
import com.common.dwr.SelectStaticMode;

public class SelectOptionExtractMethod {
    private String extractMode;
    private SelectStaticMode selectStaticMode;
    private SelectSqlMode selectSqlMode;
    private SelectDynamicMode selectDynamicMode;

    public String getExtractMode() {
        return this.extractMode;
    }

    public void setExtractMode(String extractMode) {
        this.extractMode = extractMode;
    }

    public SelectStaticMode getSelectStaticMode() {
        return this.selectStaticMode;
    }

    public void setSelectStaticMode(SelectStaticMode selectStaticMode) {
        this.selectStaticMode = selectStaticMode;
    }

    public SelectSqlMode getSelectSqlMode() {
        return this.selectSqlMode;
    }

    public void setSelectSqlMode(SelectSqlMode selectSqlMode) {
        this.selectSqlMode = selectSqlMode;
    }

    public SelectDynamicMode getSelectDynamicMode() {
        return this.selectDynamicMode;
    }

    public void setSelectDynamicMode(SelectDynamicMode selectDynamicMode) {
        this.selectDynamicMode = selectDynamicMode;
    }
}
