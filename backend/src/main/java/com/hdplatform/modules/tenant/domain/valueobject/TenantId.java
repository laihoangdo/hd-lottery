package com.hdplatform.modules.tenant.domain.valueobject;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Value Object representing the unique identifier of a Tenant.
 *
 * <p>
 * This class is immutable.
 * </p>
 */
public final class TenantId implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID value;

    private TenantId(UUID value) {
        this.value = Objects.requireNonNull(value, "TenantId cannot be null");
    }

    /**
     * Creates a new TenantId.
     *
     * @return new TenantId
     */
    public static TenantId newId() {
        return new TenantId(UUID.randomUUID());
    }

    /**
     * Creates TenantId from UUID.
     *
     * @param value uuid value
     * @return tenant id
     */
    public static TenantId from(UUID value) {
        return new TenantId(value);
    }

    /**
     * Creates TenantId from String.
     *
     * @param value uuid string
     * @return tenant id
     */
    public static TenantId from(String value) {
        return new TenantId(UUID.fromString(value));
    }

    /**
     * Returns UUID value.
     *
     * @return uuid
     */
    public UUID value() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof TenantId other)) {
            return false;
        }

        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
