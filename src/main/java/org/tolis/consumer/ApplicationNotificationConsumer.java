package org.tolis.consumer;

import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tolis.event.ApplicationNotificationEvent;
import org.tolis.service.NotificationService;

@Component
public class ApplicationNotificationConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationNotificationConsumer.class);

    @Autowired
    private NotificationService notificationService;

    @RabbitListener(queues = "${rabbitmq.notification.queue}")
    public void consume(ApplicationNotificationEvent event) throws MessagingException {
        logger.info("Received notification event: {} for application: {}", event.eventId(), event.applicationId());

        notificationService.processNotification(event);

        logger.info("Processing event: {} for application: {}", event.eventId(), event.applicationId());
    }
}
