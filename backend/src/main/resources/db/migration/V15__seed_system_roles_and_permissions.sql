INSERT INTO identity_permissions (code, description) VALUES
    ('platform:catalog:manage', 'Manage platform verticals and website templates'),
    ('platform:identity:manage', 'Manage platform administrators and access'),
    ('tenant:template:switch', 'Switch the template of the current tenant website')
ON CONFLICT (code) DO UPDATE SET description = EXCLUDED.description;

INSERT INTO identity_roles (id, scope, tenant_id, code, name, created_at, updated_at) VALUES
    ('00000000-0000-0000-0000-000000000301', 'PLATFORM', NULL, 'super_admin', 'Super Admin', NOW(), NOW()),
    ('00000000-0000-0000-0000-000000000302', 'PLATFORM', NULL, 'admin', 'Admin', NOW(), NOW())
ON CONFLICT DO NOTHING;

INSERT INTO identity_role_permissions (role_id, permission_code)
SELECT '00000000-0000-0000-0000-000000000301'::uuid, code
FROM identity_permissions
WHERE code LIKE 'platform:%' OR code = 'lottery:result:manage'
ON CONFLICT DO NOTHING;

INSERT INTO identity_role_permissions (role_id, permission_code)
SELECT '00000000-0000-0000-0000-000000000302'::uuid, code
FROM identity_permissions
WHERE code IN ('platform:tenant:manage', 'platform:catalog:manage', 'lottery:result:manage')
ON CONFLICT DO NOTHING;

INSERT INTO identity_roles (id, scope, tenant_id, code, name, created_at, updated_at)
SELECT md5(t.id::text || ':website_owner')::uuid, 'TENANT', t.id, 'website_owner', 'Website Owner', NOW(), NOW()
FROM tenants t
ON CONFLICT DO NOTHING;

INSERT INTO identity_roles (id, scope, tenant_id, code, name, created_at, updated_at)
SELECT md5(t.id::text || ':editor')::uuid, 'TENANT', t.id, 'editor', 'Editor', NOW(), NOW()
FROM tenants t
ON CONFLICT DO NOTHING;

INSERT INTO identity_roles (id, scope, tenant_id, code, name, created_at, updated_at)
SELECT md5(t.id::text || ':viewer')::uuid, 'TENANT', t.id, 'viewer', 'Viewer', NOW(), NOW()
FROM tenants t
ON CONFLICT DO NOTHING;

INSERT INTO identity_role_permissions (role_id, permission_code)
SELECT r.id, p.code
FROM identity_roles r
JOIN identity_permissions p ON p.code IN (
    'cms:page:write', 'media:asset:write', 'reporting:dashboard:read',
    'analytics:dashboard:read', 'tenant:template:switch')
WHERE r.scope = 'TENANT' AND r.code = 'website_owner'
ON CONFLICT DO NOTHING;

INSERT INTO identity_role_permissions (role_id, permission_code)
SELECT r.id, p.code
FROM identity_roles r
JOIN identity_permissions p ON p.code IN ('cms:page:write', 'media:asset:write')
WHERE r.scope = 'TENANT' AND r.code = 'editor'
ON CONFLICT DO NOTHING;

INSERT INTO identity_role_permissions (role_id, permission_code)
SELECT r.id, p.code
FROM identity_roles r
JOIN identity_permissions p ON p.code IN ('reporting:dashboard:read', 'analytics:dashboard:read')
WHERE r.scope = 'TENANT' AND r.code = 'viewer'
ON CONFLICT DO NOTHING;
