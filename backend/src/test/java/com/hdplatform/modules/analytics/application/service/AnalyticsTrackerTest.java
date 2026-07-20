package com.hdplatform.modules.analytics.application.service;

import com.hdplatform.modules.analytics.application.AnalyticsEvents;
import com.hdplatform.modules.analytics.application.port.AnalyticsRepository;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AnalyticsTrackerTest {
    private final AnalyticsRepository repository = mock(AnalyticsRepository.class);
    private final Instant now = Instant.parse("2026-07-20T23:59:59Z");
    private final AnalyticsTracker tracker = new AnalyticsTracker(repository, () -> now);

    @Test
    void records_event_for_explicit_tenant_and_utc_timestamp() {
        TenantId tenantId = TenantId.newId();

        tracker.record(tenantId, AnalyticsEvents.PAGE_VIEW);

        verify(repository).increment(tenantId, AnalyticsEvents.PAGE_VIEW, now);
    }

    @Test
    void best_effort_tracking_never_breaks_public_request() {
        TenantId tenantId = TenantId.newId();
        doThrow(new IllegalStateException("database unavailable"))
                .when(repository).increment(tenantId, AnalyticsEvents.PAGE_VIEW, now);

        assertThatCode(() -> tracker.recordBestEffort(tenantId, AnalyticsEvents.PAGE_VIEW))
                .doesNotThrowAnyException();
    }
}
