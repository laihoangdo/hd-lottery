CREATE TABLE verticals (
    id UUID PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE website_templates (
    id UUID PRIMARY KEY,
    vertical_id UUID NOT NULL REFERENCES verticals(id),
    code VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    layout_config JSONB NOT NULL DEFAULT '{}'::jsonb,
    default_colors JSONB NOT NULL DEFAULT '{}'::jsonb,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_website_templates_vertical_active
    ON website_templates(vertical_id, active);

INSERT INTO verticals (id, code, name, description, active, created_at, updated_at)
VALUES ('00000000-0000-0000-0000-000000000101', 'lottery', 'Xổ số',
        'Website và công cụ vận hành dành cho đại lý vé số', TRUE, NOW(), NOW());

INSERT INTO website_templates (id, vertical_id, code, name, layout_config, default_colors, active, created_at, updated_at)
VALUES ('00000000-0000-0000-0000-000000000201', '00000000-0000-0000-0000-000000000101',
        'lottery-phuong-nghi', 'Lottery Phương Nghi',
        '{"layoutType":"sidebar-right","headerStyle":"sticky","features":{"liveResult":true,"ticketChecker":true,"statistics":true,"articles":true}}',
        '{"primary":"#c8102e","secondary":"#f5c518","background":"#ffffff","text":"#1f2937"}',
        TRUE, NOW(), NOW());
