package com.hdplatform.modules.identity.domain.aggregate;

import com.hdplatform.modules.identity.domain.valueobject.PermissionCode;
import com.hdplatform.modules.identity.domain.valueobject.RoleCode;
import com.hdplatform.modules.identity.domain.valueobject.RoleScope;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import com.hdplatform.shared.domain.AggregateRoot;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public final class Role extends AggregateRoot<RoleId> {
    private final RoleScope scope;
    private final TenantId tenantId;
    private final RoleCode code;
    private String name;
    private final Set<PermissionCode> permissions = new LinkedHashSet<>();

    private Role(RoleId id, RoleScope scope, TenantId tenantId, RoleCode code, String name) {
        super(id);
        this.scope = Objects.requireNonNull(scope);
        if ((scope == RoleScope.TENANT) != (tenantId != null)) {
            throw new IllegalArgumentException("Tenant role must have tenantId; platform role must not");
        }
        this.tenantId = tenantId;
        this.code = Objects.requireNonNull(code);
        this.name = requireName(name);
    }

    public static Role tenantRole(RoleId id, TenantId tenantId, RoleCode code, String name) {
        return new Role(id, RoleScope.TENANT, Objects.requireNonNull(tenantId), code, name);
    }

    public static Role platformRole(RoleId id, RoleCode code, String name) {
        return new Role(id, RoleScope.PLATFORM, null, code, name);
    }

    public void grant(PermissionCode permission) { permissions.add(Objects.requireNonNull(permission)); }
    public void revoke(PermissionCode permission) { permissions.remove(Objects.requireNonNull(permission)); }
    public Set<PermissionCode> getPermissions() { return Collections.unmodifiableSet(permissions); }
    public RoleScope getScope() { return scope; }
    public TenantId getTenantId() { return tenantId; }
    public RoleCode getCode() { return code; }
    public String getName() { return name; }

    private static String requireName(String name) {
        if (name == null || name.isBlank() || name.trim().length() > 100) {
            throw new IllegalArgumentException("Role name must contain between 1 and 100 characters");
        }
        return name.trim();
    }
}
