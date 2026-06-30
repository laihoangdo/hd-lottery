package com.hdplatform.modules.tenant.adapter.out.persistence.adapter;

import com.hdplatform.modules.tenant.domain.aggregate.Tenant;
import com.hdplatform.modules.tenant.domain.repository.TenantRepository;
import com.hdplatform.modules.tenant.domain.valueobject.DomainName;
import com.hdplatform.modules.tenant.domain.valueobject.SiteKey;
import com.hdplatform.modules.tenant.domain.valueobject.TenantId;
import org.springframework.stereotype.Repository;

import java.util.List;
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
public class TenantPersistenceAdapter implements TenantRepository {

    @Override
    public Tenant save(Tenant tenant) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public Optional<Tenant> findById(TenantId id) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public Optional<Tenant> findBySiteKey(SiteKey siteKey) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public Optional<Tenant> findByDomainName(DomainName domainName) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public boolean existsBySiteKey(SiteKey siteKey) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public boolean existsByDomainName(DomainName domainName) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public List<Tenant> findAll() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}