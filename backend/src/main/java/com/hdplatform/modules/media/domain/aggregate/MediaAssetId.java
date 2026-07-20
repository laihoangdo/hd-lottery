package com.hdplatform.modules.media.domain.aggregate;

import com.hdplatform.shared.domain.identifier.UUIDIdentifier;

import java.util.UUID;

public final class MediaAssetId extends UUIDIdentifier {
    private MediaAssetId(UUID value) { super(value); }
    public static MediaAssetId newId() { return new MediaAssetId(UUID.randomUUID()); }
    public static MediaAssetId of(UUID value) { return new MediaAssetId(value); }
}
