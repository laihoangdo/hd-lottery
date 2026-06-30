# Tenant - Sequence Diagrams

## SD-001 Provision Tenant
- Platform Admin -> Tenant API: POST /tenants
- API -> Tenant Application Service: provisionTenant()
- Tenant Service -> Tenant Repository: save()
- Tenant Service -> DomainEventPublisher: publish(TenantProvisioned)
- API -> Client: 201 Created

## SD-002 Resolve Tenant by Domain
- Client -> API (Host header)
- Middleware -> Tenant Resolver: resolveByDomain(host)
- Resolver -> Tenant Repository: findByDomain()
- Resolver -> Middleware: tenantContext
- Middleware -> Downstream services: execute request

