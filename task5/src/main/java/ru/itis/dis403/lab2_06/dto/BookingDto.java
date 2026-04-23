package ru.itis.dis403.lab2_06.dto;

import lombok.Builder;

import java.util.Date;

@Builder
public record BookingDto (
    Long id,
    Date arrivaldate,
    Date stayingdate,
    Date departuredate,

    Long personId,
    String name,
    String gender,
    Date birthDate,

    String room
) {
}
