package ru.kostyanoy.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
//@Entity
public class Statistics {

    private Long id;

    private Fine fine;

    private BigDecimal charge;
}
