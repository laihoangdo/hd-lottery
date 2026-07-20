package com.hdplatform.modules.cms.domain.aggregate;

import com.hdplatform.shared.domain.identifier.UUIDIdentifier;

import java.util.UUID;

public final class PageId extends UUIDIdentifier {

    private PageId(UUID value) {
        super(value);
    }

    public static PageId newId() {
        return new PageId(UUID.randomUUID());
    }

    public static PageId of(UUID value) {
        return new PageId(value);
    }
}
