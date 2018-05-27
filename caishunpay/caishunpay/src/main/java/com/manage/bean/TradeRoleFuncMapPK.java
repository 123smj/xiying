/*
 * Decompiled with CFR 0_124.
 */
package com.manage.bean;

import java.io.Serializable;

public class TradeRoleFuncMapPK
implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer keyId;
    private Integer valueId;

    public Integer getKeyId() {
        return this.keyId;
    }

    public void setKeyId(Integer keyId) {
        this.keyId = keyId;
    }

    public Integer getValueId() {
        return this.valueId;
    }

    public void setValueId(Integer valueId) {
        this.valueId = valueId;
    }

    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = 31 * result + (this.keyId == null ? 0 : this.keyId.hashCode());
        result = 31 * result + (this.valueId == null ? 0 : this.valueId.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        TradeRoleFuncMapPK other = (TradeRoleFuncMapPK)obj;
        if (this.keyId == null ? other.keyId != null : !this.keyId.equals(other.keyId)) {
            return false;
        }
        if (this.valueId == null ? other.valueId != null : !this.valueId.equals(other.valueId)) {
            return false;
        }
        return true;
    }
}
