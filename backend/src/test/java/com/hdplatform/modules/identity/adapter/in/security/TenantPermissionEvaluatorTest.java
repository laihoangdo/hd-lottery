package com.hdplatform.modules.identity.adapter.in.security;

import com.hdplatform.modules.tenant.application.context.TenantContext;
import com.hdplatform.modules.tenant.application.context.TenantContextHolder;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import com.hdplatform.modules.tenant.domain.valueobject.SiteKey;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TenantPermissionEvaluatorTest {

    private final TenantPermissionEvaluator evaluator = new TenantPermissionEvaluator();

    @AfterEach
    void clearTenant() {
        TenantContextHolder.clear();
    }

    @Test
    void grants_permission_when_authority_and_tenant_claim_match_request_tenant() {
        UUID tenantId = UUID.randomUUID();
        TenantContextHolder.set(new TenantContext(
                TenantId.of(tenantId), SiteKey.of("dealer-one")));

        boolean granted = evaluator.hasTenantPermission(
                token(tenantId.toString(), "cms:page:write"),
                "cms:page:write");

        assertThat(granted).isTrue();
    }

    @Test
    void denies_cross_tenant_token_even_when_permission_is_present() {
        TenantContextHolder.set(new TenantContext(
                TenantId.newId(), SiteKey.of("dealer-one")));

        boolean granted = evaluator.hasTenantPermission(
                token(UUID.randomUUID().toString(), "cms:page:write"),
                "cms:page:write");

        assertThat(granted).isFalse();
    }

    @Test
    void denies_missing_permission_and_malformed_tenant_claim() {
        TenantContextHolder.set(new TenantContext(
                TenantId.newId(), SiteKey.of("dealer-one")));

        assertThat(evaluator.hasTenantPermission(
                token("not-a-uuid", "cms:page:read"), "cms:page:write")).isFalse();
    }

    @Test
    void platform_permission_does_not_require_tenant_context() {
        assertThat(evaluator.hasPlatformPermission(
                token(null, "platform", "platform:tenant:manage"),
                "platform:tenant:manage")).isTrue();
    }

    @Test
    void tenant_token_cannot_use_platform_permission_even_if_authority_was_misassigned() {
        assertThat(evaluator.hasPlatformPermission(
                token(UUID.randomUUID().toString(), "tenant", "platform:tenant:manage"),
                "platform:tenant:manage")).isFalse();
    }

    private JwtAuthenticationToken token(String tenantId, String permission) {
        return token(tenantId, "tenant", permission);
    }

    private JwtAuthenticationToken token(String tenantId, String scope, String permission) {
        Jwt.Builder builder = Jwt.withTokenValue("token")
                .header("alg", "none")
                .subject("user-1")
                .issuedAt(Instant.parse("2026-07-20T00:00:00Z"))
                .expiresAt(Instant.parse("2026-07-20T01:00:00Z"))
                .claim("scope", scope)
                .claim("permissions", List.of(permission));
        if (tenantId != null) {
            builder.claim("tenant_id", tenantId);
        }
        return new JwtAuthenticationToken(
                builder.build(),
                List.of(new SimpleGrantedAuthority(permission)));
    }
}
