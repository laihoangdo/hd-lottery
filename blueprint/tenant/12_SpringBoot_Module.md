# Tenant - SpringBoot Module

## Package Layout (gợi ý)
- `com.hdplatform.tenant`
  - `api` (controllers)
  - `application` (use cases / orchestration)
  - `domain` (aggregates, entities, value objects)
  - `domain.event` (domain events)
  - `infrastructure` (repositories)
  - `config` (module configs)

## Integration Points
- Query tenant by domain for routing/middleware
- Publish domain events for downstream modules

## Non-functional
- Index domain mapping for fast host resolution
- Ensure multi-tenant isolation through tenantId context

