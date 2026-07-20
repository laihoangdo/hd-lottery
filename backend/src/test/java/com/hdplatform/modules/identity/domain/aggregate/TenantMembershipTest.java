package com.hdplatform.modules.identity.domain.aggregate;

import com.hdplatform.modules.identity.domain.valueobject.PermissionCode;
import com.hdplatform.modules.identity.domain.valueobject.RoleCode;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TenantMembershipTest {

    private static final Instant NOW = Instant.parse("2026-07-20T00:00:00Z");

    @Test
    void assigns_role_only_when_role_and_membership_share_tenant() {
        TenantId tenantId = TenantId.newId();
        TenantMembership membership = activeMembership(tenantId);
        Role role = Role.tenantRole(
                RoleId.newId(), tenantId, RoleCode.of("CMS_EDITOR"), "CMS Editor");
        role.grant(PermissionCode.of("cms:page:write"));

        membership.assignRole(role, NOW.plusSeconds(60));

        assertThat(membership.getRoleIds()).containsExactly(role.getId());
        assertThat(role.getPermissions()).containsExactly(
                PermissionCode.of("cms:page:write"));
    }

    @Test
    void rejects_role_from_another_tenant() {
        TenantMembership membership = activeMembership(TenantId.newId());
        Role foreignRole = Role.tenantRole(
                RoleId.newId(), TenantId.newId(),
                RoleCode.of("CMS_EDITOR"), "CMS Editor");

        assertThatThrownBy(() -> membership.assignRole(foreignRole, NOW.plusSeconds(60)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("different tenant");
    }

    @Test
    void rejects_role_assignment_until_membership_is_active() {
        TenantId tenantId = TenantId.newId();
        TenantMembership membership = TenantMembership.invite(
                TenantMembershipId.newId(), UserId.newId(), tenantId, NOW);
        Role role = Role.tenantRole(
                RoleId.newId(), tenantId, RoleCode.of("CMS_EDITOR"), "CMS Editor");

        assertThatThrownBy(() -> membership.assignRole(role, NOW.plusSeconds(60)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("active memberships");
    }

    @Test
    void platform_role_cannot_be_assigned_to_tenant_membership() {
        TenantMembership membership = activeMembership(TenantId.newId());
        Role platformRole = Role.platformRole(
                RoleId.newId(), RoleCode.of("PLATFORM_ADMIN"), "Platform Admin");

        assertThatThrownBy(() -> membership.assignRole(platformRole, NOW.plusSeconds(60)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private TenantMembership activeMembership(TenantId tenantId) {
        TenantMembership membership = TenantMembership.invite(
                TenantMembershipId.newId(), UserId.newId(), tenantId, NOW);
        membership.activate(NOW.plusSeconds(30));
        return membership;
    }
}
