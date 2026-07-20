package com.hdplatform.modules.media.adapter.in.rest;

import com.hdplatform.modules.media.domain.aggregate.MediaAsset;
import com.hdplatform.modules.media.domain.valueobject.MediaStatus;
import com.hdplatform.modules.media.domain.valueobject.MediaVisibility;

import java.time.Instant;
import java.util.UUID;

public record MediaAssetResponse(UUID id, String originalName, String contentType,
                                 long sizeBytes, String checksumSha256,
                                 MediaStatus status, MediaVisibility visibility, Instant createdAt) {
    static MediaAssetResponse from(MediaAsset asset) {
        return new MediaAssetResponse(asset.getId().getValue(), asset.getOriginalName(),
                asset.getContentType(), asset.getSizeBytes(), asset.getChecksumSha256(),
                asset.getStatus(), asset.getVisibility(), asset.getCreatedAt());
    }
}
