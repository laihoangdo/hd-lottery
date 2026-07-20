ALTER TABLE media_assets
    ADD COLUMN visibility VARCHAR(20) NOT NULL DEFAULT 'PRIVATE';

ALTER TABLE media_assets
    ADD CONSTRAINT ck_media_assets_visibility
        CHECK (visibility IN ('PRIVATE', 'PUBLIC'));

ALTER TABLE media_assets
    ALTER COLUMN visibility DROP DEFAULT;

CREATE INDEX idx_media_assets_public_lookup
    ON media_assets (tenant_id, id)
    WHERE status = 'READY' AND visibility = 'PUBLIC';
