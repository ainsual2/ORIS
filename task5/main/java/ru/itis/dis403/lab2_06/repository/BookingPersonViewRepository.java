package ru.itis.dis403.lab2_06.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.dis403.lab2_06.model.BookingPersonView;

import java.util.List;

@Repository
public interface BookingPersonViewRepository extends JpaRepository<BookingPersonView, Long> {

    List<BookingPersonView> findByHotelId(Long id);
}

