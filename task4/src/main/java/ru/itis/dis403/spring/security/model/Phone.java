package ru.itis.dis403.spring.security.model;


import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "phone_number")
    private String number;

    @ManyToMany(mappedBy = "phones", fetch = FetchType.EAGER)
    private Set<Person> persons = new HashSet<>();

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Set<Person> getPersons() { return persons; }
    public void setPersons(Set<Person> persons) { this.persons = persons; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phone phone = (Phone) o;
        return Objects.equals(id, phone.id) && Objects.equals(number, phone.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number);
    }

    @Override
    public String toString() {
        return "Phone{id=" + id + ", number='" + number + "'}";
    }
}