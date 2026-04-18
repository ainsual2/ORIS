package ru.itis.dis403.lab2_06.dto;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class BookingViewResponse {
    private List<BookingPersonViewDto> bookings;

    public List<BookingPersonViewDto> getBookings() {
        return bookings;
    }

    public void setBookings(List<BookingPersonViewDto> bookings) {
        this.bookings = bookings;
    }
}
