package com.hdplatform.modules.platformcatalog.domain;

import com.hdplatform.shared.domain.identifier.UUIDIdentifier;

import java.util.UUID;

public final class VerticalId extends UUIDIdentifier {
    private VerticalId(UUID value) { super(value); }
    public static VerticalId of(UUID value) { return new VerticalId(value); }
    public static VerticalId newId() { return new VerticalId(UUID.randomUUID()); }
}
