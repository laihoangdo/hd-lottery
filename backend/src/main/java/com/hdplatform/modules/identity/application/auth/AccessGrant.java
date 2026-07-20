package com.hdplatform.modules.identity.application.auth;

import com.hdplatform.modules.identity.domain.aggregate.UserId;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;

import java.util.Set;

public record AccessGrant(
        UserId userId,
        AuthenticationScope scope,
        TenantId tenantId,
        Set<String> roles,
        Set<String> permissions
) {
    public AccessGrant {
        roles = Set.copyOf(roles);
        permissions = Set.copyOf(permissions);
        if ((scope == AuthenticationScope.TENANT) != (tenantId != null)) {
            throw new IllegalArgumentException("Tenant grant must have tenantId; platform grant must not");
        }
    }
}
