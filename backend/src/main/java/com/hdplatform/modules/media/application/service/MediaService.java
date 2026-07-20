package com.hdplatform.modules.media.application.service;

import com.hdplatform.modules.media.application.port.MediaAssetRepository;
import com.hdplatform.modules.media.application.port.ObjectStorage;
import com.hdplatform.modules.media.domain.aggregate.MediaAsset;
import com.hdplatform.modules.media.domain.aggregate.MediaAssetId;
import com.hdplatform.modules.media.domain.valueobject.MediaStatus;
import com.hdplatform.modules.media.domain.valueobject.MediaVisibility;
import com.hdplatform.modules.tenant.application.context.TenantContext;
import com.hdplatform.modules.tenant.application.context.TenantContextHolder;
import com.hdplatform.shared.domain.ClockProvider;
import com.hdplatform.shared.exception.ValidationException;
import com.hdplatform.shared.exception.NotFoundException;
import com.hdplatform.modules.reporting.application.PlatformMetricKeys;
import com.hdplatform.modules.reporting.application.service.TenantMetrics;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class MediaService {
    private final MediaAssetRepository repository;
    private final ObjectStorage storage;
    private final MediaPolicy policy;
    private final ClockProvider clock;
    private final TenantMetrics tenantMetrics;

    @Transactional
    public MediaAsset upload(String originalName, String contentType,
                             long declaredSize, InputStream content) throws IOException {
        validate(contentType, declaredSize);
        TenantContext tenant = TenantContextHolder.requireCurrent();
        MediaAssetId assetId = MediaAssetId.newId();
        ObjectStorage.StoredObject stored = storage.store(new ObjectStorage.StorageUpload(
                tenant.tenantId(), assetId, contentType, declaredSize, content));
        try {
            MediaAsset saved = repository.save(MediaAsset.ready(
                    assetId, tenant.tenantId(), stored.objectKey(), originalName,
                    contentType, stored.sizeBytes(), stored.checksumSha256(), clock.now()));
            tenantMetrics.increment(tenant.tenantId(), PlatformMetricKeys.MEDIA_ASSET_TOTAL, 1);
            tenantMetrics.increment(tenant.tenantId(), PlatformMetricKeys.MEDIA_ASSET_PRIVATE, 1);
            return saved;
        } catch (RuntimeException exception) {
            try { storage.delete(stored.objectKey()); }
            catch (IOException cleanupFailure) { exception.addSuppressed(cleanupFailure); }
            throw exception;
        }
    }

    @Transactional
    public MediaAsset publish(MediaAssetId assetId) {
        MediaAsset asset = currentTenantAsset(assetId);
        boolean wasPublic = asset.getVisibility() == MediaVisibility.PUBLIC;
        asset.makePublic(clock.now());
        MediaAsset saved = repository.save(asset);
        if (!wasPublic) {
            tenantMetrics.increment(asset.getTenantId(), PlatformMetricKeys.MEDIA_ASSET_PRIVATE, -1);
            tenantMetrics.increment(asset.getTenantId(), PlatformMetricKeys.MEDIA_ASSET_PUBLIC, 1);
        }
        return saved;
    }

    @Transactional(readOnly = true)
    public MediaDownload downloadPublic(MediaAssetId assetId) throws IOException {
        MediaAsset asset = currentTenantAsset(assetId);
        if (asset.getStatus() != MediaStatus.READY
                || asset.getVisibility() != MediaVisibility.PUBLIC) {
            throw notFound();
        }
        ObjectStorage.StoredContent stored = storage.load(asset.getObjectKey());
        if (stored.sizeBytes() != asset.getSizeBytes()) {
            stored.content().close();
            throw new IOException("Stored media size does not match metadata");
        }
        return new MediaDownload(asset.getOriginalName(), asset.getContentType(),
                asset.getSizeBytes(), asset.getChecksumSha256(), stored.content());
    }

    private MediaAsset currentTenantAsset(MediaAssetId assetId) {
        TenantContext tenant = TenantContextHolder.requireCurrent();
        return repository.findById(tenant.tenantId(), assetId).orElseThrow(this::notFound);
    }

    private NotFoundException notFound() {
        return new NotFoundException("MEDIA_NOT_FOUND", "Media not found");
    }

    private void validate(String contentType, long size) {
        if (contentType == null || !policy.allowedContentTypes().contains(contentType)) {
            throw new ValidationException("MEDIA_TYPE_NOT_ALLOWED", "Media type is not allowed");
        }
        if (size <= 0 || size > policy.maxSizeBytes()) {
            throw new ValidationException("MEDIA_SIZE_INVALID", "Media size is invalid");
        }
    }
}
