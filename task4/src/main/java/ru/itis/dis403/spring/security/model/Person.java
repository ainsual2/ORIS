package ru.itis.dis403.spring.security.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import static jakarta.persistence.InheritanceType.JOINED;

@Entity
@Inheritance(strategy = JOINED)
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    protected String name;

    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "person_phones",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "phone_id")
    )
    protected Set<Phone> phones = new HashSet<>();

    public Set<Phone> getPhones() { return phones; }
    public void setPhones(Set<Phone> phones) { this.phones = phones; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{ id=" + id + ", name='" + name + "', phones=" + phones + "}";
    }
}