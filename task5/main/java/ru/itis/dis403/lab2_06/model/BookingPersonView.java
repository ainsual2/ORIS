package ru.itis.dis403.lab2_06.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "view_booking_person")
public class BookingPersonView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date arrivaldate;

    @Temporal(TemporalType.DATE)
    private Date stayingdate;

    @Column(name = "hotel_id")
    private Long hotelId;

    private String name;

    private String gender;

    @Temporal(TemporalType.DATE)
    private Date birthdate;

    private String room;
}
