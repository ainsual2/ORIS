package ru.itis.dis403.lab2_06.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.dis403.lab2_06.model.Hotel;

import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    Optional<Hotel> findByName(String name);
}