package com.hdplatform.modules.notification.application.service;

import com.hdplatform.modules.notification.application.port.NotificationDispatcher;
import com.hdplatform.modules.notification.application.port.NotificationRepository;
import com.hdplatform.modules.notification.domain.aggregate.Notification;
import com.hdplatform.shared.domain.ClockProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class NotificationWorker {
    private final NotificationRepository repository;
    private final Map<com.hdplatform.modules.notification.domain.valueobject.NotificationChannel,
            NotificationDispatcher> dispatchers;
    private final NotificationWorkerPolicy policy;
    private final ClockProvider clock;

    public NotificationWorker(NotificationRepository repository,
                              List<NotificationDispatcher> dispatchers,
                              NotificationWorkerPolicy policy,
                              ClockProvider clock) {
        this.repository = repository;
        this.policy = policy;
        this.clock = clock;
        this.dispatchers = new EnumMap<>(
                com.hdplatform.modules.notification.domain.valueobject.NotificationChannel.class);
        dispatchers.forEach(dispatcher -> {
            if (this.dispatchers.put(dispatcher.channel(), dispatcher) != null) {
                throw new IllegalStateException("Duplicate dispatcher for " + dispatcher.channel());
            }
        });
    }

    @Scheduled(fixedDelayString = "${hd-platform.notification.worker-delay-ms:1000}")
    public void processBatch() {
        Instant now = clock.now();
        repository.claimBatch(policy.batchSize(), now, now.minus(policy.lockTimeout()))
                .forEach(this::deliver);
    }

    private void deliver(Notification notification) {
        try {
            NotificationDispatcher dispatcher = dispatchers.get(notification.getChannel());
            if (dispatcher == null) {
                throw new IllegalStateException(
                        "No dispatcher configured for " + notification.getChannel());
            }
            dispatcher.send(notification);
            notification.markSent(clock.now());
        } catch (Exception exception) {
            notification.markDeliveryFailed(
                    clock.now(), exception.getMessage(), policy.maxAttempts());
        }
        repository.save(notification);
    }
}
