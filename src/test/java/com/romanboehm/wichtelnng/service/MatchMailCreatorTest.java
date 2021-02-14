package com.romanboehm.wichtelnng.service;

import com.romanboehm.wichtelnng.TestData;
import com.romanboehm.wichtelnng.model.Donor;
import com.romanboehm.wichtelnng.model.Match;
import com.romanboehm.wichtelnng.model.Recipient;
import com.romanboehm.wichtelnng.model.entity.Event;
import com.romanboehm.wichtelnng.model.entity.Participant;
import org.assertj.core.api.Assertions;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

@SpringBootTest
public class MatchMailCreatorTest {

    @Autowired
    private MatchMailCreator matchMailCreator;

    @Test
    public void shouldHandleToAndFromCorrectly() throws MessagingException {
        Participant angusYoung = new Participant()
                .setName("Angus Young")
                .setEmail("angusyoung@acdc.net");
        Participant malcolmYoung = new Participant()
                .setName("Malcolm Young")
                .setEmail("malcolmyoung@acdc.net");
        Event event = TestData.event().entity()
                .addParticipant(angusYoung)
                .addParticipant(malcolmYoung);
        Match angusGiftsToMalcolm = new Match(
                new Donor(angusYoung),
                new Recipient(malcolmYoung)
        );

        MimeMessage mail = matchMailCreator.createMessage(event, angusGiftsToMalcolm);

        Assertions.assertThat(mail).isNotNull();
        Assertions.assertThat(mail.getRecipients(Message.RecipientType.TO))
                .extracting(Address::toString)
                .containsExactly("angusyoung@acdc.net");
    }

    @Test
    public void shouldHandleDataCorrectly() throws IOException, MessagingException {
        Participant angusYoung = new Participant()
                .setName("Angus Young")
                .setEmail("angusyoung@acdc.net");
        Participant malcolmYoung = new Participant()
                .setName("Malcolm Young")
                .setEmail("malcolmyoung@acdc.net");
        Event event = TestData.event().entity()
                .addParticipant(angusYoung)
                .addParticipant(malcolmYoung);
        Match angusGiftsToMalcolm = new Match(
                new Donor(angusYoung),
                new Recipient(malcolmYoung)
        );

        MimeMessage mail = matchMailCreator.createMessage(event, angusGiftsToMalcolm);

        Assertions.assertThat(mail).isNotNull();
        Assertions.assertThat(mail.getSubject()).isEqualTo("You have been matched to wichtel at 'AC/DC Secret Santa'");
        MatcherAssert.assertThat(mail.getContent().toString(), Matchers.stringContainsInOrder(
                "Hey Angus Young,",
                "You registered to wichtel at 'AC/DC Secret Santa' (https://wichtelnng.romanboehm.com/about)!",
                "We matched the event's participants and you're therefore now asked to give a gift to Malcolm Young. The gift's monetary value should not exceed AUD 78.50.",
                "Here's what the event's host says about it:",
                "\"There's gonna be some santa'ing\"",
                "If you have any questions, contact the event's host George Young at georgeyoung@acdc.net.",
                "This mail was generated using https://wichteln.romanboehm.com"
        ));
    }

}