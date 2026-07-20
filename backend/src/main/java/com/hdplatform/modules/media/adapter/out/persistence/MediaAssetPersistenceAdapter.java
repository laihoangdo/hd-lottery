package com.hdplatform.modules.media.adapter.out.persistence;

import com.hdplatform.modules.media.application.port.MediaAssetRepository;
import com.hdplatform.modules.media.domain.aggregate.MediaAsset;
import com.hdplatform.modules.media.domain.aggregate.MediaAssetId;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MediaAssetPersistenceAdapter implements MediaAssetRepository {
    private final MediaAssetJpaRepository repository;

    @Override
    public MediaAsset save(MediaAsset asset) {
        return toAggregate(repository.save(toEntity(asset)));
    }

    @Override
    public Optional<MediaAsset> findById(TenantId tenantId, MediaAssetId assetId) {
        return repository.findByTenantIdAndId(tenantId.getValue(), assetId.getValue())
                .map(this::toAggregate);
    }

    private MediaAssetEntity toEntity(MediaAsset asset) {
        MediaAssetEntity entity = new MediaAssetEntity();
        entity.setId(asset.getId().getValue());
        entity.setTenantId(asset.getTenantId().getValue());
        entity.setObjectKey(asset.getObjectKey());
        entity.setOriginalName(asset.getOriginalName());
        entity.setContentType(asset.getContentType());
        entity.setSizeBytes(asset.getSizeBytes());
        entity.setChecksumSha256(asset.getChecksumSha256());
        entity.setStatus(asset.getStatus());
        entity.setVisibility(asset.getVisibility());
        entity.setCreatedAt(asset.getCreatedAt());
        entity.setUpdatedAt(asset.getUpdatedAt());
        return entity;
    }

    private MediaAsset toAggregate(MediaAssetEntity entity) {
        return MediaAsset.restore(MediaAssetId.of(entity.getId()), TenantId.of(entity.getTenantId()),
                entity.getObjectKey(), entity.getOriginalName(), entity.getContentType(),
                entity.getSizeBytes(), entity.getChecksumSha256(), entity.getStatus(),
                entity.getVisibility(),
                entity.getCreatedAt(), entity.getUpdatedAt());
    }
}
