package ru.itis.dis403.lab2_06.dto;

import lombok.Builder;

import java.util.Date;

@Builder
public record BookingPersonViewDto(
        Long id,
        Date arrivaldate,
        Date stayingdate,
        Date departuredate,
        Long hotelId,
        String name,
        String gender,
        Date birthdate,
        String room
) {
}
