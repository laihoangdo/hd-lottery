package com.hdplatform.modules.cms.application.port;

import com.hdplatform.modules.cms.domain.aggregate.Page;
import com.hdplatform.modules.cms.domain.aggregate.PageId;
import com.hdplatform.modules.cms.domain.valueobject.PageSlug;
import com.hdplatform.modules.cms.domain.valueobject.PageStatus;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;

import java.util.Optional;

public interface PageRepository {
    Page save(Page page);
    Optional<Page> findById(TenantId tenantId, PageId pageId);
    Optional<Page> findBySlug(TenantId tenantId, PageSlug slug);
    Optional<Page> findBySlugAndStatus(TenantId tenantId, PageSlug slug, PageStatus status);
    boolean existsBySlug(TenantId tenantId, PageSlug slug);
}
