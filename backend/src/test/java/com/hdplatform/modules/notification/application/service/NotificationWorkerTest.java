package com.hdplatform.modules.notification.application.service;

import com.hdplatform.modules.notification.application.port.NotificationDispatcher;
import com.hdplatform.modules.notification.application.port.NotificationRepository;
import com.hdplatform.modules.notification.domain.aggregate.Notification;
import com.hdplatform.modules.notification.domain.aggregate.NotificationId;
import com.hdplatform.modules.notification.domain.valueobject.NotificationChannel;
import com.hdplatform.modules.notification.domain.valueobject.NotificationStatus;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import com.hdplatform.shared.domain.ClockProvider;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NotificationWorkerTest {
    private static final Instant NOW = Instant.parse("2026-07-20T00:00:00Z");
    private final NotificationRepository repository = mock(NotificationRepository.class);
    private final ClockProvider clock = () -> NOW;

    @Test
    void dispatches_claimed_message_and_marks_it_sent() throws Exception {
        Notification notification = processing(1);
        NotificationDispatcher dispatcher = mock(NotificationDispatcher.class);
        when(dispatcher.channel()).thenReturn(NotificationChannel.EMAIL);
        when(repository.claimBatch(10, NOW, NOW.minusSeconds(300)))
                .thenReturn(List.of(notification));
        NotificationWorker worker = new NotificationWorker(repository, List.of(dispatcher),
                new NotificationWorkerPolicy(10, 5, Duration.ofMinutes(5)), clock);

        worker.processBatch();

        verify(dispatcher).send(notification);
        ArgumentCaptor<Notification> saved = ArgumentCaptor.forClass(Notification.class);
        verify(repository).save(saved.capture());
        assertThat(saved.getValue().getStatus()).isEqualTo(NotificationStatus.SENT);
    }

    @Test
    void missing_provider_schedules_retry_instead_of_losing_message() {
        Notification notification = processing(1);
        when(repository.claimBatch(10, NOW, NOW.minusSeconds(300)))
                .thenReturn(List.of(notification));
        NotificationWorker worker = new NotificationWorker(repository, List.of(),
                new NotificationWorkerPolicy(10, 5, Duration.ofMinutes(5)), clock);

        worker.processBatch();

        ArgumentCaptor<Notification> saved = ArgumentCaptor.forClass(Notification.class);
        verify(repository).save(saved.capture());
        assertThat(saved.getValue().getStatus()).isEqualTo(NotificationStatus.PENDING);
        assertThat(saved.getValue().getNextAttemptAt()).isEqualTo(NOW.plusSeconds(30));
    }

    private Notification processing(int attempts) {
        return Notification.restore(NotificationId.newId(), TenantId.newId(),
                NotificationChannel.EMAIL, "user@example.vn", "welcome", "{}",
                NotificationStatus.PROCESSING, attempts, NOW, NOW.minusSeconds(10),
                null, null, java.util.UUID.randomUUID(),
                NOW.minusSeconds(60), NOW.minusSeconds(10));
    }
}
