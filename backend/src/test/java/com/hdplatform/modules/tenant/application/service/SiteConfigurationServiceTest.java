package com.hdplatform.modules.tenant.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdplatform.modules.platformcatalog.application.PlatformCatalogRepository;
import com.hdplatform.modules.platformcatalog.domain.*;
import com.hdplatform.modules.tenant.application.port.TenantRepository;
import com.hdplatform.modules.tenant.domain.aggregate.Tenant;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import com.hdplatform.modules.tenant.domain.valueobject.*;
import com.hdplatform.shared.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SiteConfigurationServiceTest {
    private final TenantRepository tenants = mock(TenantRepository.class);
    private final PlatformCatalogRepository catalog = mock(PlatformCatalogRepository.class);
    private final SiteConfigurationService service = new SiteConfigurationService(tenants, catalog);
    private final Instant now = Instant.parse("2026-07-20T00:00:00Z");

    @Test
    void returnsResolvedTemplateConfigurationForTheCurrentWebsite() {
        VerticalId verticalId = VerticalId.newId();
        TemplateId templateId = TemplateId.newId();
        Tenant tenant = tenant(verticalId, templateId);
        var vertical = Vertical.create(verticalId, CatalogCode.of("lottery"), "Xổ số", null, now);
        var layout = new ObjectMapper().createObjectNode().put("layoutType", "sidebar-right");
        var template = WebsiteTemplate.create(templateId, verticalId, CatalogCode.of("lottery-phuong-nghi"),
                "Phương Nghi", layout, new ObjectMapper().createObjectNode().put("primary", "#c8102e"), now);
        when(tenants.findById(tenant.getId())).thenReturn(Optional.of(tenant));
        when(catalog.findVertical(verticalId)).thenReturn(Optional.of(vertical));
        when(catalog.findTemplate(templateId)).thenReturn(Optional.of(template));

        var result = service.resolve(tenant.getId());

        assertThat(result.verticalCode()).isEqualTo("lottery");
        assertThat(result.templateCode()).isEqualTo("lottery-phuong-nghi");
        assertThat(result.layoutConfig().get("layoutType").asText()).isEqualTo("sidebar-right");
    }

    @Test
    void refusesCorruptCrossVerticalConfiguration() {
        VerticalId lotteryId = VerticalId.newId();
        Tenant tenant = tenant(lotteryId, TemplateId.newId());
        var lottery = Vertical.create(lotteryId, CatalogCode.of("lottery"), "Xổ số", null, now);
        var wrongTemplate = WebsiteTemplate.create(tenant.getTemplateId(), VerticalId.newId(),
                CatalogCode.of("bds-modern"), "BĐS", new ObjectMapper().createObjectNode(),
                new ObjectMapper().createObjectNode(), now);
        when(tenants.findById(tenant.getId())).thenReturn(Optional.of(tenant));
        when(catalog.findVertical(lotteryId)).thenReturn(Optional.of(lottery));
        when(catalog.findTemplate(tenant.getTemplateId())).thenReturn(Optional.of(wrongTemplate));

        assertThatThrownBy(() -> service.resolve(tenant.getId()))
                .isInstanceOf(DomainException.class)
                .hasMessage("Tenant template does not belong to its vertical");
    }

    private Tenant tenant(VerticalId verticalId, TemplateId templateId) {
        return Tenant.register(TenantId.newId(), SiteKey.of("dealer-one"), TenantName.of("Dealer One"),
                TenantCode.of("DEALER_ONE"), DomainName.of("dealer.example.vn"), DisplayName.of("Dealer One"),
                null, null, TenantStatus.ACTIVE, verticalId, templateId, now);
    }
}
