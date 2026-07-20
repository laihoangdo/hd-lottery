package com.hdplatform.modules.identity.domain.valueobject;

import com.hdplatform.shared.domain.exception.DomainException;
import com.hdplatform.shared.domain.valueobject.BaseValueObject;

import java.util.Objects;

/**
 * Password value object.
 *
 * This object stores ONLY hashed password.
 * Raw password must never enter the Domain Model.
 */
public final class Password extends BaseValueObject {

    private final String hashedValue;

    private Password(String hashedValue) {
        this.hashedValue = hashedValue;
    }

    /**
     * Creates password from already encoded value.
     */
    public static Password ofHashed(String hashedPassword) {

        Objects.requireNonNull(hashedPassword,
                "Password must not be null.");

        String normalized = hashedPassword.trim();

        if (normalized.isBlank()) {
            throw new DomainException(
                    "Password must not be blank.");
        }

        /*
         * BCrypt hash length = 60
         */
        if (normalized.length() < 60) {
            throw new DomainException(
                    "Password must be encoded.");
        }

        return new Password(normalized);

    }

    public String value() {
        return hashedValue;
    }

    @Override
    protected Object[] equalityValues() {
        return new Object[]{hashedValue};
    }

    @Override
    public String toString() {
        return "********";
    }

}