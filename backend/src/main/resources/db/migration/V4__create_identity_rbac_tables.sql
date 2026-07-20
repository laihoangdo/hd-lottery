CREATE TABLE identity_users
(
    id UUID PRIMARY KEY,
    email VARCHAR(320) NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT ck_identity_users_status
        CHECK (status IN ('ACTIVE', 'INACTIVE', 'LOCKED', 'PENDING'))
);

CREATE TABLE identity_permissions
(
    code VARCHAR(100) PRIMARY KEY,
    description VARCHAR(255) NOT NULL
);

CREATE TABLE identity_roles
(
    id UUID PRIMARY KEY,
    scope VARCHAR(20) NOT NULL,
    tenant_id UUID,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_identity_roles_tenant
        FOREIGN KEY (tenant_id) REFERENCES tenants(id),
    CONSTRAINT ck_identity_roles_scope
        CHECK ((scope = 'PLATFORM' AND tenant_id IS NULL)
            OR (scope = 'TENANT' AND tenant_id IS NOT NULL)),
    CONSTRAINT uk_identity_roles_id_tenant UNIQUE (id, tenant_id)
);

CREATE UNIQUE INDEX uk_identity_roles_platform_code
    ON identity_roles (code) WHERE scope = 'PLATFORM';
CREATE UNIQUE INDEX uk_identity_roles_tenant_code
    ON identity_roles (tenant_id, code) WHERE scope = 'TENANT';

CREATE TABLE identity_role_permissions
(
    role_id UUID NOT NULL,
    permission_code VARCHAR(100) NOT NULL,
    PRIMARY KEY (role_id, permission_code),
    CONSTRAINT fk_identity_role_permissions_role
        FOREIGN KEY (role_id) REFERENCES identity_roles(id) ON DELETE CASCADE,
    CONSTRAINT fk_identity_role_permissions_permission
        FOREIGN KEY (permission_code) REFERENCES identity_permissions(code)
);

CREATE TABLE identity_tenant_memberships
(
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    user_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_identity_memberships_tenant
        FOREIGN KEY (tenant_id) REFERENCES tenants(id),
    CONSTRAINT fk_identity_memberships_user
        FOREIGN KEY (user_id) REFERENCES identity_users(id),
    CONSTRAINT uk_identity_memberships_tenant_user UNIQUE (tenant_id, user_id),
    CONSTRAINT uk_identity_memberships_id_tenant UNIQUE (id, tenant_id),
    CONSTRAINT ck_identity_memberships_status
        CHECK (status IN ('INVITED', 'ACTIVE', 'SUSPENDED', 'REMOVED'))
);

CREATE TABLE identity_role_assignments
(
    membership_id UUID NOT NULL,
    role_id UUID NOT NULL,
    tenant_id UUID NOT NULL,
    PRIMARY KEY (membership_id, role_id),
    CONSTRAINT fk_identity_assignments_membership_tenant
        FOREIGN KEY (membership_id, tenant_id)
            REFERENCES identity_tenant_memberships(id, tenant_id) ON DELETE CASCADE,
    CONSTRAINT fk_identity_assignments_role_tenant
        FOREIGN KEY (role_id, tenant_id)
            REFERENCES identity_roles(id, tenant_id) ON DELETE CASCADE
);

CREATE TABLE identity_platform_role_assignments
(
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_identity_platform_assignments_user
        FOREIGN KEY (user_id) REFERENCES identity_users(id) ON DELETE CASCADE,
    CONSTRAINT fk_identity_platform_assignments_role
        FOREIGN KEY (role_id) REFERENCES identity_roles(id) ON DELETE CASCADE
);

INSERT INTO identity_permissions (code, description) VALUES
    ('cms:page:write', 'Create and manage CMS pages for a tenant'),
    ('platform:tenant:manage', 'Create and manage platform tenants');

CREATE INDEX idx_identity_memberships_user_status
    ON identity_tenant_memberships (user_id, status);
