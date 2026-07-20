package com.hdplatform.modules.media.application.port;

import com.hdplatform.modules.media.domain.aggregate.MediaAsset;
import com.hdplatform.modules.media.domain.aggregate.MediaAssetId;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;

import java.util.Optional;

public interface MediaAssetRepository {
    MediaAsset save(MediaAsset asset);
    Optional<MediaAsset> findById(TenantId tenantId, MediaAssetId assetId);
}
