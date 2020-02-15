package ru.kostyanoy.entity;

import lombok.Data;

@Data
//@Entity
public class Statistics {

    //@OneToOne
    //@JoinColumn(name = "fineID")
    private Fine fine;

    private Long topPlace;

    private Long occurrencesNumber;
}



