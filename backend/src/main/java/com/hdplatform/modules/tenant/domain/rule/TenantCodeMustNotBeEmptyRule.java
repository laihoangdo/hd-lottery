package com.hdplatform.modules.tenant.domain.rule;

import com.hdplatform.shared.domain.rule.BusinessRule;

public class TenantCodeMustNotBeEmptyRule
        implements BusinessRule {

    private final String value;

    public TenantCodeMustNotBeEmptyRule(String value) {
        this.value = value;
    }

    @Override
    public boolean isBroken() {
        return value == null || value.isBlank();
    }

    @Override
    public String message() {
        return "Tenant code must not be empty.";
    }

}