package com.hdplatform.modules.reporting.application.port;

import com.hdplatform.modules.reporting.domain.valueobject.MetricKey;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;

import java.time.Instant;
import java.util.Map;

public interface TenantMetricRepository {
    void increment(TenantId tenantId, MetricKey key, long delta, Instant now);
    Map<MetricKey, Long> snapshot(TenantId tenantId);
}
