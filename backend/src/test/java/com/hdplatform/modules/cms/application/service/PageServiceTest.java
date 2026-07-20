package com.hdplatform.modules.cms.application.service;

import com.hdplatform.modules.cms.application.command.CreatePageCommand;
import com.hdplatform.modules.cms.application.port.PageRepository;
import com.hdplatform.modules.cms.domain.aggregate.Page;
import com.hdplatform.modules.cms.domain.valueobject.PageSlug;
import com.hdplatform.modules.cms.domain.valueobject.PageStatus;
import com.hdplatform.modules.tenant.application.context.TenantContext;
import com.hdplatform.modules.tenant.application.context.TenantContextHolder;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import com.hdplatform.modules.tenant.domain.valueobject.SiteKey;
import com.hdplatform.shared.domain.ClockProvider;
import com.hdplatform.shared.exception.NotFoundException;
import com.hdplatform.modules.reporting.application.service.TenantMetrics;
import com.hdplatform.modules.reporting.application.PlatformMetricKeys;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PageServiceTest {

    private final PageRepository repository = mock(PageRepository.class);
    private final ClockProvider clock = () -> Instant.parse("2026-07-20T00:00:00Z");
    private final TenantMetrics tenantMetrics = mock(TenantMetrics.class);
    private final PageService service = new PageService(repository, clock, tenantMetrics);

    @AfterEach
    void clearTenant() {
        TenantContextHolder.clear();
    }

    @Test
    void creates_page_owned_by_current_tenant() {
        TenantId tenantId = TenantId.newId();
        TenantContextHolder.set(new TenantContext(tenantId, SiteKey.of("dealer-one")));
        when(repository.existsBySlug(tenantId, PageSlug.of("about-us"))).thenReturn(false);
        when(repository.save(any(Page.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Page page = service.createDraft(new CreatePageCommand(
                "about-us", "About us", "Reusable CMS content"));

        assertThat(page.getTenantId()).isEqualTo(tenantId);
        assertThat(page.getSlug()).isEqualTo(PageSlug.of("about-us"));
        ArgumentCaptor<Page> savedPage = ArgumentCaptor.forClass(Page.class);
        verify(repository).save(savedPage.capture());
        assertThat(savedPage.getValue().getTenantId()).isEqualTo(tenantId);
        verify(tenantMetrics).increment(tenantId, PlatformMetricKeys.CMS_PAGE_TOTAL, 1);
        verify(tenantMetrics).increment(tenantId, PlatformMetricKeys.CMS_PAGE_DRAFT, 1);
    }

    @Test
    void lookup_is_always_scoped_to_current_tenant() {
        TenantId tenantA = TenantId.newId();
        TenantId tenantB = TenantId.newId();
        PageSlug slug = PageSlug.of("contact");
        TenantContextHolder.set(new TenantContext(tenantB, SiteKey.of("dealer-two")));
        when(repository.findBySlug(tenantB, slug)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getBySlug("contact"))
                .isInstanceOf(NotFoundException.class);

        verify(repository).findBySlug(tenantB, slug);
        assertThat(tenantA).isNotEqualTo(tenantB);
    }

    @Test
    void refuses_cms_operation_without_tenant_context() {
        assertThatThrownBy(() -> service.getBySlug("contact"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("No tenant is associated with the current request");
    }

    @Test
    void public_lookup_requests_only_published_page_of_current_tenant() {
        TenantId tenantId = TenantId.newId();
        PageSlug slug = PageSlug.of("about-us");
        TenantContextHolder.set(new TenantContext(tenantId, SiteKey.of("dealer-one")));
        when(repository.findBySlugAndStatus(tenantId, slug, PageStatus.PUBLISHED))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getPublishedBySlug("about-us"))
                .isInstanceOf(NotFoundException.class);

        verify(repository).findBySlugAndStatus(tenantId, slug, PageStatus.PUBLISHED);
    }
}
