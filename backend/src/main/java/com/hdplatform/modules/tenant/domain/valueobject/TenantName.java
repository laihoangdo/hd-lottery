package com.hdplatform.modules.tenant.domain.valueobject;

import com.hdplatform.shared.domain.valueobject.BaseValueObject;

public final class TenantName extends BaseValueObject {

    private final String value;

    private TenantName(String value) {
        this.value = value.trim();
    }

    public static TenantName of(String value) {
        return new TenantName(value);
    }

    public String value() {
        return value;
    }

    @Override
    protected Object[] equalityValues() {
        return new Object[]{value};
    }

    @Override
    public String toString() {
        return value;
    }

}