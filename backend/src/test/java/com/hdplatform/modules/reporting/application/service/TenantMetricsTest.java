package com.hdplatform.modules.reporting.application.service;

import com.hdplatform.modules.reporting.application.PlatformMetricKeys;
import com.hdplatform.modules.reporting.application.port.TenantMetricRepository;
import com.hdplatform.modules.tenant.application.context.TenantContext;
import com.hdplatform.modules.tenant.application.context.TenantContextHolder;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import com.hdplatform.modules.tenant.domain.valueobject.SiteKey;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TenantMetricsTest {
    private final TenantMetricRepository repository = mock(TenantMetricRepository.class);
    private final TenantMetrics metrics = new TenantMetrics(
            repository, () -> Instant.parse("2026-07-20T00:00:00Z"));

    @AfterEach void clearTenant() { TenantContextHolder.clear(); }

    @Test
    void dashboard_snapshot_is_scoped_to_current_tenant() {
        TenantId tenantId = TenantId.newId();
        TenantContextHolder.set(new TenantContext(tenantId, SiteKey.of("dealer-one")));
        when(repository.snapshot(tenantId)).thenReturn(Map.of(
                PlatformMetricKeys.CMS_PAGE_TOTAL, 12L,
                PlatformMetricKeys.MEDIA_ASSET_TOTAL, 4L));

        Map<String, Long> snapshot = metrics.currentTenantSnapshot();

        assertThat(snapshot).containsEntry("cms.page.total", 12L)
                .containsEntry("media.asset.total", 4L);
        verify(repository).snapshot(tenantId);
    }

    @Test
    void records_delta_for_explicit_tenant() {
        TenantId tenantId = TenantId.newId();

        metrics.increment(tenantId, PlatformMetricKeys.CMS_PAGE_TOTAL, 1);

        verify(repository).increment(tenantId, PlatformMetricKeys.CMS_PAGE_TOTAL, 1,
                Instant.parse("2026-07-20T00:00:00Z"));
    }
}
