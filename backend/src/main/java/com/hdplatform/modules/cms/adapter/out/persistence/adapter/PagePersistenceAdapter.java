package com.hdplatform.modules.cms.adapter.out.persistence.adapter;

import com.hdplatform.modules.cms.adapter.out.persistence.mapper.PagePersistenceMapper;
import com.hdplatform.modules.cms.adapter.out.persistence.repository.PageJpaRepository;
import com.hdplatform.modules.cms.application.port.PageRepository;
import com.hdplatform.modules.cms.domain.aggregate.Page;
import com.hdplatform.modules.cms.domain.aggregate.PageId;
import com.hdplatform.modules.cms.domain.valueobject.PageSlug;
import com.hdplatform.modules.cms.domain.valueobject.PageStatus;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PagePersistenceAdapter implements PageRepository {
    private final PageJpaRepository repository;
    private final PagePersistenceMapper mapper;

    @Override
    public Page save(Page page) {
        return mapper.toAggregate(repository.save(mapper.toEntity(page)));
    }

    @Override
    public Optional<Page> findById(TenantId tenantId, PageId pageId) {
        return repository.findByTenantIdAndId(tenantId.getValue(), pageId.getValue())
                .map(mapper::toAggregate);
    }

    @Override
    public Optional<Page> findBySlug(TenantId tenantId, PageSlug slug) {
        return repository.findByTenantIdAndSlug(tenantId.getValue(), slug.value())
                .map(mapper::toAggregate);
    }

    @Override
    public Optional<Page> findBySlugAndStatus(
            TenantId tenantId, PageSlug slug, PageStatus status) {
        return repository.findByTenantIdAndSlugAndStatus(
                        tenantId.getValue(), slug.value(), status)
                .map(mapper::toAggregate);
    }

    @Override
    public boolean existsBySlug(TenantId tenantId, PageSlug slug) {
        return repository.existsByTenantIdAndSlug(tenantId.getValue(), slug.value());
    }
}
