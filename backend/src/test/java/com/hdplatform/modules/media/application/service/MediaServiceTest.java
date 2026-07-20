package com.hdplatform.modules.media.application.service;

import com.hdplatform.modules.media.application.port.MediaAssetRepository;
import com.hdplatform.modules.media.application.port.ObjectStorage;
import com.hdplatform.modules.media.domain.aggregate.MediaAsset;
import com.hdplatform.modules.media.domain.aggregate.MediaAssetId;
import com.hdplatform.modules.media.domain.valueobject.MediaVisibility;
import com.hdplatform.modules.tenant.application.context.TenantContext;
import com.hdplatform.modules.tenant.application.context.TenantContextHolder;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import com.hdplatform.modules.tenant.domain.valueobject.SiteKey;
import com.hdplatform.shared.domain.ClockProvider;
import com.hdplatform.shared.exception.ValidationException;
import com.hdplatform.modules.reporting.application.service.TenantMetrics;
import com.hdplatform.modules.reporting.application.PlatformMetricKeys;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Set;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

class MediaServiceTest {
    private final MediaAssetRepository repository = mock(MediaAssetRepository.class);
    private final ObjectStorage storage = mock(ObjectStorage.class);
    private final ClockProvider clock = () -> Instant.parse("2026-07-20T00:00:00Z");
    private final TenantMetrics tenantMetrics = mock(TenantMetrics.class);
    private final MediaService service = new MediaService(repository, storage,
            new MediaPolicy(100, Set.of("image/png")), clock, tenantMetrics);

    @AfterEach void clearTenant() { TenantContextHolder.clear(); }

    @Test
    void creates_asset_owned_by_current_tenant() throws Exception {
        TenantId tenantId = TenantId.newId();
        TenantContextHolder.set(new TenantContext(tenantId, SiteKey.of("dealer-one")));
        when(storage.store(any())).thenReturn(new ObjectStorage.StoredObject(
                tenantId.getValue() + "/asset.png", 4, "a".repeat(64)));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        MediaAsset asset = service.upload("logo.png", "image/png", 4,
                new ByteArrayInputStream(new byte[]{1, 2, 3, 4}));

        assertThat(asset.getTenantId()).isEqualTo(tenantId);
        assertThat(asset.getVisibility()).isEqualTo(MediaVisibility.PRIVATE);
        verify(repository).save(any(MediaAsset.class));
        verify(tenantMetrics).increment(tenantId, PlatformMetricKeys.MEDIA_ASSET_TOTAL, 1);
        verify(tenantMetrics).increment(tenantId, PlatformMetricKeys.MEDIA_ASSET_PRIVATE, 1);
    }

    @Test
    void rejects_type_and_size_before_writing_storage() {
        assertThatThrownBy(() -> service.upload("script.svg", "image/svg+xml", 4,
                new ByteArrayInputStream(new byte[4])))
                .isInstanceOf(ValidationException.class);
        assertThatThrownBy(() -> service.upload("huge.png", "image/png", 101,
                new ByteArrayInputStream(new byte[0])))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void removes_stored_object_when_metadata_persistence_fails() throws Exception {
        TenantId tenantId = TenantId.newId();
        TenantContextHolder.set(new TenantContext(tenantId, SiteKey.of("dealer-one")));
        String objectKey = tenantId.getValue() + "/asset.png";
        when(storage.store(any())).thenReturn(new ObjectStorage.StoredObject(
                objectKey, 4, "a".repeat(64)));
        when(repository.save(any())).thenThrow(new IllegalStateException("database unavailable"));

        assertThatThrownBy(() -> service.upload("logo.png", "image/png", 4,
                new ByteArrayInputStream(new byte[4])))
                .isInstanceOf(IllegalStateException.class);

        verify(storage).delete(objectKey);
    }

    @Test
    void public_download_requires_explicitly_published_asset_of_current_tenant() throws Exception {
        TenantId tenantId = TenantId.newId();
        TenantContextHolder.set(new TenantContext(tenantId, SiteKey.of("dealer-one")));
        MediaAsset asset = readyAsset(tenantId);
        asset.makePublic(clock.now());
        when(repository.findById(tenantId, asset.getId())).thenReturn(Optional.of(asset));
        byte[] bytes = new byte[]{1, 2, 3, 4};
        when(storage.load(asset.getObjectKey())).thenReturn(
                new ObjectStorage.StoredContent(new ByteArrayInputStream(bytes), bytes.length));

        MediaDownload download = service.downloadPublic(asset.getId());

        assertThat(download.content().readAllBytes()).isEqualTo(bytes);
        verify(repository).findById(tenantId, asset.getId());
    }

    @Test
    void private_asset_is_not_exposed_by_public_download() {
        TenantId tenantId = TenantId.newId();
        TenantContextHolder.set(new TenantContext(tenantId, SiteKey.of("dealer-one")));
        MediaAsset asset = readyAsset(tenantId);
        when(repository.findById(tenantId, asset.getId())).thenReturn(Optional.of(asset));

        assertThatThrownBy(() -> service.downloadPublic(asset.getId()))
                .isInstanceOf(com.hdplatform.shared.exception.NotFoundException.class);
        try {
            verify(storage, never()).load(asset.getObjectKey());
        } catch (IOException exception) {
            throw new AssertionError(exception);
        }
    }

    private MediaAsset readyAsset(TenantId tenantId) {
        return MediaAsset.ready(MediaAssetId.newId(), tenantId,
                tenantId.getValue() + "/asset.png", "logo.png", "image/png", 4,
                "a".repeat(64), clock.now());
    }
}
