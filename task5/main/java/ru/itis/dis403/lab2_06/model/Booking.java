package ru.itis.dis403.lab2_06.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@ToString(exclude = {"hotel", "person"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date arrivaldate;

    @Temporal(TemporalType.DATE)
    private Date stayingdate;

    private String room;

    @Temporal(TemporalType.DATE)
    private Date departuredate;

    @ManyToOne
    private Hotel hotel;

    @ManyToOne(cascade = CascadeType.ALL)
    private Person person;

}
