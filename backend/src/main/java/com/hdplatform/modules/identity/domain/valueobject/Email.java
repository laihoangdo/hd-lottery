package com.hdplatform.modules.identity.domain.valueobject;


import java.util.Objects;
import java.util.regex.Pattern;

import com.hdplatform.shared.domain.exception.DomainException;
import com.hdplatform.shared.domain.valueobject.BaseValueObject;

/**
 * Email value object.
 */
public final class Email extends BaseValueObject {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private final String value;

    private Email(String value) {
        this.value = value;
    }

    public static Email of(String value) {

        Objects.requireNonNull(value, "Email must not be null.");

        String normalized = value.trim().toLowerCase();

        if (normalized.isBlank()) {
            throw new DomainException("Email must not be blank.");
        }

        if (!EMAIL_PATTERN.matcher(normalized).matches()) {
            throw new DomainException("Invalid email format.");
        }

        return new Email(normalized);
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