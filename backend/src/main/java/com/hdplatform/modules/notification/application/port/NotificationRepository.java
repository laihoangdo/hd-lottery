package com.hdplatform.modules.notification.application.port;

import com.hdplatform.modules.notification.domain.aggregate.Notification;

import java.time.Instant;
import java.util.List;

public interface NotificationRepository {
    Notification save(Notification notification);
    List<Notification> claimBatch(int batchSize, Instant now, Instant staleBefore);
}
