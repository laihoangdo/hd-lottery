# Tenant - Ubiquitous Language

## Terms
- **Tenant**: một khách hàng SaaS (đại lý/chuỗi) có không gian cấu hình và dữ liệu riêng.
- **Tenant Domain**: domain name dùng để định danh tenant khi request đến hệ thống.
- **Theme**: bộ cấu hình hiển thị/brand cho tenant.
- **Subscription**: gói dịch vụ/chu kỳ trả phí gắn với tenant.
- **Settings**: cấu hình tùy biến (locale, timezone, flags...).

## Rules
- Không dùng “Account” thay cho “Tenant” trong code contract.
- “Domain” trong ngữ cảnh này luôn là **tenant domain mapping**, không phải cấu trúc kỹ thuật khác.

