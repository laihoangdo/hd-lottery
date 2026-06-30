# Tenant - Policies

## P1. Domain Uniqueness
- Mỗi `tenant domain` (domain name) chỉ được ánh xạ tới **1 tenant**.

## P2. Status Transition
- `ACTIVE` -> `SUSPENDED` hợp lệ
- `SUSPENDED` -> `ACTIVE` hợp lệ
- `DELETED` không chuyển về các trạng thái khác (soft-delete hoặc hard-delete tùy thiết kế)

## P3. Settings Validation
- Locale/timezone bắt buộc thuộc whitelist chuẩn.
- Theme phải tham chiếu cấu hình hợp lệ.

