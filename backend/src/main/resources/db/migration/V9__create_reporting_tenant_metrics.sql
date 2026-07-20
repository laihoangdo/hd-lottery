CREATE TABLE reporting_tenant_metrics
(
    tenant_id UUID NOT NULL,
    metric_key VARCHAR(100) NOT NULL,
    metric_value BIGINT NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    PRIMARY KEY (tenant_id, metric_key),
    CONSTRAINT fk_reporting_metrics_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id),
    CONSTRAINT ck_reporting_metrics_non_negative CHECK (metric_value >= 0)
);

INSERT INTO reporting_tenant_metrics (tenant_id, metric_key, metric_value, updated_at)
SELECT tenant_id, 'cms.page.total', COUNT(*), CURRENT_TIMESTAMP
FROM cms_pages GROUP BY tenant_id
UNION ALL
SELECT tenant_id, 'cms.page.draft', COUNT(*), CURRENT_TIMESTAMP
FROM cms_pages WHERE status = 'DRAFT' GROUP BY tenant_id
UNION ALL
SELECT tenant_id, 'cms.page.published', COUNT(*), CURRENT_TIMESTAMP
FROM cms_pages WHERE status = 'PUBLISHED' GROUP BY tenant_id
UNION ALL
SELECT tenant_id, 'cms.page.archived', COUNT(*), CURRENT_TIMESTAMP
FROM cms_pages WHERE status = 'ARCHIVED' GROUP BY tenant_id
UNION ALL
SELECT tenant_id, 'media.asset.total', COUNT(*), CURRENT_TIMESTAMP
FROM media_assets WHERE status <> 'DELETED' GROUP BY tenant_id
UNION ALL
SELECT tenant_id, 'media.asset.private', COUNT(*), CURRENT_TIMESTAMP
FROM media_assets WHERE status = 'READY' AND visibility = 'PRIVATE' GROUP BY tenant_id
UNION ALL
SELECT tenant_id, 'media.asset.public', COUNT(*), CURRENT_TIMESTAMP
FROM media_assets WHERE status = 'READY' AND visibility = 'PUBLIC' GROUP BY tenant_id;

INSERT INTO identity_permissions (code, description) VALUES
    ('reporting:dashboard:read', 'View tenant operational dashboards');
