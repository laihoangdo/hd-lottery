CREATE TABLE analytics_daily_events
(
    tenant_id UUID NOT NULL,
    event_date DATE NOT NULL,
    event_name VARCHAR(50) NOT NULL,
    event_count BIGINT NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    PRIMARY KEY (tenant_id, event_date, event_name),
    CONSTRAINT fk_analytics_events_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id),
    CONSTRAINT ck_analytics_events_count CHECK (event_count >= 0)
);

CREATE INDEX idx_analytics_events_tenant_name_date
    ON analytics_daily_events (tenant_id, event_name, event_date DESC);

INSERT INTO identity_permissions (code, description) VALUES
    ('analytics:dashboard:read', 'View tenant analytics dashboards');
