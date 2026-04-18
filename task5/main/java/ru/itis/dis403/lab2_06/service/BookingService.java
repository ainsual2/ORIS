package ru.itis.dis403.lab2_06.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.dis403.lab2_06.dto.BookingDto;
import ru.itis.dis403.lab2_06.model.Booking;
import ru.itis.dis403.lab2_06.model.User;
import ru.itis.dis403.lab2_06.repository.BookingRepository;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingDto getBookingById(Long id, User user) {
        Booking booking = bookingRepository.findByIdAndHotelId(id, user.getHotel().getId());

        return BookingDto.builder()
                .id(booking.getId())
                .arrivaldate(booking.getArrivaldate())
                .stayingdate(booking.getStayingdate())
                .departuredate(booking.getDeparturedate())
                .personId(booking.getPerson().getId())
                .name(booking.getPerson().getName())
                .build();
    }

    public BookingDto updateBooking(Long id, BookingDto dto, User user) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getHotel().getId().equals(user.getHotel().getId())) {
            throw new RuntimeException("Access denied");
        }

        booking.setArrivaldate(dto.arrivaldate());
        booking.setStayingdate(dto.stayingdate());
        booking.setDeparturedate(dto.departuredate());
        booking.setRoom(dto.room());

        bookingRepository.save(booking);
        return getBookingById(id, user);
    }
}
