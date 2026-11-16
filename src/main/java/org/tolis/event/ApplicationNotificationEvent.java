package org.tolis.event;

import java.time.Instant;
import java.util.UUID;

public record ApplicationNotificationEvent(
        UUID eventId,
        Instant dateOfCreation,
        Long applicationId,
        ApplicationEventType eventType,
        String userEmail,
        String fullName
) {
}
