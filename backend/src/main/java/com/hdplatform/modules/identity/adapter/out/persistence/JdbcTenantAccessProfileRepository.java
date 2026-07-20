package com.hdplatform.modules.identity.adapter.out.persistence;

import com.hdplatform.modules.identity.application.port.TenantAccessProfileRepository;
import com.hdplatform.modules.identity.application.query.TenantAccessProfile;
import com.hdplatform.modules.identity.domain.aggregate.TenantMembershipId;
import com.hdplatform.modules.identity.domain.aggregate.UserId;
import com.hdplatform.modules.identity.domain.valueobject.PermissionCode;
import com.hdplatform.modules.identity.domain.valueobject.RoleCode;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JdbcTenantAccessProfileRepository implements TenantAccessProfileRepository {

    private static final String FIND_ACTIVE_PROFILE = """
            SELECT m.id AS membership_id,
                   r.code AS role_code,
                   p.code AS permission_code
            FROM identity_tenant_memberships m
            JOIN identity_users u
              ON u.id = m.user_id AND u.status = 'ACTIVE'
            LEFT JOIN identity_role_assignments a
              ON a.membership_id = m.id AND a.tenant_id = m.tenant_id
            LEFT JOIN identity_roles r
              ON r.id = a.role_id AND r.tenant_id = m.tenant_id AND r.scope = 'TENANT'
            LEFT JOIN identity_role_permissions rp ON rp.role_id = r.id
            LEFT JOIN identity_permissions p ON p.code = rp.permission_code
            WHERE m.user_id = :userId
              AND m.tenant_id = :tenantId
              AND m.status = 'ACTIVE'
            ORDER BY r.code, p.code
            """;

    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public Optional<TenantAccessProfile> findActive(UserId userId, TenantId tenantId) {
        List<AccessProfileRow> rows = jdbc.query(
                FIND_ACTIVE_PROFILE,
                Map.of("userId", userId.getValue(), "tenantId", tenantId.getValue()),
                (resultSet, rowNumber) -> new AccessProfileRow(
                        resultSet.getObject("membership_id", UUID.class),
                        resultSet.getString("role_code"),
                        resultSet.getString("permission_code")));
        return toProfile(userId, tenantId, rows);
    }

    static Optional<TenantAccessProfile> toProfile(
            UserId userId, TenantId tenantId, List<AccessProfileRow> rows) {
        if (rows.isEmpty()) {
            return Optional.empty();
        }
        UUID membershipId = rows.getFirst().membershipId();
        TreeSet<RoleCode> roles = new TreeSet<>(
                java.util.Comparator.comparing(RoleCode::value));
        TreeSet<PermissionCode> permissions = new TreeSet<>(
                java.util.Comparator.comparing(PermissionCode::value));
        rows.forEach(row -> {
            if (row.roleCode() != null) roles.add(RoleCode.of(row.roleCode()));
            if (row.permissionCode() != null) permissions.add(PermissionCode.of(row.permissionCode()));
            if (!membershipId.equals(row.membershipId())) {
                throw new IllegalStateException("Access profile query returned mixed memberships");
            }
        });
        return Optional.of(new TenantAccessProfile(
                userId, TenantMembershipId.of(membershipId), tenantId, roles, permissions));
    }
}

record AccessProfileRow(UUID membershipId, String roleCode, String permissionCode) {
}
