package ru.itis.dis403.lab2_06.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.dis403.lab2_06.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

}
