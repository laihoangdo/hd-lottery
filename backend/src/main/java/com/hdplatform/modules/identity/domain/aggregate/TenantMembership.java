package com.hdplatform.modules.identity.domain.aggregate;

import com.hdplatform.modules.identity.domain.valueobject.MembershipStatus;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import com.hdplatform.shared.domain.AuditableEntity;

import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public final class TenantMembership extends AuditableEntity<TenantMembershipId> {
    private final UserId userId;
    private final TenantId tenantId;
    private MembershipStatus status;
    private final Set<RoleId> roleIds = new LinkedHashSet<>();

    private TenantMembership(TenantMembershipId id, UserId userId, TenantId tenantId,
                             MembershipStatus status) {
        super(id);
        this.userId = Objects.requireNonNull(userId);
        this.tenantId = Objects.requireNonNull(tenantId);
        this.status = Objects.requireNonNull(status);
    }

    public static TenantMembership invite(TenantMembershipId id, UserId userId,
                                          TenantId tenantId, Instant now) {
        TenantMembership membership = new TenantMembership(
                id, userId, tenantId, MembershipStatus.INVITED);
        membership.markCreated(Objects.requireNonNull(now));
        return membership;
    }

    public void activate(Instant now) {
        if (status == MembershipStatus.REMOVED) {
            throw new IllegalStateException("Removed membership cannot be activated");
        }
        status = MembershipStatus.ACTIVE;
        markUpdated(Objects.requireNonNull(now));
    }

    public void assignRole(Role role, Instant now) {
        if (status != MembershipStatus.ACTIVE) {
            throw new IllegalStateException("Roles can only be assigned to active memberships");
        }
        if (role.getScope() != com.hdplatform.modules.identity.domain.valueobject.RoleScope.TENANT
                || !tenantId.equals(role.getTenantId())) {
            throw new IllegalArgumentException("Role belongs to a different tenant or scope");
        }
        roleIds.add(role.getId());
        markUpdated(Objects.requireNonNull(now));
    }

    public UserId getUserId() { return userId; }
    public TenantId getTenantId() { return tenantId; }
    public MembershipStatus getStatus() { return status; }
    public Set<RoleId> getRoleIds() { return Collections.unmodifiableSet(roleIds); }
}
