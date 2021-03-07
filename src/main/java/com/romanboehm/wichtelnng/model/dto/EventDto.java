package com.romanboehm.wichtelnng.model.dto;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@Valid
public class EventDto {

    // May be `null` first
    private UUID id;

    @NotBlank
    @Size(max = 100)
    private String title;

    @NotBlank
    @Size(max = 1000)
    private String description;

    @NotNull
    @Valid
    private MonetaryAmountDto monetaryAmount;

    // Keep date and time apart since we replaced `input[@type='datetime-local']` with two separate `inputs` for reasons
    // of browser compatibility and ease of use.
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate localDate;

    // Keep date and time apart since we replaced `input[@type='datetime-local']` with two separate `inputs` for reasons
    // of browser compatibility and ease of use.
    @NotNull
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime localTime;

    @NotNull
    private ZoneId timezone;

    @NotNull
    @Valid
    private HostDto host;

    public UUID getId() {
        return id;
    }

    public EventDto setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public EventDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public EventDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public MonetaryAmountDto getMonetaryAmount() {
        return monetaryAmount;
    }

    public EventDto setMonetaryAmount(MonetaryAmountDto monetaryAmount) {
        this.monetaryAmount = monetaryAmount;
        return this;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public EventDto setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
        return this;
    }

    public LocalTime getLocalTime() {
        return localTime;
    }

    public EventDto setLocalTime(LocalTime localTime) {
        this.localTime = localTime;
        return this;
    }

    public ZoneId getTimezone() {
        return timezone;
    }

    public EventDto setTimezone(ZoneId timezone) {
        this.timezone = timezone;
        return this;
    }

    public HostDto getHost() {
        return host;
    }

    public EventDto setHost(HostDto host) {
        this.host = host;
        return this;
    }

    // Needed to delegate validation for event's "when" (its local date and local time at the respective timezone) to
    // the javax validator.
    // Non-nullability of the date, time, and timezone components is validated separately through field annotations.
    @FutureOrPresent
    public ZonedDateTime getZonedDateTime() {
        return ZonedDateTime.of(
                localDate != null ? localDate : LocalDate.now(),
                localTime != null ? localTime : LocalTime.now(),
                timezone != null ? timezone : ZoneId.systemDefault()
        );
    }

    public String toString() {
        return String.format(
                "EventDto(title=%s, description=%s, monetaryAmount=%s, localDate=%s, localTime=%s, timezone=%s, host=%s)",
                this.getTitle(),
                this.getDescription(),
                this.getMonetaryAmount(),
                this.getLocalDate(),
                this.getLocalTime(),
                this.getTimezone(),
                this.getHost()
        );
    }

}
