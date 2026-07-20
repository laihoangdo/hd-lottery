package com.hdplatform.modules.tenant.application.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdplatform.modules.platformcatalog.application.PlatformCatalogRepository;
import com.hdplatform.modules.platformcatalog.domain.*;
import com.hdplatform.modules.tenant.application.port.TenantRepository;
import com.hdplatform.modules.tenant.application.port.TenantIdentityProvisioner;
import com.hdplatform.modules.tenant.application.port.TenantOwnerAccount;
import com.hdplatform.modules.tenant.application.command.CreateTenantCommand;
import com.hdplatform.modules.tenant.domain.aggregate.Tenant;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import com.hdplatform.modules.tenant.domain.valueobject.*;
import com.hdplatform.shared.domain.ClockProvider;
import com.hdplatform.shared.domain.exception.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TenantWebsiteConfigurationTest {
    private final Instant now = Instant.parse("2026-07-20T00:00:00Z");
    private final TenantRepository tenants = mock(TenantRepository.class);
    private final PlatformCatalogRepository catalog = mock(PlatformCatalogRepository.class);
    private final TenantIdentityProvisioner identityProvisioner = mock(TenantIdentityProvisioner.class);
    private CreateTenantService service;

    @BeforeEach
    void setUp() {
        ClockProvider clock = mock(ClockProvider.class);
        when(clock.now()).thenReturn(now);
        service = new CreateTenantService(tenants, clock, catalog, identityProvisioner);
    }

    @Test
    void provisionsDefaultTenantRolesWhenCreatingAWebsite() {
        VerticalId verticalId = VerticalId.newId();
        TemplateId templateId = TemplateId.newId();
        stubActiveCatalog(verticalId, templateId);
        when(tenants.existsByCode(any())).thenReturn(false);
        when(tenants.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        TenantId tenantId = TenantId.newId();
        var command = new CreateTenantCommand(tenantId, TenantName.of("Dealer One"), TenantCode.of("DEALER_ONE"),
                SiteKey.of("dealer-one"), DomainName.of("dealer.example.vn"), DisplayName.of("Dealer One"),
                null, null, verticalId, templateId, "owner@example.com", "Owner One", "StrongPassword!123",
                null, null, null);

        service.execute(command);

        verify(identityProvisioner).provisionDefaultRoles(tenantId);
        verify(identityProvisioner).provisionWebsiteOwner(tenantId,
                new TenantOwnerAccount("owner@example.com", "Owner One", "StrongPassword!123"));
    }

    @Test
    void rejectsDuplicateSiteKeyBeforePersistingAnything() {
        VerticalId verticalId = VerticalId.newId();
        TemplateId templateId = TemplateId.newId();
        stubActiveCatalog(verticalId, templateId);
        when(tenants.existsBySiteKey(SiteKey.of("dealer-one"))).thenReturn(true);
        var command = new CreateTenantCommand(TenantId.newId(), TenantName.of("Dealer One"), TenantCode.of("DEALER_ONE"),
                SiteKey.of("dealer-one"), DomainName.of("dealer.example.vn"), DisplayName.of("Dealer One"),
                null, null, verticalId, templateId, "owner@example.com", "Owner", "StrongPassword!123",
                null, null, null);

        assertThatThrownBy(() -> service.execute(command))
                .isInstanceOf(DomainException.class).hasMessage("Site key already exists.");
        verify(tenants, never()).save(any());
        verifyNoInteractions(identityProvisioner);
    }

    @Test
    void switchesTemplateWithoutChangingWebsiteVerticalOrContentIdentity() {
        VerticalId lotteryId = VerticalId.newId();
        TemplateId oldTemplateId = TemplateId.newId();
        TemplateId newTemplateId = TemplateId.newId();
        Tenant tenant = tenant(lotteryId, oldTemplateId);
        stubActiveCatalog(lotteryId, newTemplateId);
        when(tenants.findById(tenant.getId())).thenReturn(Optional.of(tenant));
        when(tenants.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Tenant changed = service.switchTemplate(tenant.getId(), newTemplateId);

        assertThat(changed.getTemplateId()).isEqualTo(newTemplateId);
        assertThat(changed.getVerticalId()).isEqualTo(lotteryId);
        assertThat(changed.getSiteKey()).isEqualTo(SiteKey.of("dealer-one"));
    }

    @Test
    void rejectsTemplateFromAnotherVerticalBeforeSavingTenant() {
        VerticalId lotteryId = VerticalId.newId();
        VerticalId realEstateId = VerticalId.newId();
        TemplateId bdsTemplateId = TemplateId.newId();
        Tenant tenant = tenant(lotteryId, TemplateId.newId());
        Vertical lottery = Vertical.create(lotteryId, CatalogCode.of("lottery"), "Xổ số", null, now);
        WebsiteTemplate bds = WebsiteTemplate.create(bdsTemplateId, realEstateId, CatalogCode.of("bds-modern"),
                "BĐS Modern", new ObjectMapper().createObjectNode(), new ObjectMapper().createObjectNode(), now);
        when(tenants.findById(tenant.getId())).thenReturn(Optional.of(tenant));
        when(catalog.findVertical(lotteryId)).thenReturn(Optional.of(lottery));
        when(catalog.findTemplate(bdsTemplateId)).thenReturn(Optional.of(bds));

        assertThatThrownBy(() -> service.switchTemplate(tenant.getId(), bdsTemplateId))
                .isInstanceOf(DomainException.class)
                .hasMessage("Template does not belong to the selected vertical");
        verify(tenants, never()).save(any());
    }

    private void stubActiveCatalog(VerticalId verticalId, TemplateId templateId) {
        Vertical vertical = Vertical.create(verticalId, CatalogCode.of("lottery"), "Xổ số", null, now);
        WebsiteTemplate template = WebsiteTemplate.create(templateId, verticalId, CatalogCode.of("lottery-modern"),
                "Modern", new ObjectMapper().createObjectNode(), new ObjectMapper().createObjectNode(), now);
        when(catalog.findVertical(verticalId)).thenReturn(Optional.of(vertical));
        when(catalog.findTemplate(templateId)).thenReturn(Optional.of(template));
    }

    private Tenant tenant(VerticalId verticalId, TemplateId templateId) {
        return Tenant.register(TenantId.newId(), SiteKey.of("dealer-one"), TenantName.of("Dealer One"),
                TenantCode.of("DEALER_ONE"), DomainName.of("dealer.example.vn"), DisplayName.of("Dealer One"),
                null, null, TenantStatus.ACTIVE, verticalId, templateId, now);
    }
}
