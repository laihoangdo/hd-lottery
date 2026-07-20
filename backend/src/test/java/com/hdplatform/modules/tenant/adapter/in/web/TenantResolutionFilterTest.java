package com.hdplatform.modules.tenant.adapter.in.web;

import com.hdplatform.modules.tenant.application.context.TenantContextHolder;
import com.hdplatform.modules.tenant.application.port.TenantRepository;
import com.hdplatform.modules.tenant.application.service.TenantResolver;
import com.hdplatform.modules.tenant.domain.aggregate.Tenant;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import com.hdplatform.modules.tenant.domain.valueobject.DisplayName;
import com.hdplatform.modules.tenant.domain.valueobject.DomainName;
import com.hdplatform.modules.tenant.domain.valueobject.SiteKey;
import com.hdplatform.modules.tenant.domain.valueobject.TenantCode;
import com.hdplatform.modules.tenant.domain.valueobject.TenantName;
import com.hdplatform.modules.tenant.domain.valueobject.TenantStatus;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.time.Instant;
import com.hdplatform.modules.platformcatalog.domain.TemplateId;
import com.hdplatform.modules.platformcatalog.domain.VerticalId;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class TenantResolutionFilterTest {

    private final TenantRepository repository = mock(TenantRepository.class);
    private final TenantResolver resolver = new TenantResolver(repository);

    @AfterEach
    void clearContext() {
        TenantContextHolder.clear();
    }

    @Test
    void resolves_active_tenant_from_request_hostname_and_clears_context_after_request()
            throws ServletException, IOException {
        Tenant tenant = tenant(TenantStatus.ACTIVE);
        when(repository.findByDomainName(DomainName.of("dealer.example.vn")))
                .thenReturn(Optional.of(tenant));
        TenantResolutionFilter filter = new TenantResolutionFilter(
                resolver,
                new TenantResolutionProperties(false));
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("dealer.example.vn");
        AtomicReference<TenantId> tenantSeenByRequest = new AtomicReference<>();

        filter.doFilter(request, new MockHttpServletResponse(),
                (ignoredRequest, ignoredResponse) -> tenantSeenByRequest.set(
                        TenantContextHolder.requireCurrent().tenantId()));

        assertThat(tenantSeenByRequest.get()).isEqualTo(tenant.getId());
        assertThat(TenantContextHolder.current()).isEmpty();
    }

    @Test
    void does_not_expose_inactive_tenant_to_request() throws ServletException, IOException {
        when(repository.findByDomainName(DomainName.of("disabled.example.vn")))
                .thenReturn(Optional.of(tenant(TenantStatus.SUSPENDED)));
        TenantResolutionFilter filter = new TenantResolutionFilter(
                resolver,
                new TenantResolutionProperties(false));
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("disabled.example.vn");
        AtomicReference<Boolean> contextPresent = new AtomicReference<>();

        filter.doFilter(request, new MockHttpServletResponse(),
                (ignoredRequest, ignoredResponse) -> contextPresent.set(
                        TenantContextHolder.current().isPresent()));

        assertThat(contextPresent.get()).isFalse();
        assertThat(TenantContextHolder.current()).isEmpty();
    }

    @Test
    void ignores_untrusted_site_key_header_when_header_resolution_is_disabled()
            throws ServletException, IOException {
        when(repository.findByDomainName(DomainName.of("unknown.example.vn")))
                .thenReturn(Optional.empty());
        TenantResolutionFilter filter = new TenantResolutionFilter(
                resolver,
                new TenantResolutionProperties(false));
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("unknown.example.vn");
        request.addHeader(TenantResolutionFilter.SITE_KEY_HEADER, "another-tenant");

        filter.doFilter(request, new MockHttpServletResponse(), new MockFilterChain());

        verify(repository).findByDomainName(DomainName.of("unknown.example.vn"));
        verify(repository, never()).findBySiteKey(SiteKey.of("another-tenant"));
    }

    @Test
    void allows_local_operational_requests_without_creating_a_tenant_context()
            throws ServletException, IOException {
        TenantResolutionFilter filter = new TenantResolutionFilter(
                resolver,
                new TenantResolutionProperties(false));
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("localhost");
        AtomicReference<Boolean> contextPresent = new AtomicReference<>();

        filter.doFilter(request, new MockHttpServletResponse(),
                (ignoredRequest, ignoredResponse) -> contextPresent.set(
                        TenantContextHolder.current().isPresent()));

        assertThat(contextPresent.get()).isFalse();
        verifyNoInteractions(repository);
    }

    private Tenant tenant(TenantStatus status) {
        return Tenant.register(
                TenantId.newId(),
                SiteKey.of("dealer-one"),
                TenantName.of("Dealer One"),
                TenantCode.of("DEALER_ONE"),
                DomainName.of(status == TenantStatus.ACTIVE
                        ? "dealer.example.vn"
                        : "disabled.example.vn"),
                DisplayName.of("Dealer One"),
                null,
                null,
                status,
                VerticalId.of(java.util.UUID.fromString("00000000-0000-0000-0000-000000000101")),
                TemplateId.of(java.util.UUID.fromString("00000000-0000-0000-0000-000000000201")),
                Instant.parse("2026-07-20T00:00:00Z"));
    }
}
