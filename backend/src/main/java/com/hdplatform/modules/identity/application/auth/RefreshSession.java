package com.hdplatform.modules.identity.application.auth;

import com.hdplatform.modules.identity.domain.aggregate.UserId;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;

import java.time.Instant;
import java.util.UUID;

public record RefreshSession(
        UUID id,
        UUID familyId,
        UserId userId,
        AuthenticationScope scope,
        TenantId tenantId,
        String tokenHash,
        Instant expiresAt,
        Instant revokedAt
) {
    public boolean expiredAt(Instant now) { return !expiresAt.isAfter(now); }
    public boolean revoked() { return revokedAt != null; }
}
