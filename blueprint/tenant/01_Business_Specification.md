# Tenant - Business Specification

## Purpose
Mô tả business scope của **Tenant Domain** trong HD Platform.

## Responsibility (Bounded Context)
- Tenant (định danh và trạng thái)
- Domain mapping (domain -> tenant)
- Theme / Branding
- Subscription / Plan (nếu áp dụng)
- Settings (cấu hình theo tenant)

## Primary Workflows (High-level)
1. Provision Tenant (tạo tenant, cấu hình domain)
2. Configure Tenant Settings (theme, feature flags, locales...)
3. Activate / Suspend Tenant
4. Manage Subscription lifecycle (trial -> paid -> renewal)

## Out of Scope
Các domain khác (Identity, Lottery, CMS...) không sở hữu trực tiếp dữ liệu Tenant; chỉ đọc thông qua API hoặc Domain Event.

