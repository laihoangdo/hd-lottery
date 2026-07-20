package com.hdplatform.modules.identity.domain.valueobject;

import com.hdplatform.shared.domain.exception.DomainException;
import com.hdplatform.shared.domain.valueobject.BaseValueObject;

import java.util.Objects;

/**
 * Full name value object.
 */
public final class FullName extends BaseValueObject {

    private final String value;

    private FullName(String value) {
        this.value = value;
    }

    public static FullName of(String value) {

        Objects.requireNonNull(value);

        String normalized = value.trim();

        if (normalized.isBlank()) {
            throw new DomainException("Full name must not be blank.");
        }

        if (normalized.length() < 3) {
            throw new DomainException("Full name must contain at least 3 characters.");
        }

        if (normalized.length() > 100) {
            throw new DomainException("Full name must not exceed 100 characters.");
        }

        return new FullName(normalized);

    }

    public String value() {
        return value;
    }

    @Override
    protected Object[] equalityValues() {
        return new Object[]{value};
    }

}