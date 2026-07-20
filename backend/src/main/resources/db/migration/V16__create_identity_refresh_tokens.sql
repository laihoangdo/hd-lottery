CREATE TABLE identity_refresh_tokens
(
    id UUID PRIMARY KEY,
    family_id UUID NOT NULL,
    user_id UUID NOT NULL REFERENCES identity_users(id) ON DELETE CASCADE,
    scope VARCHAR(20) NOT NULL CHECK (scope IN ('PLATFORM', 'TENANT')),
    tenant_id UUID REFERENCES tenants(id) ON DELETE CASCADE,
    token_hash CHAR(64) NOT NULL UNIQUE,
    expires_at TIMESTAMP NOT NULL,
    revoked_at TIMESTAMP,
    replaced_by UUID REFERENCES identity_refresh_tokens(id),
    created_at TIMESTAMP NOT NULL,
    last_used_at TIMESTAMP,
    CONSTRAINT ck_identity_refresh_scope CHECK (
        (scope='PLATFORM' AND tenant_id IS NULL) OR (scope='TENANT' AND tenant_id IS NOT NULL))
);

CREATE INDEX idx_identity_refresh_family ON identity_refresh_tokens(family_id);
CREATE INDEX idx_identity_refresh_user ON identity_refresh_tokens(user_id, expires_at);
