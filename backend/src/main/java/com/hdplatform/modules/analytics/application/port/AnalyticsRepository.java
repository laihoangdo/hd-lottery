package com.hdplatform.modules.analytics.application.port;

import com.hdplatform.modules.analytics.application.query.DailyEventCount;
import com.hdplatform.modules.analytics.domain.valueobject.AnalyticsEventName;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public interface AnalyticsRepository {
    void increment(TenantId tenantId, AnalyticsEventName eventName, Instant occurredAt);
    List<DailyEventCount> trend(TenantId tenantId, AnalyticsEventName eventName,
                                LocalDate from, LocalDate to);
}
