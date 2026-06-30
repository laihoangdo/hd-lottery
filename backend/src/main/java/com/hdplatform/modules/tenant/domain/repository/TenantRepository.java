package com.hdplatform.modules.tenant.domain.repository;

import com.hdplatform.modules.tenant.domain.aggregate.Tenant;
import com.hdplatform.modules.tenant.domain.valueobject.DomainName;
import com.hdplatform.modules.tenant.domain.valueobject.SiteKey;
import com.hdplatform.modules.tenant.domain.valueobject.TenantId;

import java.util.List;
import java.util.Optional;

/**
 * Domain Repository.
 *
 * Infrastructure layer will implement this interface.
 */
public interface TenantRepository {

    Tenant save(Tenant tenant);

    Optional<Tenant> findById(TenantId id);

    Optional<Tenant> findBySiteKey(SiteKey siteKey);

    Optional<Tenant> findByDomainName(DomainName domainName);

    boolean existsBySiteKey(SiteKey siteKey);

    boolean existsByDomainName(DomainName domainName);

    List<Tenant> findAll();

}