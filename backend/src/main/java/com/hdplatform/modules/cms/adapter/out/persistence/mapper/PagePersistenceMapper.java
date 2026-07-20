package com.hdplatform.modules.cms.adapter.out.persistence.mapper;

import com.hdplatform.modules.cms.adapter.out.persistence.entity.PageEntity;
import com.hdplatform.modules.cms.domain.aggregate.Page;
import com.hdplatform.modules.cms.domain.aggregate.PageId;
import com.hdplatform.modules.cms.domain.valueobject.PageSlug;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import org.springframework.stereotype.Component;

@Component
public class PagePersistenceMapper {
    public PageEntity toEntity(Page page) {
        PageEntity entity = new PageEntity();
        entity.setId(page.getId().getValue());
        entity.setTenantId(page.getTenantId().getValue());
        entity.setSlug(page.getSlug().value());
        entity.setTitle(page.getTitle());
        entity.setContent(page.getContent());
        entity.setStatus(page.getStatus());
        entity.setPublishedAt(page.getPublishedAt());
        entity.setCreatedAt(page.getCreatedAt());
        entity.setUpdatedAt(page.getUpdatedAt());
        return entity;
    }

    public Page toAggregate(PageEntity entity) {
        return Page.restore(
                PageId.of(entity.getId()), TenantId.of(entity.getTenantId()),
                PageSlug.of(entity.getSlug()), entity.getTitle(), entity.getContent(),
                entity.getStatus(), entity.getPublishedAt(),
                entity.getCreatedAt(), entity.getUpdatedAt());
    }
}
