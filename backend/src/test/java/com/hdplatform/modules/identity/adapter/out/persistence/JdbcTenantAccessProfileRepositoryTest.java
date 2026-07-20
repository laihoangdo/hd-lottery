package com.hdplatform.modules.identity.adapter.out.persistence;

import com.hdplatform.modules.identity.application.query.TenantAccessProfile;
import com.hdplatform.modules.identity.domain.aggregate.UserId;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcTenantAccessProfileRepositoryTest {

    @Test
    void aggregates_deduplicated_roles_and_permissions_into_canonical_claims() {
        UserId userId = UserId.newId();
        TenantId tenantId = TenantId.newId();
        UUID membershipId = UUID.randomUUID();

        TenantAccessProfile profile = JdbcTenantAccessProfileRepository.toProfile(
                userId,
                tenantId,
                List.of(
                        new AccessProfileRow(membershipId, "CMS_EDITOR", "cms:page:write"),
                        new AccessProfileRow(membershipId, "CMS_EDITOR", "cms:page:write"),
                        new AccessProfileRow(membershipId, "VIEWER", null)))
                .orElseThrow();

        assertThat(profile.roles()).extracting(role -> role.value())
                .containsExactlyInAnyOrder("CMS_EDITOR", "VIEWER");
        assertThat(profile.permissions()).extracting(permission -> permission.value())
                .containsExactly("cms:page:write");
        assertThat(profile.asJwtClaims()).containsAllEntriesOf(Map.of(
                "scope", "tenant",
                "sub", userId.getValue().toString(),
                "tenant_id", tenantId.getValue().toString(),
                "membership_id", membershipId.toString(),
                "roles", List.of("CMS_EDITOR", "VIEWER"),
                "permissions", List.of("cms:page:write")));
    }

    @Test
    void returns_empty_when_user_has_no_active_membership_for_tenant() {
        assertThat(JdbcTenantAccessProfileRepository.toProfile(
                UserId.newId(), TenantId.newId(), List.of())).isEmpty();
    }
}
