package com.hdplatform.modules.tenant.application.port;

import com.hdplatform.modules.tenant.domain.aggregate.Tenant;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import com.hdplatform.modules.tenant.domain.valueobject.TenantCode;

import java.util.List;
import java.util.Optional;


public interface TenantRepository {

    Tenant save(Tenant tenant);

    Optional<Tenant> findById(TenantId id);

    Optional<Tenant> findByCode(TenantCode code);

    boolean existsByCode(TenantCode code);
    
    List<Tenant> findAll();

}