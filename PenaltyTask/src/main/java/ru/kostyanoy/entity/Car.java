package ru.kostyanoy.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "car")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "make")
    private String make;

    @Column(name = "model")
    private String model;

    @OneToOne
    @JoinColumn(name = "state_number_id")
    private StateNumber stateNumber;

    @ManyToOne
    @JoinColumn(name = "car_owner_id")
    private CarOwner carOwner;
}
