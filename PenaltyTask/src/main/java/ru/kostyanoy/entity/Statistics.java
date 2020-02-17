package ru.kostyanoy.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Statistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "fine_id")
    private Fine fine;

    @Column(name = "fine_top_place")
    private Long topPlace;

    @Column(name = "fine_occurrences")
    private Long occurrencesNumber;
}



