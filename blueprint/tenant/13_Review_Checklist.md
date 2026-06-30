# Tenant - Review Checklist

## Domain Boundaries
- [ ] Tenant aggregate có độc lập, không phụ thuộc trực tiếp domain khác?
- [ ] Domain ownership rõ ràng (Tenant chỉ thao tác table của Tenant domain)?

## Modeling
- [ ] TenantCode và tenant domain mapping có unique constraints?
- [ ] Settings/Theme có validation rõ ràng?

## Events & Consistency
- [ ] Các domain event đủ để các module khác phản ứng?
- [ ] Event payload không chứa dữ liệu nhạy cảm thừa?

## API Contract
- [ ] API endpoints thống nhất với prefix chuẩn platform?
- [ ] Authorization rules tương ứng các role đã định nghĩa?

## Migration
- [ ] Flyway scripts có versioning và reversible plan (nếu yêu cầu)?

