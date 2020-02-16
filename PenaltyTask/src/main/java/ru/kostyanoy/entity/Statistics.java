package ru.kostyanoy.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Data
@Entity
public class Statistics { //TODO добавить первичный ключ https://easyjava.ru/data/jpa/pervichnye-klyuchi-v-jpa/

    @OneToOne
    @JoinColumn(name = "fine_id")
    private Fine fine;

    @Column(name = "model")
    private Long topPlace;

    @Column(name = "model")
    private Long occurrencesNumber;
}



