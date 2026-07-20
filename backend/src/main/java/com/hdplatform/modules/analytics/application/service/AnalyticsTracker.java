package com.hdplatform.modules.analytics.application.service;

import com.hdplatform.modules.analytics.application.port.AnalyticsRepository;
import com.hdplatform.modules.analytics.domain.valueobject.AnalyticsEventName;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import com.hdplatform.shared.domain.ClockProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsTracker {
    private final AnalyticsRepository repository;
    private final ClockProvider clock;

    public void record(TenantId tenantId, AnalyticsEventName eventName) {
        repository.increment(tenantId, eventName, clock.now());
    }

    /** Analytics failure must not take down a public website request. */
    public void recordBestEffort(TenantId tenantId, AnalyticsEventName eventName) {
        try {
            record(tenantId, eventName);
        } catch (RuntimeException exception) {
            log.warn("Unable to record analytics event {}", eventName.value());
        }
    }
}
