package com.hdplatform.modules.media.domain.aggregate;

import com.hdplatform.modules.media.domain.valueobject.MediaStatus;
import com.hdplatform.modules.media.domain.valueobject.MediaVisibility;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import com.hdplatform.shared.domain.AuditableEntity;

import java.time.Instant;
import java.util.Objects;

public final class MediaAsset extends AuditableEntity<MediaAssetId> {
    private final TenantId tenantId;
    private final String objectKey;
    private final String originalName;
    private final String contentType;
    private final long sizeBytes;
    private final String checksumSha256;
    private MediaStatus status;
    private MediaVisibility visibility;

    private MediaAsset(MediaAssetId id, TenantId tenantId, String objectKey,
                       String originalName, String contentType, long sizeBytes,
                       String checksumSha256, MediaStatus status, MediaVisibility visibility) {
        super(id);
        this.tenantId = Objects.requireNonNull(tenantId);
        this.objectKey = requireText(objectKey, "objectKey", 500);
        this.originalName = requireText(originalName, "originalName", 255);
        this.contentType = requireText(contentType, "contentType", 100);
        if (sizeBytes <= 0) throw new IllegalArgumentException("Media size must be positive");
        this.sizeBytes = sizeBytes;
        this.checksumSha256 = requireText(checksumSha256, "checksumSha256", 64);
        this.status = Objects.requireNonNull(status);
        this.visibility = Objects.requireNonNull(visibility);
    }

    public static MediaAsset ready(MediaAssetId id, TenantId tenantId, String objectKey,
                                   String originalName, String contentType, long sizeBytes,
                                   String checksumSha256, Instant now) {
        MediaAsset asset = new MediaAsset(id, tenantId, objectKey, originalName,
                contentType, sizeBytes, checksumSha256,
                MediaStatus.READY, MediaVisibility.PRIVATE);
        asset.markCreated(Objects.requireNonNull(now));
        return asset;
    }

    public static MediaAsset restore(MediaAssetId id, TenantId tenantId, String objectKey,
                                     String originalName, String contentType, long sizeBytes,
                                     String checksumSha256, MediaStatus status,
                                     MediaVisibility visibility,
                                     Instant createdAt, Instant updatedAt) {
        MediaAsset asset = new MediaAsset(id, tenantId, objectKey, originalName,
                contentType, sizeBytes, checksumSha256, status, visibility);
        asset.createdAt = createdAt;
        asset.updatedAt = updatedAt;
        return asset;
    }

    public void markDeleted(Instant now) {
        status = MediaStatus.DELETED;
        visibility = MediaVisibility.PRIVATE;
        markUpdated(Objects.requireNonNull(now));
    }

    public void makePublic(Instant now) {
        if (status != MediaStatus.READY) {
            throw new IllegalStateException("Only ready media can be public");
        }
        visibility = MediaVisibility.PUBLIC;
        markUpdated(Objects.requireNonNull(now));
    }

    public void makePrivate(Instant now) {
        visibility = MediaVisibility.PRIVATE;
        markUpdated(Objects.requireNonNull(now));
    }

    private static String requireText(String value, String field, int max) {
        if (value == null || value.isBlank() || value.length() > max) {
            throw new IllegalArgumentException(field + " is invalid");
        }
        return value.trim();
    }

    public TenantId getTenantId() { return tenantId; }
    public String getObjectKey() { return objectKey; }
    public String getOriginalName() { return originalName; }
    public String getContentType() { return contentType; }
    public long getSizeBytes() { return sizeBytes; }
    public String getChecksumSha256() { return checksumSha256; }
    public MediaStatus getStatus() { return status; }
    public MediaVisibility getVisibility() { return visibility; }
}
