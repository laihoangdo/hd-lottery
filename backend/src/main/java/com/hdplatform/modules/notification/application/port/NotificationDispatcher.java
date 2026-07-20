package com.hdplatform.modules.notification.application.port;

import com.hdplatform.modules.notification.domain.aggregate.Notification;
import com.hdplatform.modules.notification.domain.valueobject.NotificationChannel;

public interface NotificationDispatcher {
    NotificationChannel channel();
    void send(Notification notification) throws Exception;
}
