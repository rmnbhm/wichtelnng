package com.romanboehm.wichtelnng.usecases.matchandnotify;

import com.romanboehm.wichtelnng.data.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Component
public class LostEventMailCreator {

    private final String domain;
    private final String from;
    private final JavaMailSender mailSender;

    public LostEventMailCreator(
            @Value("${com.romanboehm.wichtelnng.domain}") String domain,
            @Value("${com.romanboehm.wichtelnng.mail.from}") String from,
            JavaMailSender mailSender
    ) {
        this.domain = domain;
        this.from = from;
        this.mailSender = mailSender;
    }

    MimeMessage createMessage(Event event) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, UTF_8.toString());
            message.setSubject(format("Unfortunately, nobody has registered for '%s'", event.getTitle()));
            message.setFrom(from);
            message.setTo(event.getHost().getEmail());

            message.setText(format(
                    "Hey %s,%nUnfortunately nobody has registered to wichtel at '%s'.%nTry creating a new event: %s!%nThis mail was generated using %s",
                    event.getHost().getName(),
                    event.getTitle(),
                    domain,
                    domain
            ));

            log.debug("Created mail to inform about empty event: {}", event);
            return mimeMessage;
        } catch (MessagingException e) {
            log.error("Failed to create mail for {}", event, e);
            // Re-throw as `RuntimeException` to be handled by upstream by `ErrorController`
            throw new RuntimeException("Failed to create lost event mail");
        }
    }
}
