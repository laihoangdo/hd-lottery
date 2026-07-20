ALTER TABLE website_templates
    ADD CONSTRAINT uk_website_templates_id_vertical UNIQUE (id, vertical_id);

ALTER TABLE tenants ADD COLUMN vertical_id UUID;
ALTER TABLE tenants ADD COLUMN template_id UUID;

UPDATE tenants
SET vertical_id = '00000000-0000-0000-0000-000000000101',
    template_id = '00000000-0000-0000-0000-000000000201'
WHERE vertical_id IS NULL OR template_id IS NULL;

ALTER TABLE tenants ALTER COLUMN vertical_id SET NOT NULL;
ALTER TABLE tenants ALTER COLUMN template_id SET NOT NULL;

ALTER TABLE tenants
    ADD CONSTRAINT fk_tenants_vertical
        FOREIGN KEY (vertical_id) REFERENCES verticals(id),
    ADD CONSTRAINT fk_tenants_template_same_vertical
        FOREIGN KEY (template_id, vertical_id)
        REFERENCES website_templates(id, vertical_id);

CREATE INDEX idx_tenants_vertical_template_status
    ON tenants(vertical_id, template_id, status);
