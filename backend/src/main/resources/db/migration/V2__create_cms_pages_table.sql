CREATE TABLE cms_pages
(
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    slug VARCHAR(160) NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_cms_pages_tenant
        FOREIGN KEY (tenant_id) REFERENCES tenants(id),
    CONSTRAINT uk_cms_pages_tenant_slug
        UNIQUE (tenant_id, slug),
    CONSTRAINT ck_cms_pages_status
        CHECK (status IN ('DRAFT', 'PUBLISHED', 'ARCHIVED'))
);

CREATE INDEX idx_cms_pages_tenant_status_updated
    ON cms_pages (tenant_id, status, updated_at DESC);
