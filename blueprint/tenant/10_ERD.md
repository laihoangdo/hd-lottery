# Tenant - ERD (Conceptual)

## Tables (gợi ý)
- `tenant`
  - id (PK)
  - code (unique)
  - name
  - status
  - created_at, updated_at

- `tenant_domain`
  - id (PK)
  - tenant_id (FK)
  - domain (unique)
  - is_primary

- `tenant_settings`
  - tenant_id (PK/FK)
  - locale
  - timezone
  - locale_theme_version

- `tenant_theme`
  - tenant_id (PK/FK)
  - theme_code
  - config_json

- `tenant_subscription`
  - tenant_id (PK/FK)
  - plan_code
  - status
  - renewal_at

