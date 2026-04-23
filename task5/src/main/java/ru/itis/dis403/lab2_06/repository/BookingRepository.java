package ru.itis.dis403.lab2_06.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.itis.dis403.lab2_06.model.Booking;
import ru.itis.dis403.lab2_06.model.Hotel;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByHotel(Hotel hotel);

    @Query("select b from Booking b where b.id = :id and b.hotel.id = :hotelId")
    Booking findByIdAndHotelId(Long id, Long hotelId);
}
