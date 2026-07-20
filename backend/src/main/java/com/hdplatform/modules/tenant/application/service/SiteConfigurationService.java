package com.hdplatform.modules.tenant.application.service;

import com.hdplatform.modules.platformcatalog.application.PlatformCatalogRepository;
import com.hdplatform.modules.tenant.application.port.TenantRepository;
import com.hdplatform.modules.tenant.application.query.SiteConfiguration;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import com.hdplatform.shared.domain.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SiteConfigurationService {
    private final TenantRepository tenants;
    private final PlatformCatalogRepository catalog;

    @Transactional(readOnly = true)
    public SiteConfiguration resolve(TenantId tenantId) {
        var tenant = tenants.findById(tenantId).orElseThrow(() -> new DomainException("Tenant not found"));
        var vertical = catalog.findVertical(tenant.getVerticalId())
                .orElseThrow(() -> new DomainException("Tenant vertical configuration is missing"));
        var template = catalog.findTemplate(tenant.getTemplateId())
                .orElseThrow(() -> new DomainException("Tenant template configuration is missing"));
        if (!template.getVerticalId().equals(vertical.getId())) {
            throw new DomainException("Tenant template does not belong to its vertical");
        }
        return new SiteConfiguration(
                tenant.getId().getValue(), tenant.getSiteKey().value(), tenant.getDomainName().value(),
                tenant.getDisplayName().value(), tenant.getLogoUrl() == null ? null : tenant.getLogoUrl().value().toString(),
                tenant.getHotline() == null ? null : tenant.getHotline().value(), vertical.getCode().value(),
                template.getCode().value(), template.getLayoutConfig(), template.getDefaultColors());
    }
}
