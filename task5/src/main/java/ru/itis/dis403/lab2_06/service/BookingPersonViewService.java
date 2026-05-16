package ru.itis.dis403.lab2_06.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.dis403.lab2_06.dto.BookingPersonViewDto;
import ru.itis.dis403.lab2_06.model.Booking;
import ru.itis.dis403.lab2_06.model.Hotel;
import ru.itis.dis403.lab2_06.repository.BookingPersonViewRepository;
import ru.itis.dis403.lab2_06.repository.BookingRepository;
import ru.itis.dis403.lab2_06.repository.HotelRepository;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingPersonViewService {

    private final BookingPersonViewRepository repository;
    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;

    public List<BookingPersonViewDto> findByHotelId(Long id) {
        List<BookingPersonViewDto> fromView = repository.findByHotelId(id)
                .stream()
                .map(b ->
                        BookingPersonViewDto.builder()
                                .id(b.getId())
                                .arrivaldate(b.getArrivaldate())
                                .stayingdate(b.getStayingdate())
                                .room(b.getRoom())
                                .name(b.getName())
                                .birthdate(b.getBirthdate())
                                .hotelId(b.getHotelId())
                                .gender(b.getGender())
                                .build()
                ).toList();

        if (!fromView.isEmpty()) {
            return fromView;
        }

        Hotel hotel = hotelRepository.findById(id).orElse(null);
        if (hotel == null) {
            return Collections.emptyList();
        }

        return bookingRepository.findByHotel(hotel)
                .stream()
                .map(this::toDto)
                .toList();
    }

    private BookingPersonViewDto toDto(Booking booking) {
        return BookingPersonViewDto.builder()
                .id(booking.getId())
                .arrivaldate(booking.getArrivaldate())
                .stayingdate(booking.getStayingdate())
                .room(booking.getRoom())
                .name(booking.getPerson() != null ? booking.getPerson().getName() : null)
                .birthdate(booking.getPerson() != null ? booking.getPerson().getBirthdate() : null)
                .hotelId(booking.getHotel() != null ? booking.getHotel().getId() : null)
                .gender(booking.getPerson() != null ? booking.getPerson().getGender() : null)
                .build();
    }
}