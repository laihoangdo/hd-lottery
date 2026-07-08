package com.hdplatform.modules.tenant.adapter.out.persistence.mapper;

import java.net.URI;

import org.springframework.stereotype.Component;

import com.hdplatform.modules.tenant.adapter.out.persistence.entity.TenantEntity;
import com.hdplatform.modules.tenant.domain.aggregate.Tenant;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import com.hdplatform.modules.tenant.domain.valueobject.DisplayName;
import com.hdplatform.modules.tenant.domain.valueobject.DomainName;
import com.hdplatform.modules.tenant.domain.valueobject.Hotline;
import com.hdplatform.modules.tenant.domain.valueobject.LogoUrl;
import com.hdplatform.modules.tenant.domain.valueobject.SiteKey;
import com.hdplatform.modules.tenant.domain.valueobject.TenantCode;
import com.hdplatform.modules.tenant.domain.valueobject.TenantName;

/**
 * Maps between Domain Aggregate and Persistence Entity.
 *
 * NOTE:
 * This mapper will be completed after Aggregate refactoring
 * because Tenant currently does not expose a rehydrate factory.
 */
@Component
public class TenantPersistenceMapper {

    // TenantEntity toEntity(Tenant tenant);

    // Tenant toAggregate(TenantEntity entity);
    public TenantEntity toEntity(Tenant aggregate) {

        TenantEntity entity = new TenantEntity();

        entity.setId(aggregate.getId().getValue());
        entity.setSiteKey(aggregate.getSiteKey().value());
        entity.setDomainName(aggregate.getDomainName().value());
        entity.setDisplayName(aggregate.getDisplayName().value());
        entity.setName(aggregate.getName().value());
        entity.setCode(aggregate.getCode().value());

        entity.setLogoUrl(
                aggregate.getLogoUrl() == null
                        ? null
                        : aggregate.getLogoUrl().toString());

        entity.setHotline(
                aggregate.getHotline() == null
                        ? null
                        : aggregate.getHotline().value());

        entity.setStatus(aggregate.getStatus());

        entity.setCreatedAt(aggregate.getCreatedAt());
        entity.setUpdatedAt(aggregate.getUpdatedAt());

        return entity;
    }

    public Tenant toAggregate(TenantEntity entity) {

        return Tenant.restore(
            TenantId.of(entity.getId()),
            SiteKey.of(entity.getSiteKey()),
            DomainName.of(entity.getDomainName()),
            DisplayName.of(entity.getDisplayName()),
            TenantName.of(entity.getName()),
            TenantCode.of(entity.getCode()),
            entity.getLogoUrl() == null
                    ? null
                    : LogoUrl.of(entity.getLogoUrl()),
            entity.getHotline() == null
                    ? null
                    : Hotline.of(entity.getHotline()),
            // null,
            // null,
            entity.getStatus(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

}