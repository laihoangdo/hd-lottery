package com.hdplatform.modules.identity.application.port;

import com.hdplatform.modules.identity.application.auth.*;
import com.hdplatform.modules.identity.domain.aggregate.UserId;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface AuthenticationRepository {
    Optional<UserCredentials> findCredentialsByEmail(String email);
    Optional<AccessGrant> findPlatformGrant(UserId userId);
    Optional<AccessGrant> findTenantGrant(UserId userId, TenantId tenantId);
    Optional<RefreshSession> findRefreshSessionForUpdate(String tokenHash);
    void saveRefreshSession(RefreshSession session, Instant createdAt);
    void rotateRefreshSession(UUID currentId, RefreshSession replacement, Instant now);
    void revokeFamily(UUID familyId, Instant now);
}
