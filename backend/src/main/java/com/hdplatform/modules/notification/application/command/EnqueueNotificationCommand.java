package com.hdplatform.modules.notification.application.command;

import com.hdplatform.modules.notification.domain.valueobject.NotificationChannel;

import java.util.Map;

public record EnqueueNotificationCommand(
        NotificationChannel channel,
        String recipient,
        String templateKey,
        Map<String, Object> payload
) {
}
