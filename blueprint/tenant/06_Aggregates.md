# Tenant - Aggregates

## Aggregate: Tenant
**Entity/Value Objects (gợi ý)**
- TenantId
- TenantCode
- Name
- Status (ACTIVE/SUSPENDED/DELETED)
- TenantDomainMapping (collection)
- Theme
- Settings
- SubscriptionSummary

**Invariants**
- TenantCode duy nhất
- Domain mapping không trùng

