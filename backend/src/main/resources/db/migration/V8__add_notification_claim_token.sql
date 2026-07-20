ALTER TABLE notification_outbox
    ADD COLUMN claim_token UUID;

CREATE UNIQUE INDEX uk_notification_outbox_claim_token
    ON notification_outbox (claim_token)
    WHERE claim_token IS NOT NULL;
