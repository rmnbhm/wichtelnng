package com.romanboehm.wichtelnng.usecases.registerparticipant;

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
public class RegisterParticipantMailCreatorTest {

    @Autowired
    private RegisterParticipantMailCreator mailCreator;

    @Test
    void shouldHandleToAndFromCorrectly() throws MessagingException {
        RegisterParticipant registration = RegisterParticipant.with(event())
                .setParticipantName("Angus Young")
                .setParticipantEmail("angusyoung@acdc.net");

        MimeMessage mail = mailCreator.createMessage(registration);

        assertThat(mail).isNotNull();
        assertThat(mail.getFrom())
                .extracting(Address::toString)
                .containsExactly("wichteln@romanboehm.com");
        assertThat(mail.getRecipients(TO))
                .extracting(Address::toString)
                .containsExactly("angusyoung@acdc.net");
    }

    @Test
    void shouldHandleDataCorrectly() throws IOException, MessagingException {
        RegisterParticipant registration = RegisterParticipant.with(event())
                .setParticipantName("Angus Young")
                .setParticipantEmail("angusyoung@acdc.net");

        MimeMessage mail = mailCreator.createMessage(registration);

        assertThat(mail).isNotNull();
        assertThat(mail.getSubject()).isEqualTo("You have registered to wichtel at 'AC/DC Secret Santa'");
        MatcherAssert.assertThat(mail.getContent().toString(), stringContainsInOrder(
                "Hey Angus Young,",
                "You have successfully registered to wichtel at 'AC/DC Secret Santa' (https://wichtelnng.romanboehm.com/about)!",
                "You'll be provided with the name of your gift's recipient through an email to angusyoung@acdc.net once everybody has registered.",
                "The gift's monetary value should not exceed AUD 78.50.",
                "Here's what the event's host says about it:",
                "\"There's gonna be some santa'ing\"",
                "If you have any questions, contact the event's host George Young at georgeyoung@acdc.net.",
                "This mail was generated using https://wichtelnng.romanboehm.com"
        ));
    }

}