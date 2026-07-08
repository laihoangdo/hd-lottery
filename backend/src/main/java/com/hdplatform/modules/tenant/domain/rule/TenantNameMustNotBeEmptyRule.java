package com.hdplatform.modules.tenant.domain.rule;

import com.hdplatform.shared.domain.rule.BusinessRule;

public class TenantNameMustNotBeEmptyRule
        implements BusinessRule {

    private final String value;

    public TenantNameMustNotBeEmptyRule(String value) {
        this.value = value;
    }

    @Override
    public boolean isBroken() {
        return value == null || value.isBlank();
    }

    @Override
    public String message() {
        return "Tenant name must not be empty.";
    }

}