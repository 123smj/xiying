/*
 * Decompiled with CFR 0_124.
 */
package com.trade.util.ninepi;

import java.io.Serializable;

public class HttpSendResult
implements Serializable {
    private static final long serialVersionUID = 3612208038316088287L;
    private int status = -1;
    private String responseBody;

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getResponseBody() {
        return this.responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }
}
