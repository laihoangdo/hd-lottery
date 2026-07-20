package com.hdplatform.modules.notification.domain.aggregate;

import com.hdplatform.modules.notification.domain.valueobject.NotificationChannel;
import com.hdplatform.modules.notification.domain.valueobject.NotificationStatus;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import com.hdplatform.shared.domain.AuditableEntity;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class Notification extends AuditableEntity<NotificationId> {
    private final TenantId tenantId;
    private final NotificationChannel channel;
    private final String recipient;
    private final String templateKey;
    private final String payloadJson;
    private NotificationStatus status;
    private int attempts;
    private Instant nextAttemptAt;
    private Instant lockedAt;
    private Instant sentAt;
    private String lastError;
    private UUID claimToken;

    private Notification(NotificationId id, TenantId tenantId, NotificationChannel channel,
                         String recipient, String templateKey, String payloadJson) {
        super(id);
        this.tenantId = Objects.requireNonNull(tenantId);
        this.channel = Objects.requireNonNull(channel);
        this.recipient = requireText(recipient, "recipient", 320);
        this.templateKey = requireText(templateKey, "templateKey", 100);
        this.payloadJson = requireText(payloadJson, "payloadJson", 20_000);
    }

    public static Notification enqueue(NotificationId id, TenantId tenantId,
                                       NotificationChannel channel, String recipient,
                                       String templateKey, String payloadJson, Instant now) {
        Notification notification = new Notification(
                id, tenantId, channel, recipient, templateKey, payloadJson);
        notification.status = NotificationStatus.PENDING;
        notification.nextAttemptAt = Objects.requireNonNull(now);
        notification.markCreated(now);
        return notification;
    }

    public static Notification restore(NotificationId id, TenantId tenantId,
                                       NotificationChannel channel, String recipient,
                                       String templateKey, String payloadJson,
                                       NotificationStatus status, int attempts,
                                       Instant nextAttemptAt, Instant lockedAt, Instant sentAt,
                                       String lastError, UUID claimToken,
                                       Instant createdAt, Instant updatedAt) {
        Notification notification = new Notification(
                id, tenantId, channel, recipient, templateKey, payloadJson);
        notification.status = status;
        notification.attempts = attempts;
        notification.nextAttemptAt = nextAttemptAt;
        notification.lockedAt = lockedAt;
        notification.sentAt = sentAt;
        notification.lastError = lastError;
        notification.claimToken = claimToken;
        notification.createdAt = createdAt;
        notification.updatedAt = updatedAt;
        return notification;
    }

    public void markSent(Instant now) {
        ensureProcessing();
        status = NotificationStatus.SENT;
        sentAt = Objects.requireNonNull(now);
        lockedAt = null;
        lastError = null;
        markUpdated(now);
    }

    public void markDeliveryFailed(Instant now, String error, int maxAttempts) {
        ensureProcessing();
        if (maxAttempts <= 0) throw new IllegalArgumentException("maxAttempts must be positive");
        lastError = truncateError(error);
        lockedAt = null;
        if (attempts >= maxAttempts) {
            status = NotificationStatus.FAILED;
            nextAttemptAt = null;
        } else {
            status = NotificationStatus.PENDING;
            long multiplier = 1L << Math.min(Math.max(attempts - 1, 0), 7);
            nextAttemptAt = now.plus(Duration.ofSeconds(30L * multiplier));
        }
        markUpdated(now);
    }

    private void ensureProcessing() {
        if (status != NotificationStatus.PROCESSING) {
            throw new IllegalStateException("Notification must be processing");
        }
    }

    private static String requireText(String value, String field, int max) {
        if (value == null || value.isBlank() || value.length() > max) {
            throw new IllegalArgumentException(field + " is invalid");
        }
        return value.trim();
    }

    private static String truncateError(String error) {
        String safe = error == null || error.isBlank() ? "Delivery failed" : error.trim();
        return safe.substring(0, Math.min(safe.length(), 1000));
    }

    public TenantId getTenantId() { return tenantId; }
    public NotificationChannel getChannel() { return channel; }
    public String getRecipient() { return recipient; }
    public String getTemplateKey() { return templateKey; }
    public String getPayloadJson() { return payloadJson; }
    public NotificationStatus getStatus() { return status; }
    public int getAttempts() { return attempts; }
    public Instant getNextAttemptAt() { return nextAttemptAt; }
    public Instant getLockedAt() { return lockedAt; }
    public Instant getSentAt() { return sentAt; }
    public String getLastError() { return lastError; }
    public UUID getClaimToken() { return claimToken; }
}
