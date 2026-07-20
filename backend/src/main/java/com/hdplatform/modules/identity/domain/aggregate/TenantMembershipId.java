package com.hdplatform.modules.identity.domain.aggregate;

import com.hdplatform.shared.domain.identifier.UUIDIdentifier;

import java.util.UUID;

public final class TenantMembershipId extends UUIDIdentifier {
    private TenantMembershipId(UUID value) { super(value); }
    public static TenantMembershipId newId() { return new TenantMembershipId(UUID.randomUUID()); }
    public static TenantMembershipId of(UUID value) { return new TenantMembershipId(value); }
}
