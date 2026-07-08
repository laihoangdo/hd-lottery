CREATE TABLE tenants
(
    id UUID PRIMARY KEY,

    site_key VARCHAR(100) NOT NULL,

    domain_name VARCHAR(255) NOT NULL,

    display_name VARCHAR(255) NOT NULL,

    name VARCHAR(255) NOT NULL,
    code VARCHAR(255) NOT NULL,

    logo_url VARCHAR(500),

    hotline VARCHAR(50),

    status VARCHAR(30) NOT NULL,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL
);

ALTER TABLE tenants
    ADD CONSTRAINT uk_tenants_site_key
        UNIQUE (site_key);

ALTER TABLE tenants
    ADD CONSTRAINT uk_tenants_domain_name
        UNIQUE (domain_name);

CREATE INDEX idx_tenants_status
    ON tenants(status);

CREATE INDEX idx_tenants_created_at
    ON tenants(created_at);