package com.hdplatform.shared.domain;

import java.time.Instant;

public interface DomainEvent {

    Instant occurredOn();

}