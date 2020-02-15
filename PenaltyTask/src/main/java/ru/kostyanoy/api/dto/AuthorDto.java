package ru.kostyanoy.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonDeserialize(builder = AuthorDto.Builder.class)
public class AuthorDto {

    private final Long id;

    private final String name;

    private final String description;

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
    }
}
