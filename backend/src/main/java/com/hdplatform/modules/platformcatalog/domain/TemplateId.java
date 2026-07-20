package com.hdplatform.modules.platformcatalog.domain;

import com.hdplatform.shared.domain.identifier.UUIDIdentifier;

import java.util.UUID;

public final class TemplateId extends UUIDIdentifier {
    private TemplateId(UUID value) { super(value); }
    public static TemplateId of(UUID value) { return new TemplateId(value); }
    public static TemplateId newId() { return new TemplateId(UUID.randomUUID()); }
}
