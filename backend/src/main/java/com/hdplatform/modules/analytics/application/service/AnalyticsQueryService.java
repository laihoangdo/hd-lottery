package com.hdplatform.modules.analytics.application.service;

import com.hdplatform.modules.analytics.application.port.AnalyticsRepository;
import com.hdplatform.modules.analytics.application.query.DailyEventCount;
import com.hdplatform.modules.analytics.domain.valueobject.AnalyticsEventName;
import com.hdplatform.modules.analytics.application.AnalyticsEvents;
import com.hdplatform.modules.tenant.application.context.TenantContextHolder;
import com.hdplatform.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsQueryService {
    private final AnalyticsRepository repository;

    @Transactional(readOnly = true)
    public List<DailyEventCount> trend(String eventName, LocalDate from, LocalDate to) {
        if (from == null || to == null || from.isAfter(to)
                || ChronoUnit.DAYS.between(from, to) > 89) {
            throw new ValidationException(
                    "ANALYTICS_DATE_RANGE_INVALID",
                    "Analytics date range must contain at most 90 days");
        }
        return repository.trend(
                TenantContextHolder.requireCurrent().tenantId(),
                AnalyticsEvents.requireKnown(eventName), from, to);
    }
}
