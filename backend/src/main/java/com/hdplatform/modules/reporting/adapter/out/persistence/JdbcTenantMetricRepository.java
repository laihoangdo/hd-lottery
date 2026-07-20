package com.hdplatform.modules.reporting.adapter.out.persistence;

import com.hdplatform.modules.reporting.application.port.TenantMetricRepository;
import com.hdplatform.modules.reporting.domain.valueobject.MetricKey;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class JdbcTenantMetricRepository implements TenantMetricRepository {
    private static final String INCREMENT = """
            INSERT INTO reporting_tenant_metrics (tenant_id, metric_key, metric_value, updated_at)
            VALUES (:tenantId, :metricKey, :delta, :now)
            ON CONFLICT (tenant_id, metric_key) DO UPDATE
            SET metric_value = reporting_tenant_metrics.metric_value + EXCLUDED.metric_value,
                updated_at = EXCLUDED.updated_at
            """;
    private static final String SNAPSHOT = """
            SELECT metric_key, metric_value
            FROM reporting_tenant_metrics
            WHERE tenant_id = :tenantId
            ORDER BY metric_key
            """;

    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public void increment(TenantId tenantId, MetricKey key, long delta, Instant now) {
        jdbc.update(INCREMENT, Map.of(
                "tenantId", tenantId.getValue(), "metricKey", key.value(),
                "delta", delta, "now", now));
    }

    @Override
    public Map<MetricKey, Long> snapshot(TenantId tenantId) {
        LinkedHashMap<MetricKey, Long> result = new LinkedHashMap<>();
        jdbc.query(SNAPSHOT, Map.of("tenantId", tenantId.getValue()), resultSet -> {
            result.put(MetricKey.of(resultSet.getString("metric_key")),
                    resultSet.getLong("metric_value"));
        });
        return Map.copyOf(result);
    }
}
