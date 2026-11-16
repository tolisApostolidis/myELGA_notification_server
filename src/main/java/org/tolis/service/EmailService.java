package org.tolis.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;
import org.tolis.event.ApplicationNotificationEvent;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Value("${myelga.frontend.login-url}")
    private String frontendLoginUrl;

    public void sendApplicationEmail(ApplicationNotificationEvent event) throws MessagingException {
        String template;

        switch (event.eventType()) {
            case APPLICATION_ACCEPTED -> {
                template = "email/application_accepted";
            }
            case APPLICATION_REJECTED -> {
                template = "email/application_rejected";
            }
            default -> throw new IllegalArgumentException("Unsupported event type: " + event.eventType());
        }

        Context context = new Context();
        context.setVariable("fullName", event.fullName());
        context.setVariable("applicationId", event.applicationId());
        context.setVariable("loginUrl", frontendLoginUrl);

        String html = templateEngine.process(template, context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

        helper.setFrom(senderEmail);
        helper.setTo(event.userEmail());
        helper.setSubject("Η αίτηση με αριθμό " + event.applicationId() + " έχει αλλάξει κατάσταση");
        helper.setText(html, true);

        mailSender.send(message);

        logger.info("Email successfully submitted to SMTP server for application: {}", event.applicationId());
    }
}
