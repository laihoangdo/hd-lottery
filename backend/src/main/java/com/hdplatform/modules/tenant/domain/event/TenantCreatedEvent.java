package com.hdplatform.modules.tenant.domain.event;

import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import com.hdplatform.shared.domain.DomainEvent;

import java.time.Instant;

public record TenantCreatedEvent(
        TenantId tenantId,
        Instant occurredOn
) implements DomainEvent {

    @Override
    public Instant occurredOn() {
        return occurredOn;
    }
}