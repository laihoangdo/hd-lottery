package com.hdplatform.modules.tenant.domain.valueobject;

import java.io.Serial;
import java.io.Serializable;
import java.net.IDN;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object representing a domain name.
 *
 * Examples:
 * xosophuongnghi.com.vn
 * demo.local
 * hd-platform.vn
 */
public final class DomainName implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Pattern DOMAIN_PATTERN =
            Pattern.compile("^(?=.{1,253}$)(?!-)[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)+$");

    private final String value;

    private DomainName(String value) {

        Objects.requireNonNull(value, "Domain name cannot be null");

        String normalized = IDN.toASCII(value.trim())
                .toLowerCase(Locale.ROOT);

        if (!DOMAIN_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException(
                    "Invalid domain name: " + value
            );
        }

        this.value = normalized;
    }

    public static DomainName from(String value) {
        return new DomainName(value);
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

        if (!(obj instanceof DomainName other)) {
            return false;
        }

        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}