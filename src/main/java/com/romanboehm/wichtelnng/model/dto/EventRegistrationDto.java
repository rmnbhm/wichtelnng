package com.romanboehm.wichtelnng.model.dto;

import javax.validation.Valid;

public class EventRegistrationDto {
    @Valid
    private EventDto event;
    @Valid
    private ParticipantDto participant;

    public EventRegistrationDto(EventDto event) {
        this.event = event;
    }

    public EventDto getEvent() {
        return event;
    }

    public EventRegistrationDto setEvent(EventDto event) {
        this.event = event;
        return this;
    }

    public ParticipantDto getParticipant() {
        return participant;
    }

    public EventRegistrationDto setParticipant(ParticipantDto participant) {
        this.participant = participant;
        return this;
    }

    @Override
    public String toString() {
        return String.format("%s with %s", event.toString(), participant.toString());
    }
}
