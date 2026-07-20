package com.hdplatform.modules.cms.adapter.out.persistence.repository;

import com.hdplatform.modules.cms.adapter.out.persistence.entity.PageEntity;
import com.hdplatform.modules.cms.domain.valueobject.PageStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PageJpaRepository extends JpaRepository<PageEntity, UUID> {
    Optional<PageEntity> findByTenantIdAndId(UUID tenantId, UUID id);
    Optional<PageEntity> findByTenantIdAndSlug(UUID tenantId, String slug);
    Optional<PageEntity> findByTenantIdAndSlugAndStatus(UUID tenantId, String slug, PageStatus status);
    boolean existsByTenantIdAndSlug(UUID tenantId, String slug);
}
