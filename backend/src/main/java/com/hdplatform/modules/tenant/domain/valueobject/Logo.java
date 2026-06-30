package com.hdplatform.modules.tenant.domain.valueobject;

import java.io.Serial;
import java.io.Serializable;
import java.net.URI;
import java.util.Objects;

/**
 * Value Object representing a tenant logo URL.
 */
public final class Logo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final URI value;

    private Logo(URI value) {
        this.value = Objects.requireNonNull(value);
    }

    public static Logo from(String value) {

        Objects.requireNonNull(value, "Logo URL cannot be null");

        URI uri = URI.create(value.trim());

        return new Logo(uri);
    }

    public URI value() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Logo other)) {
            return false;
        }

        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}