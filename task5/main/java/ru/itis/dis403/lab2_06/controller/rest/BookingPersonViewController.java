package ru.itis.dis403.lab2_06.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.dis403.lab2_06.dto.BookingPersonViewDto;
import ru.itis.dis403.lab2_06.dto.BookingViewResponse;
import ru.itis.dis403.lab2_06.service.BookingPersonViewService;
import ru.itis.dis403.lab2_06.service.UserDetailsImpl;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingPersonViewController {

    private final BookingPersonViewService bookingPersonViewService;

    @GetMapping("/allview")
    public ResponseEntity<BookingViewResponse> getBooking() {

        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<BookingPersonViewDto> bookings = bookingPersonViewService.findByHotelId(userDetails.getUser().getHotel().getId());

        bookings.forEach(b-> System.out.println(b.id()));

        return ResponseEntity.ok(new BookingViewResponse(bookings));
    }
}
