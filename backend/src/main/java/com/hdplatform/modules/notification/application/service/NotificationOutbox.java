package com.hdplatform.modules.notification.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdplatform.modules.notification.application.command.EnqueueNotificationCommand;
import com.hdplatform.modules.notification.application.port.NotificationRepository;
import com.hdplatform.modules.notification.domain.aggregate.Notification;
import com.hdplatform.modules.notification.domain.aggregate.NotificationId;
import com.hdplatform.modules.tenant.application.context.TenantContextHolder;
import com.hdplatform.shared.domain.ClockProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationOutbox {
    private final NotificationRepository repository;
    private final ObjectMapper objectMapper;
    private final ClockProvider clock;

    @Transactional
    public Notification enqueue(EnqueueNotificationCommand command) {
        try {
            return repository.save(Notification.enqueue(
                    NotificationId.newId(),
                    TenantContextHolder.requireCurrent().tenantId(),
                    command.channel(), command.recipient(), command.templateKey(),
                    objectMapper.writeValueAsString(command.payload()), clock.now()));
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("Notification payload cannot be serialized", exception);
        }
    }
}
