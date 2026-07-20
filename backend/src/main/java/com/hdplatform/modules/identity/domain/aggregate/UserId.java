package com.hdplatform.modules.identity.domain.aggregate;


import java.util.Objects;
import java.util.UUID;

import com.hdplatform.shared.domain.identifier.UUIDIdentifier;

/**
 * Strongly typed identifier for User aggregate.
 */
public final class UserId extends UUIDIdentifier {

    private UserId(UUID value) {
        super(value);
    }

    public static UserId of(UUID value) {
        Objects.requireNonNull(value, "User id must not be null.");
        return new UserId(value);
    }

    public static UserId from(String value) {
        Objects.requireNonNull(value, "User id must not be null.");
        return new UserId(UUID.fromString(value));
    }

    public static UserId newId() {
        return new UserId(UUID.randomUUID());
    }

}