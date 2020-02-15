package ru.kostyanoy.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonDeserialize(builder = ReportDto.Builder.class)
public class ReportDto {

    private final Long penaltyEventID;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime penaltyEventTimeStamp;

    private final String fineType;

    private final BigDecimal fineCharge;

    private final String carMake;

    private final String carModel;

    private final String fullStateNumber;

    private final String firstName;

    private final String middleName;

    private final String lastName;

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
    }
    //TODO Если дата будет сериализоваться неправильно, то надо в билдере указать аннотацию, как на penaltyEventTimeStamp
}
