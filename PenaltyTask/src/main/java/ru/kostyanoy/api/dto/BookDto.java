package ru.kostyanoy.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonDeserialize(builder = BookDto.Builder.class)
public class BookDto {

    private final Long id;

    private final String name;

    private final String description;

    private final String isbn;

    private final Long authorId;

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
    }
}
