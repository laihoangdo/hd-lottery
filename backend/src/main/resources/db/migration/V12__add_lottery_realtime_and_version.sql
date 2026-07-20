ALTER TABLE lottery_results
    DROP CONSTRAINT ck_lottery_results_status;

ALTER TABLE lottery_results
    ADD CONSTRAINT ck_lottery_results_status
        CHECK (status IN ('DRAFT', 'DRAWING', 'PUBLISHED'));

ALTER TABLE lottery_results
    ADD COLUMN version BIGINT NOT NULL DEFAULT 0;

ALTER TABLE lottery_results
    ALTER COLUMN version DROP DEFAULT;

CREATE INDEX idx_lottery_results_live_lookup
    ON lottery_results (draw_date DESC, region, province_name)
    WHERE status IN ('DRAWING', 'PUBLISHED');
