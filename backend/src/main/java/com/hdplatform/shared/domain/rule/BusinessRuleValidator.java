package com.hdplatform.shared.domain.rule;

import com.hdplatform.shared.domain.exception.DomainException;

public final class BusinessRuleValidator {

    private BusinessRuleValidator() {
    }

    public static void check(BusinessRule rule) {

        if (rule.isBroken()) {
            throw new DomainException(rule.message());
        }
    }

}