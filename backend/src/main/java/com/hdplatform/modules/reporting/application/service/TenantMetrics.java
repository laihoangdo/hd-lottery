package com.hdplatform.modules.reporting.application.service;

import com.hdplatform.modules.reporting.application.port.TenantMetricRepository;
import com.hdplatform.modules.reporting.domain.valueobject.MetricKey;
import com.hdplatform.modules.tenant.application.context.TenantContextHolder;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import com.hdplatform.shared.domain.ClockProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TenantMetrics {
    private final TenantMetricRepository repository;
    private final ClockProvider clock;

    public void increment(TenantId tenantId, MetricKey key, long delta) {
        if (delta == 0) return;
        repository.increment(tenantId, key, delta, clock.now());
    }

    @Transactional(readOnly = true)
    public Map<String, Long> currentTenantSnapshot() {
        return repository.snapshot(TenantContextHolder.requireCurrent().tenantId())
                .entrySet().stream()
                .collect(java.util.stream.Collectors.toUnmodifiableMap(
                        entry -> entry.getKey().value(), Map.Entry::getValue));
    }
}
