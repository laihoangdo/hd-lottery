package com.hdplatform.modules.tenant.domain.valueobject;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

import com.hdplatform.shared.domain.exception.DomainException;

/**
 * Value Object representing a tenant hotline.
 */
public final class Hotline implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Pattern PATTERN =
            Pattern.compile("^[0-9+\\- ]{8,20}$");

    private final String value;

    private Hotline(String value) {

        Objects.requireNonNull(value);

        String normalized = value.trim();

        if (!PATTERN.matcher(normalized).matches()) {
            throw new DomainException("Invalid hotline.");
        }

        this.value = normalized;
    }

    public static Hotline from(String value) {
        return new Hotline(value);
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

        if (!(obj instanceof Hotline other)) {
            return false;
        }

        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public static Hotline of(String hotline) {
        return new Hotline(hotline);
    }

}