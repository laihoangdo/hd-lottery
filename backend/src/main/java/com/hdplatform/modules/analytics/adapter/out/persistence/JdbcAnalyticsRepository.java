package com.hdplatform.modules.analytics.adapter.out.persistence;

import com.hdplatform.modules.analytics.application.port.AnalyticsRepository;
import com.hdplatform.modules.analytics.application.query.DailyEventCount;
import com.hdplatform.modules.analytics.domain.valueobject.AnalyticsEventName;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class JdbcAnalyticsRepository implements AnalyticsRepository {
    private static final String INCREMENT = """
            INSERT INTO analytics_daily_events
                (tenant_id, event_date, event_name, event_count, updated_at)
            VALUES (:tenantId, :eventDate, :eventName, 1, :updatedAt)
            ON CONFLICT (tenant_id, event_date, event_name) DO UPDATE
            SET event_count = analytics_daily_events.event_count + 1,
                updated_at = EXCLUDED.updated_at
            """;
    private static final String TREND = """
            SELECT event_date, event_count
            FROM analytics_daily_events
            WHERE tenant_id = :tenantId
              AND event_name = :eventName
              AND event_date BETWEEN :fromDate AND :toDate
            ORDER BY event_date
            """;

    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public void increment(TenantId tenantId, AnalyticsEventName eventName, Instant occurredAt) {
        jdbc.update(INCREMENT, Map.of(
                "tenantId", tenantId.getValue(),
                "eventDate", occurredAt.atZone(ZoneOffset.UTC).toLocalDate(),
                "eventName", eventName.value(),
                "updatedAt", occurredAt));
    }

    @Override
    public List<DailyEventCount> trend(TenantId tenantId, AnalyticsEventName eventName,
                                       LocalDate from, LocalDate to) {
        return jdbc.query(TREND, Map.of(
                        "tenantId", tenantId.getValue(), "eventName", eventName.value(),
                        "fromDate", from, "toDate", to),
                (resultSet, rowNum) -> new DailyEventCount(
                        resultSet.getObject("event_date", LocalDate.class),
                        resultSet.getLong("event_count")));
    }
}
