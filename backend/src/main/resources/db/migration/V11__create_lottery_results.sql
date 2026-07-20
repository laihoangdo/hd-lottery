CREATE TABLE lottery_results
(
    id UUID PRIMARY KEY,
    province_code VARCHAR(20) NOT NULL,
    province_name VARCHAR(100) NOT NULL,
    region VARCHAR(20) NOT NULL,
    draw_date DATE NOT NULL,
    prizes_json JSONB NOT NULL,
    status VARCHAR(20) NOT NULL,
    published_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT uk_lottery_results_province_date UNIQUE (province_code, draw_date),
    CONSTRAINT ck_lottery_results_region CHECK (region IN ('NORTH', 'CENTRAL', 'SOUTH')),
    CONSTRAINT ck_lottery_results_status CHECK (status IN ('DRAFT', 'PUBLISHED'))
);

CREATE INDEX idx_lottery_results_public_lookup
    ON lottery_results (draw_date DESC, region, province_name)
    WHERE status = 'PUBLISHED';

INSERT INTO identity_permissions (code, description) VALUES
    ('lottery:result:manage', 'Create and publish canonical lottery results');
