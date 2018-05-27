/*
 * Decompiled with CFR 0_124.
 */
package com.common.dwr;

import com.common.dwr.SelectOptionExtractMethod;

public class SelectElement {
    private String txnId;
    private SelectOptionExtractMethod extractMethod;

    public String getTxnId() {
        return this.txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public SelectOptionExtractMethod getExtractMethod() {
        return this.extractMethod;
    }

    public void setExtractMethod(SelectOptionExtractMethod extractMethod) {
        this.extractMethod = extractMethod;
    }
}
