package com.romanboehm.wichtelnng;

import com.romanboehm.wichtelnng.data.*;
import com.romanboehm.wichtelnng.usecases.createevent.CreateEvent;
import com.romanboehm.wichtelnng.usecases.registerparticipant.RegisterParticipant;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.money.Monetary;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static java.time.Month.JUNE;
import static java.time.ZoneId.systemDefault;
import static javax.money.Monetary.getCurrency;

public class TestData {

    private static final ZoneId ZONE_ID = systemDefault();

    public static Event event() {
        return new Event()
                .setTitle("AC/DC Secret Santa")
                .setDescription("There's gonna be some santa'ing")
                .setMonetaryAmount(monetaryAmount())
                .setDeadline(deadline())
                .setHost(host());
    }

    public static String zoneId() {
        return ZONE_ID.getId();
    }

    public static Deadline deadline() {
        return new Deadline()
                .setZoneId(ZoneId.of("Australia/Sydney").getId())
                .setLocalDateTime(
                        LocalDateTime.of(
                                LocalDate.of(2666, JUNE, 7),
                                LocalTime.of(6, 6)
                        )
                );
    }

    public static Host host() {
        return new Host()
                .setName("George Young")
                .setEmail("georgeyoung@acdc.net");
    }

    public static MonetaryAmount monetaryAmount() {
        return new MonetaryAmount()
                .setCurrency(getCurrency("AUD").getCurrencyCode())
                .setNumber(BigDecimal.valueOf(78.50));
    }

    public static Participant participant() {
        return new Participant()
                .setName("Angus Young")
                .setEmail("angusyoung@acdc.net");
    }

    public static CreateEvent createEvent() {
        Event entity = event();
        return new CreateEvent()
                .setId(entity.getId())
                .setTitle(entity.getTitle())
                .setDescription(entity.getDescription())
                .setCurrency(Monetary.getCurrency(entity.getMonetaryAmount().getCurrency()))
                .setNumber(entity.getMonetaryAmount().getNumber())
                .setLocalDate(entity.getDeadline().getLocalDateTime().atZone(ZoneId.of(entity.getDeadline().getZoneId())).toLocalDate())
                .setLocalTime(entity.getDeadline().getLocalDateTime().atZone(ZoneId.of(entity.getDeadline().getZoneId())).toLocalTime())
                .setTimezone(ZoneId.of(entity.getDeadline().getZoneId()))
                .setHostName(entity.getHost().getName())
                .setHostEmail(entity.getHost().getEmail());
    }

    public static MultiValueMap<String, String> eventFormParams() {
        CreateEvent createEvent = createEvent();
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("title", createEvent.getTitle());
        map.add("description", createEvent.getDescription());
        map.add("number", createEvent.getNumber().toString());
        map.add("currency", createEvent.getCurrency().toString());
        map.add("localDate", createEvent.getLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        map.add("localTime", createEvent.getLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        map.add("timezone", createEvent.getTimezone().toString());
        map.add("hostName", createEvent.getHostName());
        map.add("hostEmail", createEvent.getHostEmail());
        return map;
    }

    public static RegisterParticipant participantRegistration() {
        Participant participant = participant();
        return RegisterParticipant.with(event())
                .setParticipantName(participant.getName())
                .setParticipantEmail(participant.getEmail());
    }

    public static MultiValueMap<String, String> participantFormParams() {
        Participant participant = participant();
        MultiValueMap<String, String> participantFormParams = new LinkedMultiValueMap<>();
        participantFormParams.add("participantName", participant.getName());
        participantFormParams.add("participantEmail", participant.getEmail());
        return participantFormParams;
    }
}
