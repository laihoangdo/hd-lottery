package com.hdplatform.modules.tenant.domain.aggregate;

import com.hdplatform.shared.domain.identifier.UUIDIdentifier;

import java.util.UUID;

public final class TenantId extends UUIDIdentifier {

    private TenantId(UUID value) {
        super(value);
    }

    public static TenantId of(UUID value) {
        return new TenantId(value);
    }

    public static TenantId newId() {
        return new TenantId(UUID.randomUUID());
    }

}