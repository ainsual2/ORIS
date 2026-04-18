package ru.itis.dis403.lab2_06.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.itis.dis403.lab2_06.config.SecurityConfig;
import ru.itis.dis403.lab2_06.dto.BookingDto;
import ru.itis.dis403.lab2_06.dto.BookingResponse;
import ru.itis.dis403.lab2_06.model.Booking;
import ru.itis.dis403.lab2_06.repository.BookingRepository;
import ru.itis.dis403.lab2_06.service.BookingService;
import ru.itis.dis403.lab2_06.service.UserDetailsImpl;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingRepository bookingRepository;
    private final BookingService bookingService;

    @GetMapping("/get/{id}")
    public ResponseEntity<BookingDto> getBookingById(
            @PathVariable Long id
    ) {

        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal();

        System.out.println(userDetails.getUser());
        BookingDto booking = bookingService.getBookingById(id, userDetails.getUser());
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/all")
    public ResponseEntity<BookingResponse> getBookings() {
        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal();

        System.out.println(userDetails.getUser());
        List<Booking> bookings = bookingRepository.findByHotel(userDetails.getUser().getHotel());

        bookings.forEach(b-> System.out.println(b.getId()));

        return ResponseEntity.ok(new BookingResponse(bookings));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BookingDto> updateBooking(
            @PathVariable Long id,
            @RequestBody BookingDto bookingDto
    ) {
        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        BookingDto updated = bookingService.updateBooking(id, bookingDto, userDetails.getUser());
        return ResponseEntity.ok(updated);
    }
}
