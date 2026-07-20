package com.hdplatform.modules.identity.adapter.out.persistence;

import com.hdplatform.modules.identity.application.auth.*;
import com.hdplatform.modules.identity.application.port.AuthenticationRepository;
import com.hdplatform.modules.identity.domain.aggregate.UserId;
import com.hdplatform.modules.identity.domain.valueobject.UserStatus;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class JdbcAuthenticationRepository implements AuthenticationRepository {
    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public Optional<UserCredentials> findCredentialsByEmail(String email) {
        return jdbc.query("SELECT id, email, password_hash, status FROM identity_users WHERE lower(email) = :email",
                Map.of("email", email), (rs, n) -> new UserCredentials(UserId.of(rs.getObject("id", UUID.class)),
                        rs.getString("email"), rs.getString("password_hash"), UserStatus.valueOf(rs.getString("status"))))
                .stream().findFirst();
    }

    @Override public Optional<AccessGrant> findPlatformGrant(UserId userId) {
        String sql = """
                SELECT r.code role_code, p.code permission_code FROM identity_users u
                JOIN identity_platform_role_assignments a ON a.user_id=u.id
                JOIN identity_roles r ON r.id=a.role_id AND r.scope='PLATFORM'
                LEFT JOIN identity_role_permissions rp ON rp.role_id=r.id
                LEFT JOIN identity_permissions p ON p.code=rp.permission_code
                WHERE u.id=:userId AND u.status='ACTIVE' ORDER BY r.code,p.code""";
        return grant(userId, AuthenticationScope.PLATFORM, null, jdbc.query(sql,
                Map.of("userId", userId.getValue()), JdbcAuthenticationRepository::accessRow));
    }

    @Override public Optional<AccessGrant> findTenantGrant(UserId userId, TenantId tenantId) {
        String sql = """
                SELECT r.code role_code, p.code permission_code FROM identity_tenant_memberships m
                JOIN identity_users u ON u.id=m.user_id AND u.status='ACTIVE'
                JOIN identity_role_assignments a ON a.membership_id=m.id AND a.tenant_id=m.tenant_id
                JOIN identity_roles r ON r.id=a.role_id AND r.tenant_id=m.tenant_id AND r.scope='TENANT'
                LEFT JOIN identity_role_permissions rp ON rp.role_id=r.id
                LEFT JOIN identity_permissions p ON p.code=rp.permission_code
                WHERE m.user_id=:userId AND m.tenant_id=:tenantId AND m.status='ACTIVE'
                ORDER BY r.code,p.code""";
        return grant(userId, AuthenticationScope.TENANT, tenantId, jdbc.query(sql,
                Map.of("userId", userId.getValue(), "tenantId", tenantId.getValue()), JdbcAuthenticationRepository::accessRow));
    }

    private static AccessRow accessRow(java.sql.ResultSet rs, int n) throws java.sql.SQLException {
        return new AccessRow(rs.getString("role_code"), rs.getString("permission_code"));
    }

    static Optional<AccessGrant> grant(UserId userId, AuthenticationScope scope, TenantId tenantId, List<AccessRow> rows) {
        if (rows.isEmpty()) return Optional.empty();
        Set<String> roles = new TreeSet<>(); Set<String> permissions = new TreeSet<>();
        rows.forEach(row -> { if (row.role() != null) roles.add(row.role()); if (row.permission() != null) permissions.add(row.permission()); });
        return Optional.of(new AccessGrant(userId, scope, tenantId, roles, permissions));
    }

    @Override public Optional<RefreshSession> findRefreshSessionForUpdate(String hash) {
        String sql = "SELECT * FROM identity_refresh_tokens WHERE token_hash=:hash FOR UPDATE";
        return jdbc.query(sql, Map.of("hash", hash), (rs, n) -> new RefreshSession(
                rs.getObject("id", UUID.class), rs.getObject("family_id", UUID.class),
                UserId.of(rs.getObject("user_id", UUID.class)), AuthenticationScope.valueOf(rs.getString("scope")),
                rs.getObject("tenant_id") == null ? null : TenantId.of(rs.getObject("tenant_id", UUID.class)),
                rs.getString("token_hash"), rs.getTimestamp("expires_at").toInstant(),
                rs.getTimestamp("revoked_at") == null ? null : rs.getTimestamp("revoked_at").toInstant())).stream().findFirst();
    }

    @Override public void saveRefreshSession(RefreshSession s, Instant createdAt) {
        insert(s, createdAt);
    }

    @Override public void rotateRefreshSession(UUID currentId, RefreshSession replacement, Instant now) {
        insert(replacement, now);
        int changed = jdbc.update("UPDATE identity_refresh_tokens SET revoked_at=:now,replaced_by=:replacement,last_used_at=:now WHERE id=:id AND revoked_at IS NULL",
                Map.of("now", Timestamp.from(now), "replacement", replacement.id(), "id", currentId));
        if (changed != 1) throw new IllegalStateException("Refresh token was concurrently rotated");
    }

    private void insert(RefreshSession s, Instant now) {
        Map<String,Object> values = new HashMap<>();
        values.put("id",s.id()); values.put("family",s.familyId()); values.put("user",s.userId().getValue());
        values.put("scope",s.scope().name()); values.put("tenant",s.tenantId()==null?null:s.tenantId().getValue());
        values.put("hash",s.tokenHash()); values.put("expires",Timestamp.from(s.expiresAt())); values.put("created",Timestamp.from(now));
        jdbc.update("""
                INSERT INTO identity_refresh_tokens(id,family_id,user_id,scope,tenant_id,token_hash,expires_at,created_at)
                VALUES(:id,:family,:user,:scope,:tenant,:hash,:expires,:created)
                """, values);
    }

    @Override public void revokeFamily(UUID familyId, Instant now) {
        jdbc.update("UPDATE identity_refresh_tokens SET revoked_at=COALESCE(revoked_at,:now) WHERE family_id=:family",
                Map.of("now", Timestamp.from(now), "family", familyId));
    }
}

record AccessRow(String role, String permission) {}
