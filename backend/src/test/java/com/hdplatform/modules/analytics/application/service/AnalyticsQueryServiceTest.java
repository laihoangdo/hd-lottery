package com.hdplatform.modules.analytics.application.service;

import com.hdplatform.modules.analytics.application.AnalyticsEvents;
import com.hdplatform.modules.analytics.application.port.AnalyticsRepository;
import com.hdplatform.modules.analytics.application.query.DailyEventCount;
import com.hdplatform.modules.tenant.application.context.TenantContext;
import com.hdplatform.modules.tenant.application.context.TenantContextHolder;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import com.hdplatform.modules.tenant.domain.valueobject.SiteKey;
import com.hdplatform.shared.exception.ValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AnalyticsQueryServiceTest {
    private final AnalyticsRepository repository = mock(AnalyticsRepository.class);
    private final AnalyticsQueryService service = new AnalyticsQueryService(repository);

    @AfterEach void clearTenant() { TenantContextHolder.clear(); }

    @Test
    void trend_query_is_scoped_to_current_tenant_and_known_event() {
        TenantId tenantId = TenantId.newId();
        TenantContextHolder.set(new TenantContext(tenantId, SiteKey.of("dealer-one")));
        LocalDate from = LocalDate.of(2026, 7, 1);
        LocalDate to = LocalDate.of(2026, 7, 20);
        List<DailyEventCount> expected = List.of(new DailyEventCount(from, 10));
        when(repository.trend(tenantId, AnalyticsEvents.PAGE_VIEW, from, to))
                .thenReturn(expected);

        assertThat(service.trend("page_view", from, to)).isEqualTo(expected);
        verify(repository).trend(tenantId, AnalyticsEvents.PAGE_VIEW, from, to);
    }

    @Test
    void rejects_more_than_ninety_days() {
        assertThatThrownBy(() -> service.trend(
                "page_view", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 4, 1)))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void rejects_unknown_event_to_prevent_unbounded_cardinality() {
        TenantContextHolder.set(new TenantContext(TenantId.newId(), SiteKey.of("dealer-one")));

        assertThatThrownBy(() -> service.trend(
                "custom_unbounded_event", LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 2)))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Unknown analytics event name");
    }
}
