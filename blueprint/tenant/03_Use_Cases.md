# Tenant - Use Cases

## UC-001 Provision Tenant
**Actor**: Platform Admin

**Flow**
1. Nhập tenant info (code, name, domain)
2. Hệ thống validate domain mapping chưa tồn tại
3. Tạo tenant với trạng thái `ACTIVE`
4. Ghi cấu hình mặc định (theme/settings)

**Postconditions**
- Tenant tồn tại, domain mapping sẵn sàng để routing.

## UC-002 Change Tenant Settings
**Actor**: Tenant Admin / Platform Admin

**Flow**
1. Gửi request thay đổi settings/theme
2. Validate & persist
3. Publish event `TenantSettingsUpdated`

## UC-003 Suspend Tenant
**Actor**: Platform Admin
- Đổi trạng thái tenant sang `SUSPENDED`
- Publish `TenantSuspended`

## UC-004 Resolve Tenant By Domain
**Actor**: System (middleware/routing)
- Nhận `Host` header
- Query tenant by domain mapping
- Trả tenant context hoặc 404

