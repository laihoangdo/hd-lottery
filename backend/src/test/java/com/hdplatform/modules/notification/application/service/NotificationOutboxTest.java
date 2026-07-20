package com.hdplatform.modules.notification.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdplatform.modules.notification.application.command.EnqueueNotificationCommand;
import com.hdplatform.modules.notification.application.port.NotificationRepository;
import com.hdplatform.modules.notification.domain.aggregate.Notification;
import com.hdplatform.modules.notification.domain.valueobject.NotificationChannel;
import com.hdplatform.modules.notification.domain.valueobject.NotificationStatus;
import com.hdplatform.modules.tenant.application.context.TenantContext;
import com.hdplatform.modules.tenant.application.context.TenantContextHolder;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import com.hdplatform.modules.tenant.domain.valueobject.SiteKey;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NotificationOutboxTest {
    private final NotificationRepository repository = mock(NotificationRepository.class);

    @AfterEach void clearTenant() { TenantContextHolder.clear(); }

    @Test
    void enqueues_notification_owned_by_current_tenant() {
        TenantId tenantId = TenantId.newId();
        TenantContextHolder.set(new TenantContext(tenantId, SiteKey.of("dealer-one")));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        NotificationOutbox outbox = new NotificationOutbox(
                repository, new ObjectMapper(),
                () -> Instant.parse("2026-07-20T00:00:00Z"));

        Notification notification = outbox.enqueue(new EnqueueNotificationCommand(
                NotificationChannel.EMAIL, "user@example.vn", "welcome",
                Map.of("name", "Customer")));

        assertThat(notification.getTenantId()).isEqualTo(tenantId);
        assertThat(notification.getStatus()).isEqualTo(NotificationStatus.PENDING);
        assertThat(notification.getPayloadJson()).contains("Customer");
    }
}
