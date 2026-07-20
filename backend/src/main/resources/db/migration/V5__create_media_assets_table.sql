CREATE TABLE media_assets
(
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    object_key VARCHAR(500) NOT NULL UNIQUE,
    original_name VARCHAR(255) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    size_bytes BIGINT NOT NULL,
    checksum_sha256 VARCHAR(64) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_media_assets_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id),
    CONSTRAINT ck_media_assets_size CHECK (size_bytes > 0),
    CONSTRAINT ck_media_assets_status CHECK (status IN ('READY', 'DELETED'))
);

CREATE INDEX idx_media_assets_tenant_status_created
    ON media_assets (tenant_id, status, created_at DESC);

INSERT INTO identity_permissions (code, description) VALUES
    ('media:asset:write', 'Upload and manage media assets for a tenant');
