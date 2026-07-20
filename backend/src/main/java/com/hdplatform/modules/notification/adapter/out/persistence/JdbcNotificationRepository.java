package com.hdplatform.modules.notification.adapter.out.persistence;

import com.hdplatform.modules.notification.application.port.NotificationRepository;
import com.hdplatform.modules.notification.domain.aggregate.Notification;
import com.hdplatform.modules.notification.domain.aggregate.NotificationId;
import com.hdplatform.modules.notification.domain.valueobject.NotificationChannel;
import com.hdplatform.modules.notification.domain.valueobject.NotificationStatus;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class JdbcNotificationRepository implements NotificationRepository {
    private static final String INSERT = """
            INSERT INTO notification_outbox
                (id, tenant_id, channel, recipient, template_key, payload_json, status,
                 attempts, next_attempt_at, locked_at, sent_at, last_error, claim_token,
                 created_at, updated_at)
            VALUES
                (:id, :tenantId, :channel, :recipient, :templateKey, CAST(:payloadJson AS jsonb),
                 :status, :attempts, :nextAttemptAt, :lockedAt, :sentAt, :lastError, NULL,
                 :createdAt, :updatedAt)
            """;

    private static final String COMPLETE_CLAIM = """
            UPDATE notification_outbox
            SET status = :status,
                attempts = :attempts,
                next_attempt_at = :nextAttemptAt,
                locked_at = :lockedAt,
                sent_at = :sentAt,
                last_error = :lastError,
                claim_token = NULL,
                updated_at = :updatedAt
            WHERE id = :id
              AND status = 'PROCESSING'
              AND claim_token = :claimToken
            """;

    private static final String CLAIM = """
            WITH candidates AS (
                SELECT id
                FROM notification_outbox
                WHERE (status = 'PENDING' AND next_attempt_at <= :now)
                   OR (status = 'PROCESSING' AND locked_at < :staleBefore)
                ORDER BY next_attempt_at NULLS LAST, created_at
                FOR UPDATE SKIP LOCKED
                LIMIT :batchSize
            )
            UPDATE notification_outbox n
            SET status = 'PROCESSING',
                attempts = n.attempts + 1,
                locked_at = :now,
                claim_token = gen_random_uuid(),
                updated_at = :now
            FROM candidates c
            WHERE n.id = c.id
            RETURNING n.*
            """;

    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public Notification save(Notification notification) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", notification.getId().getValue());
        parameters.put("tenantId", notification.getTenantId().getValue());
        parameters.put("channel", notification.getChannel().name());
        parameters.put("recipient", notification.getRecipient());
        parameters.put("templateKey", notification.getTemplateKey());
        parameters.put("payloadJson", notification.getPayloadJson());
        parameters.put("status", notification.getStatus().name());
        parameters.put("attempts", notification.getAttempts());
        parameters.put("nextAttemptAt", notification.getNextAttemptAt());
        parameters.put("lockedAt", notification.getLockedAt());
        parameters.put("sentAt", notification.getSentAt());
        parameters.put("lastError", notification.getLastError());
        parameters.put("createdAt", notification.getCreatedAt());
        parameters.put("updatedAt", notification.getUpdatedAt());
        if (notification.getClaimToken() == null) {
            jdbc.update(INSERT, parameters);
        } else {
            parameters.put("claimToken", notification.getClaimToken());
            int updated = jdbc.update(COMPLETE_CLAIM, parameters);
            if (updated != 1) {
                throw new IllegalStateException("Notification claim is no longer owned by this worker");
            }
        }
        return notification;
    }

    @Override
    @Transactional
    public List<Notification> claimBatch(int batchSize, Instant now, Instant staleBefore) {
        return jdbc.query(CLAIM, Map.of(
                "batchSize", batchSize,
                "now", now,
                "staleBefore", staleBefore), mapper());
    }

    private RowMapper<Notification> mapper() {
        return (resultSet, rowNum) -> Notification.restore(
                NotificationId.of(resultSet.getObject("id", java.util.UUID.class)),
                TenantId.of(resultSet.getObject("tenant_id", java.util.UUID.class)),
                NotificationChannel.valueOf(resultSet.getString("channel")),
                resultSet.getString("recipient"), resultSet.getString("template_key"),
                resultSet.getString("payload_json"),
                NotificationStatus.valueOf(resultSet.getString("status")),
                resultSet.getInt("attempts"), instant(resultSet, "next_attempt_at"),
                instant(resultSet, "locked_at"), instant(resultSet, "sent_at"),
                resultSet.getString("last_error"),
                resultSet.getObject("claim_token", java.util.UUID.class),
                instant(resultSet, "created_at"),
                instant(resultSet, "updated_at"));
    }

    private Instant instant(ResultSet resultSet, String column) throws SQLException {
        java.sql.Timestamp value = resultSet.getTimestamp(column);
        return value == null ? null : value.toInstant();
    }
}
