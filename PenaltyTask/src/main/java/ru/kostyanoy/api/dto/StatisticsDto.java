package ru.kostyanoy.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonDeserialize(builder = StatisticsDto.Builder.class)
public class StatisticsDto {

    private final Long fineID;

    private final String fineType;

    private final Long topPlace;

    private final Long occurrencesNumber;

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
    }
}
