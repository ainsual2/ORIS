package ru.itis.dis403.spring.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.dis403.spring.security.model.Person;


public interface PersonRepository extends JpaRepository<Person, Long> {
}