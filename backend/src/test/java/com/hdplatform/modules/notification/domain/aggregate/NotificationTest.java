package com.hdplatform.modules.notification.domain.aggregate;

import com.hdplatform.modules.notification.domain.valueobject.NotificationChannel;
import com.hdplatform.modules.notification.domain.valueobject.NotificationStatus;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationTest {
    private static final Instant NOW = Instant.parse("2026-07-20T00:00:00Z");

    @Test
    void schedules_exponential_retry_after_delivery_failure() {
        Notification notification = processing(2);

        notification.markDeliveryFailed(NOW, "provider timeout", 5);

        assertThat(notification.getStatus()).isEqualTo(NotificationStatus.PENDING);
        assertThat(notification.getNextAttemptAt()).isEqualTo(NOW.plusSeconds(60));
        assertThat(notification.getLockedAt()).isNull();
        assertThat(notification.getLastError()).isEqualTo("provider timeout");
    }

    @Test
    void permanently_fails_after_maximum_attempts() {
        Notification notification = processing(5);

        notification.markDeliveryFailed(NOW, "rejected", 5);

        assertThat(notification.getStatus()).isEqualTo(NotificationStatus.FAILED);
        assertThat(notification.getNextAttemptAt()).isNull();
    }

    @Test
    void marks_successful_delivery_and_clears_lock() {
        Notification notification = processing(1);

        notification.markSent(NOW);

        assertThat(notification.getStatus()).isEqualTo(NotificationStatus.SENT);
        assertThat(notification.getSentAt()).isEqualTo(NOW);
        assertThat(notification.getLockedAt()).isNull();
    }

    private Notification processing(int attempts) {
        return Notification.restore(NotificationId.newId(), TenantId.newId(),
                NotificationChannel.EMAIL, "user@example.vn", "welcome", "{}",
                NotificationStatus.PROCESSING, attempts, NOW, NOW.minusSeconds(10),
                null, null, java.util.UUID.randomUUID(),
                NOW.minusSeconds(60), NOW.minusSeconds(10));
    }
}
