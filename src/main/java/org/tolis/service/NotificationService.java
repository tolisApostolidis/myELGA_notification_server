package org.tolis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import org.tolis.event.ApplicationNotificationEvent;

@Service
public class NotificationService {

    @Autowired
    private EmailService emailService;

    public void processNotification(ApplicationNotificationEvent event) throws MessagingException {
        emailService.sendApplicationEmail(event);
    }
}
