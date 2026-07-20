package com.hdplatform.modules.notification.domain.aggregate;

import com.hdplatform.shared.domain.identifier.UUIDIdentifier;

import java.util.UUID;

public final class NotificationId extends UUIDIdentifier {
    private NotificationId(UUID value) { super(value); }
    public static NotificationId newId() { return new NotificationId(UUID.randomUUID()); }
    public static NotificationId of(UUID value) { return new NotificationId(value); }
}
