package ru.cft.focusstart.mapper;

import org.springframework.stereotype.Component;
import ru.cft.focusstart.api.dto.BookDto;
import ru.cft.focusstart.entity.Book;

@Component
public class BookMapper {

    public BookDto toDto(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .name(book.getName())
                .description(book.getDescription())
                .isbn(book.getIsbn())
                .authorId(book.getAuthor().getId())
                .build();
    }
}
