package com.hdplatform.modules.identity.adapter.out.persistence;

import com.hdplatform.modules.identity.application.port.RoleSummaryRepository;
import com.hdplatform.modules.identity.application.query.RoleSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JdbcRoleSummaryRepository implements RoleSummaryRepository {
    private final JdbcTemplate jdbc;

    @Override
    public List<RoleSummary> findPlatformRoles() {
        return jdbc.query("""
                SELECT r.id, r.scope, r.tenant_id, r.code, r.name,
                       COALESCE(array_agg(rp.permission_code ORDER BY rp.permission_code)
                           FILTER (WHERE rp.permission_code IS NOT NULL), ARRAY[]::varchar[]) AS permissions
                FROM identity_roles r
                LEFT JOIN identity_role_permissions rp ON rp.role_id = r.id
                WHERE r.scope = 'PLATFORM'
                GROUP BY r.id, r.scope, r.tenant_id, r.code, r.name
                ORDER BY r.code
                """, (rs, row) -> new RoleSummary(
                rs.getObject("id", UUID.class), rs.getString("scope"),
                rs.getObject("tenant_id", UUID.class), rs.getString("code"), rs.getString("name"),
                List.of((String[]) rs.getArray("permissions").getArray())));
    }
}
