package com.hdplatform.shared.domain.identifier;

import com.hdplatform.shared.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public abstract class UUIDIdentifier
        implements Identifier<UUID> {

    private final UUID value;

    protected UUIDIdentifier(UUID value) {

        this.value = Objects.requireNonNull(value);
    }

    @Override
    public UUID getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (!(obj instanceof UUIDIdentifier other))
            return false;

        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}