package com.hdplatform.modules.tenant.domain.valueobject;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Value Object representing the display name of a tenant.
 */
public final class DisplayName implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 100;

    private final String value;

    private DisplayName(String value) {

        Objects.requireNonNull(value, "Display name cannot be null");

        String normalized = value.trim();

        if (normalized.length() < MIN_LENGTH || normalized.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "Display name must be between "
                            + MIN_LENGTH
                            + " and "
                            + MAX_LENGTH
                            + " characters."
            );
        }

        this.value = normalized;
    }

    public static DisplayName from(String value) {
        return new DisplayName(value);
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof DisplayName other)) {
            return false;
        }

        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public static DisplayName of(String displayName) {
        return new DisplayName(displayName);
    }

}