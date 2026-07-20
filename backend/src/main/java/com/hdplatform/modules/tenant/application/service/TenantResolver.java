package com.hdplatform.modules.tenant.application.service;

import com.hdplatform.modules.tenant.application.context.TenantContext;
import com.hdplatform.modules.tenant.application.port.TenantRepository;
import com.hdplatform.modules.tenant.domain.aggregate.Tenant;
import com.hdplatform.modules.tenant.domain.valueobject.DomainName;
import com.hdplatform.modules.tenant.domain.valueobject.SiteKey;
import com.hdplatform.modules.tenant.domain.valueobject.TenantStatus;
import com.hdplatform.shared.domain.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TenantResolver {

    private final TenantRepository tenantRepository;

    public Optional<TenantContext> resolveByDomain(String domain) {
        if (domain == null || domain.isBlank()) {
            return Optional.empty();
        }
        try {
            return activeContext(tenantRepository.findByDomainName(DomainName.of(domain)));
        } catch (DomainException exception) {
            // localhost and IP-based operational endpoints are not tenant domains.
            return Optional.empty();
        }
    }

    public Optional<TenantContext> resolveBySiteKey(String siteKey) {
        return activeContext(tenantRepository.findBySiteKey(SiteKey.of(siteKey)));
    }

    private Optional<TenantContext> activeContext(Optional<Tenant> candidate) {
        return candidate
                .filter(tenant -> tenant.getStatus() == TenantStatus.ACTIVE)
                .map(tenant -> new TenantContext(tenant.getId(), tenant.getSiteKey()));
    }
}
