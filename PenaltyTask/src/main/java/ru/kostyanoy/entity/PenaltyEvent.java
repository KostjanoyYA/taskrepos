package ru.kostyanoy.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "penaltyEvent")
public class PenaltyEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "eventDate")
    private LocalDateTime eventDate;

    @OneToOne
    @JoinColumn(name = "fineID")
    private Fine fine;

    @ManyToOne
    @JoinColumn(name = "carID")
    private Car car;
}
