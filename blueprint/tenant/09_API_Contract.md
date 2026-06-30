# Tenant - API Contract

## REST Endpoints (gợi ý)

### Provision Tenant
- `POST /api/platform/tenants`
- Request: tenantCode, name, domain, theme, settings
- Response: 201 + tenantId

### Update Tenant Settings
- `PATCH /api/platform/tenants/{tenantId}/settings`
- Request: theme/settings fields
- Response: 200

### Suspend Tenant
- `POST /api/platform/tenants/{tenantId}/suspend`
- Response: 200

### Resolve Tenant Context (internal)
- `GET /internal/tenants/resolve?domain={host}`
- Response: tenant context (id/code/status)

## Authentication/Authorization
- Platform Admin: full CRUD
- Tenant Admin: manage settings within tenant

