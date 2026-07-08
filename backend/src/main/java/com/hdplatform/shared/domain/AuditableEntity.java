package com.hdplatform.shared.domain;

import java.time.Instant;

public abstract class AuditableEntity<ID extends Identifier<?>>
        extends AggregateRoot<ID> {

    protected Instant createdAt;

    protected Instant updatedAt;

    protected AuditableEntity() {
    }

    protected AuditableEntity(ID id) {
        super(id);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    protected void markCreated(Instant now) {
        this.createdAt = now;
        this.updatedAt = now;
    }

    protected void markUpdated(Instant now) {
        this.updatedAt = now;
    }

}