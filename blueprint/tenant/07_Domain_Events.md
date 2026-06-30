# Tenant - Domain Events

## Event: TenantProvisioned
- Trigger: khi tạo tenant
- Payload: tenantId, tenantCode, domains, status

## Event: TenantSettingsUpdated
- Trigger: cập nhật theme/settings
- Payload: tenantId, changedFields, updatedAt

## Event: TenantSuspended
- Trigger: suspend tenant
- Payload: tenantId, reason (optional)

## Event: TenantReactivated
- Trigger: active lại
- Payload: tenantId

