package com.hdplatform.modules.identity.application.query;

import com.hdplatform.modules.identity.domain.aggregate.TenantMembershipId;
import com.hdplatform.modules.identity.domain.aggregate.UserId;
import com.hdplatform.modules.identity.domain.valueobject.PermissionCode;
import com.hdplatform.modules.identity.domain.valueobject.RoleCode;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public record TenantAccessProfile(
        UserId userId,
        TenantMembershipId membershipId,
        TenantId tenantId,
        Set<RoleCode> roles,
        Set<PermissionCode> permissions
) {
    public TenantAccessProfile {
        Objects.requireNonNull(userId);
        Objects.requireNonNull(membershipId);
        Objects.requireNonNull(tenantId);
        roles = Set.copyOf(roles);
        permissions = Set.copyOf(permissions);
    }

    /** Canonical claims consumed by HD Platform's resource server. */
    public Map<String, Object> asJwtClaims() {
        return Map.of(
                "sub", userId.getValue().toString(),
                "scope", "tenant",
                "tenant_id", tenantId.getValue().toString(),
                "membership_id", membershipId.getValue().toString(),
                "roles", roles.stream().map(RoleCode::value).sorted().toList(),
                "permissions", permissions.stream()
                        .map(PermissionCode::value).sorted().toList());
    }
}
