package com.romanboehm.wichtelnng.service;

import com.romanboehm.wichtelnng.model.entity.Event;
import com.romanboehm.wichtelnng.model.entity.Participant;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

import static com.romanboehm.wichtelnng.TestData.event;
import static javax.mail.Message.RecipientType.TO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.stringContainsInOrder;

@SpringBootTest(properties = {
        "com.romanboehm.wichtelnng.domain=https://wichtelnng.romanboehm.com",
        "com.romanboehm.wichtelnng.mail.from=wichteln@romanboehm.com",
})
public class HostMailCreatorTest {

    @Autowired
    private HostMailCreator hostMailCreator;

    @Test
    public void shouldHandleToAndFromCorrectly() throws MessagingException {
        Event event = event()
                .addParticipant(
                        new Participant()
                                .setName("Angus Young")
                                .setEmail("angusyoung@acdc.net")
                );

        MimeMessage mail = hostMailCreator.createMessage(event);

        assertThat(mail).isNotNull();
        assertThat(mail.getFrom())
                .extracting(Address::toString)
                .containsExactly("wichteln@romanboehm.com");
        assertThat(mail.getRecipients(TO))
                .extracting(Address::toString)
                .containsExactly("georgeyoung@acdc.net");
    }

    @Test
    public void shouldHandleDataCorrectly() throws IOException, MessagingException {
        Event event = event()
                .addParticipant(
                        new Participant()
                                .setName("Angus Young")
                                .setEmail("angusyoung@acdc.net")
                );

        MimeMessage mail = hostMailCreator.createMessage(event);

        assertThat(mail).isNotNull();
        assertThat(mail.getSubject()).isEqualTo("Unfortunately, nobody has registered for 'AC/DC Secret Santa'");
        MatcherAssert.assertThat(mail.getContent().toString(), stringContainsInOrder(
                "Hey George Young,",
                "Unfortunately nobody has registered to wichtel at 'AC/DC Secret Santa'.",
                "Try creating a new event: https://wichtelnng.romanboehm.com!",
                "This mail was generated using https://wichtelnng.romanboehm.com"
        ));
    }

}