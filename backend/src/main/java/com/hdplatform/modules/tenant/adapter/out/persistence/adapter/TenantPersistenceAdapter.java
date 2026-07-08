package com.hdplatform.modules.tenant.adapter.out.persistence.adapter;

import com.hdplatform.modules.tenant.adapter.out.persistence.entity.TenantEntity;
import com.hdplatform.modules.tenant.adapter.out.persistence.mapper.TenantPersistenceMapper;
import com.hdplatform.modules.tenant.adapter.out.persistence.repository.TenantJpaRepository;
import com.hdplatform.modules.tenant.application.port.TenantRepository;
import com.hdplatform.modules.tenant.domain.aggregate.Tenant;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import com.hdplatform.modules.tenant.domain.valueobject.TenantCode;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Persistence adapter.
 *
 * Temporary implementation.
 *
 * The implementation will be completed after
 * Tenant Aggregate supports rehydration.
 */
@Repository
@RequiredArgsConstructor
public class TenantPersistenceAdapter implements TenantRepository {


    private final TenantJpaRepository repository;

    private final TenantPersistenceMapper mapper;

    @Override
    public Tenant save(Tenant tenant) {
        System.out.println("tenant"+ tenant);
        TenantEntity entity =
                mapper.toEntity(tenant);

        return mapper.toAggregate(repository.save(entity));
    }

    @Override
    public Optional<Tenant> findById(TenantId id) {

        return repository.findById(id.getValue())
                .map(mapper::toAggregate);

    }

    @Override
    public Optional<Tenant> findByCode(TenantCode code) {

        return repository.findByCode(code.value())
                .map(mapper::toAggregate);

    }

    @Override
    public boolean existsByCode(TenantCode code) {

        return repository.existsByCode(code.value());

    }
}