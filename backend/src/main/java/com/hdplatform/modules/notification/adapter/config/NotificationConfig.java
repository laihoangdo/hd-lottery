package com.hdplatform.modules.notification.adapter.config;

import com.hdplatform.modules.notification.application.service.NotificationWorkerPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Duration;

@Configuration
@EnableScheduling
public class NotificationConfig {
    @Bean
    NotificationWorkerPolicy notificationWorkerPolicy(
            @Value("${hd-platform.notification.batch-size:50}") int batchSize,
            @Value("${hd-platform.notification.max-attempts:5}") int maxAttempts,
            @Value("${hd-platform.notification.lock-timeout-seconds:300}") long lockTimeoutSeconds) {
        return new NotificationWorkerPolicy(
                batchSize, maxAttempts, Duration.ofSeconds(lockTimeoutSeconds));
    }
}
