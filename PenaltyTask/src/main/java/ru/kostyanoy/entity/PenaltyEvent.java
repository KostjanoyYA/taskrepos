package ru.kostyanoy.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "penalty_event")
public class PenaltyEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @OneToOne
    @JoinColumn(name = "fine_id")
    private Fine fine;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;
}
