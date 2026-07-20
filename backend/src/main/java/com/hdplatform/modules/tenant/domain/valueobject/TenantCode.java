package com.hdplatform.modules.tenant.domain.valueobject;

import com.hdplatform.shared.domain.valueobject.BaseValueObject;

public final class TenantCode extends BaseValueObject {

    private final String value;

    private TenantCode(String value){
        this.value = value.trim().toLowerCase();
    }

    public static TenantCode of(String value) {
        return new TenantCode(value);
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