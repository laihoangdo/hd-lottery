CREATE TABLE notification_outbox
(
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    channel VARCHAR(20) NOT NULL,
    recipient VARCHAR(320) NOT NULL,
    template_key VARCHAR(100) NOT NULL,
    payload_json JSONB NOT NULL,
    status VARCHAR(20) NOT NULL,
    attempts INTEGER NOT NULL DEFAULT 0,
    next_attempt_at TIMESTAMP,
    locked_at TIMESTAMP,
    sent_at TIMESTAMP,
    last_error VARCHAR(1000),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_notification_outbox_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id),
    CONSTRAINT ck_notification_outbox_channel
        CHECK (channel IN ('EMAIL', 'SMS', 'PUSH', 'ZALO', 'IN_APP')),
    CONSTRAINT ck_notification_outbox_status
        CHECK (status IN ('PENDING', 'PROCESSING', 'SENT', 'FAILED')),
    CONSTRAINT ck_notification_outbox_attempts CHECK (attempts >= 0)
);

CREATE INDEX idx_notification_outbox_claim
    ON notification_outbox (next_attempt_at, created_at)
    WHERE status = 'PENDING';

CREATE INDEX idx_notification_outbox_stale_lock
    ON notification_outbox (locked_at)
    WHERE status = 'PROCESSING';

CREATE INDEX idx_notification_outbox_tenant_created
    ON notification_outbox (tenant_id, created_at DESC);
