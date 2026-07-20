package com.hdplatform.modules.identity.adapter.out.persistence;

import com.hdplatform.modules.tenant.application.port.TenantIdentityProvisioner;
import com.hdplatform.modules.tenant.application.port.TenantOwnerAccount;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import com.hdplatform.shared.domain.ClockProvider;
import com.hdplatform.shared.domain.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JdbcTenantIdentityProvisioner implements TenantIdentityProvisioner {
    private static final Map<String, List<String>> ROLE_PERMISSIONS = Map.of(
            "website_owner", List.of("cms:page:write", "media:asset:write", "reporting:dashboard:read",
                    "analytics:dashboard:read", "tenant:template:switch"),
            "editor", List.of("cms:page:write", "media:asset:write"),
            "viewer", List.of("reporting:dashboard:read", "analytics:dashboard:read"));
    private static final Map<String, String> ROLE_NAMES = Map.of(
            "website_owner", "Website Owner", "editor", "Editor", "viewer", "Viewer");

    private final JdbcTemplate jdbc;
    private final PasswordEncoder passwordEncoder;
    private final ClockProvider clock;

    @Override
    public void provisionDefaultRoles(TenantId tenantId) {
        Instant now = clock.now();
        ROLE_PERMISSIONS.forEach((code, permissions) -> {
            UUID roleId = UUID.randomUUID();
            jdbc.update("""
                    INSERT INTO identity_roles (id, scope, tenant_id, code, name, created_at, updated_at)
                    VALUES (?, 'TENANT', ?, ?, ?, ?, ?)
                    ON CONFLICT (tenant_id, code) WHERE scope = 'TENANT' DO NOTHING
                    """, roleId, tenantId.getValue(), code, ROLE_NAMES.get(code), Timestamp.from(now), Timestamp.from(now));
            UUID persistedRoleId = jdbc.queryForObject(
                    "SELECT id FROM identity_roles WHERE tenant_id = ? AND scope = 'TENANT' AND code = ?",
                    UUID.class, tenantId.getValue(), code);
            permissions.forEach(permission -> jdbc.update("""
                    INSERT INTO identity_role_permissions (role_id, permission_code)
                    VALUES (?, ?) ON CONFLICT DO NOTHING
                    """, persistedRoleId, permission));
        });
    }

    @Override
    public void provisionWebsiteOwner(TenantId tenantId, TenantOwnerAccount owner) {
        String email = owner.email().trim().toLowerCase(java.util.Locale.ROOT);
        List<UserRow> users = jdbc.query(
                "SELECT id, status FROM identity_users WHERE lower(email) = ?",
                (rs, row) -> new UserRow(rs.getObject("id", UUID.class), rs.getString("status")), email);
        UUID userId;
        if (users.isEmpty()) {
            userId = UUID.randomUUID();
            Instant now = clock.now();
            jdbc.update("""
                    INSERT INTO identity_users(id,email,full_name,password_hash,status,created_at,updated_at)
                    VALUES(?,?,?,?, 'ACTIVE', ?, ?)
                    """, userId, email, owner.fullName().trim(), passwordEncoder.encode(owner.initialPassword()),
                    Timestamp.from(now), Timestamp.from(now));
        } else {
            UserRow user = users.getFirst();
            if (!"ACTIVE".equals(user.status())) {
                throw new DomainException("Existing owner account is not active");
            }
            userId = user.id();
        }

        UUID membershipId = UUID.randomUUID();
        Instant now = clock.now();
        jdbc.update("""
                INSERT INTO identity_tenant_memberships(id,tenant_id,user_id,status,created_at,updated_at)
                VALUES(?,?,?,'ACTIVE',?,?)
                """, membershipId, tenantId.getValue(), userId, Timestamp.from(now), Timestamp.from(now));
        UUID ownerRoleId = jdbc.queryForObject(
                "SELECT id FROM identity_roles WHERE tenant_id=? AND scope='TENANT' AND code='website_owner'",
                UUID.class, tenantId.getValue());
        jdbc.update("""
                INSERT INTO identity_role_assignments(membership_id,role_id,tenant_id)
                VALUES(?,?,?)
                """, membershipId, ownerRoleId, tenantId.getValue());
    }
}

record UserRow(UUID id, String status) {}
