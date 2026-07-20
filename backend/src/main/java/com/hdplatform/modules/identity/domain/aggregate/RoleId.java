package com.hdplatform.modules.identity.domain.aggregate;

import com.hdplatform.shared.domain.identifier.UUIDIdentifier;

import java.util.UUID;

public final class RoleId extends UUIDIdentifier {
    private RoleId(UUID value) { super(value); }
    public static RoleId newId() { return new RoleId(UUID.randomUUID()); }
    public static RoleId of(UUID value) { return new RoleId(value); }
}
