ALTER TABLE cms_pages
    ADD COLUMN published_at TIMESTAMP;

CREATE INDEX idx_cms_pages_tenant_published_slug
    ON cms_pages (tenant_id, slug)
    WHERE status = 'PUBLISHED';
