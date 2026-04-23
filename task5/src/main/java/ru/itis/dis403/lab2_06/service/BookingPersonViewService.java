package ru.itis.dis403.lab2_06.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.dis403.lab2_06.dto.BookingPersonViewDto;
import ru.itis.dis403.lab2_06.repository.BookingPersonViewRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingPersonViewService {

    private final BookingPersonViewRepository repository;

    public List<BookingPersonViewDto> findByHotelId(Long id) {
        return repository.findByHotelId(id)
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
    }

}
