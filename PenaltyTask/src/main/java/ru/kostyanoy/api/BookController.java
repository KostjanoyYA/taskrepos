package ru.kostyanoy.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.kostyanoy.api.dto.BookDto;
import ru.kostyanoy.service.book.BookService;

import java.util.List;

@RestController
@RequestMapping(path = Paths.BOOKS, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public BookDto create(@RequestBody BookDto bookDto) {
        return bookService.create(bookDto);
    }

    @GetMapping(path = Paths.ID)
    public BookDto getById(@PathVariable(name = Parameters.ID) Long id) {
        return bookService.getById(id);
    }

    @GetMapping
    public List<BookDto> get(
            @RequestParam(name = Parameters.NAME, required = false) String name,
            @RequestParam(name = Parameters.AUTHOR_NAME, required = false) String authorName
    ) {
        return bookService.get(name, authorName);
    }

    @PutMapping(path = Paths.ID)
    public BookDto merge(@PathVariable(name = Parameters.ID) Long id, @RequestBody BookDto bookDto) {
        return bookService.merge(id, bookDto);
    }

    @DeleteMapping(path = Paths.ID)
    public void delete(@PathVariable(name = Parameters.ID) Long id) {
        bookService.delete(id);
    }
}
