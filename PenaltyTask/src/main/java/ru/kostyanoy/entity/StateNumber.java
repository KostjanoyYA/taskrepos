package ru.kostyanoy.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "stateNumber")
public class StateNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "country")
    private String country;

    @Column(name = "regionCode")
    private int regionCode;

    @Column(name = "series")
    private String series;

    @Column(name = "number")
    private int number;
}
