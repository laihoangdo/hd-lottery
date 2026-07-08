package com.hdplatform.modules.tenant.domain.valueobject;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object representing a unique tenant site key.
 *
 * <p>Examples:</p>
 * <ul>
 *     <li>xosophuongnghi</li>
 *     <li>xosomiennam</li>
 *     <li>daily123</li>
 * </ul>
 */
public final class SiteKey implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Lowercase letters, numbers and hyphen only.
     */
    private static final Pattern PATTERN =
            Pattern.compile("^[a-z0-9-]{3,50}$");

    private final String value;

    private SiteKey(String value) {

        Objects.requireNonNull(value, "SiteKey cannot be null");

        String normalized = value.trim().toLowerCase();

        if (!PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException(
                    "Invalid SiteKey. Only lowercase letters, numbers and hyphen are allowed."
            );
        }

        this.value = normalized;
    }

    public static SiteKey from(String value) {
        return new SiteKey(value);
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

        if (!(obj instanceof SiteKey other)) {
            return false;
        }

        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public static SiteKey of(String siteKey) {
        return new SiteKey(siteKey);
    }

}