package com.hdplatform.modules.tenant.adapter.out.persistence.mapper;

import com.hdplatform.modules.tenant.adapter.out.persistence.entity.TenantEntity;
import com.hdplatform.modules.tenant.domain.aggregate.Tenant;

/**
 * Maps between Domain Aggregate and Persistence Entity.
 *
 * NOTE:
 * This mapper will be completed after Aggregate refactoring
 * because Tenant currently does not expose a rehydrate factory.
 */
public interface TenantPersistenceMapper {

    TenantEntity toEntity(Tenant tenant);

    Tenant toDomain(TenantEntity entity);

}