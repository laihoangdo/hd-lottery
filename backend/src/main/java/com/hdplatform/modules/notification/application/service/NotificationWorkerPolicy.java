package com.hdplatform.modules.notification.application.service;

import java.time.Duration;

public record NotificationWorkerPolicy(int batchSize, int maxAttempts, Duration lockTimeout) {
    public NotificationWorkerPolicy {
        if (batchSize <= 0 || maxAttempts <= 0 || lockTimeout.isNegative() || lockTimeout.isZero()) {
            throw new IllegalArgumentException("Invalid notification worker policy");
        }
    }
}
