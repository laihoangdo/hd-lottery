package com.hdplatform.modules.tenant.application.port;

import com.hdplatform.modules.tenant.domain.aggregate.Tenant;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import com.hdplatform.modules.tenant.domain.valueobject.TenantCode;
import com.hdplatform.modules.tenant.domain.valueobject.DomainName;
import com.hdplatform.modules.tenant.domain.valueobject.SiteKey;

import java.util.List;
import java.util.Optional;


public interface TenantRepository {

    Tenant save(Tenant tenant);

    Optional<Tenant> findById(TenantId id);

    Optional<Tenant> findByCode(TenantCode code);

    Optional<Tenant> findByDomainName(DomainName domainName);

    Optional<Tenant> findBySiteKey(SiteKey siteKey);

    boolean existsByCode(TenantCode code);

    boolean existsBySiteKey(SiteKey siteKey);

    boolean existsByDomainName(DomainName domainName);
    
    List<Tenant> findAll();

}
