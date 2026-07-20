package com.hdplatform.modules.identity.application.port;

import com.hdplatform.modules.identity.application.query.TenantAccessProfile;
import com.hdplatform.modules.identity.domain.aggregate.UserId;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;

import java.util.Optional;

public interface TenantAccessProfileRepository {
    Optional<TenantAccessProfile> findActive(UserId userId, TenantId tenantId);
}
