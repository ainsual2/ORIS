package ru.itis.dis403.lab2_06.dto;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.itis.dis403.lab2_06.model.Booking;

import java.util.List;

@AllArgsConstructor
public class BookingResponse {

    private List<Booking> bookings;

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
