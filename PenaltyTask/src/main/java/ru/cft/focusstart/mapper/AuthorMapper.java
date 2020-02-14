package ru.cft.focusstart.mapper;

import org.springframework.stereotype.Component;
import ru.cft.focusstart.api.dto.AuthorDto;
import ru.cft.focusstart.entity.Author;

@Component
public class AuthorMapper {

    public AuthorDto toDto(Author author) {
        return AuthorDto.builder()
                .id(author.getId())
                .name(author.getName())
                .description(author.getDescription())
                .build();
    }
}
