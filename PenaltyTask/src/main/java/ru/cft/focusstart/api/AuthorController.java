package ru.cft.focusstart.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.cft.focusstart.api.dto.AuthorDto;
import ru.cft.focusstart.api.dto.BookDto;
import ru.cft.focusstart.service.author.AuthorService;

import java.util.List;

@RestController
@RequestMapping(path = Paths.AUTHORS, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping
    public AuthorDto create(@RequestBody AuthorDto authorDto) {
        return authorService.create(authorDto);
    }

    @GetMapping(path = Paths.ID)
    public AuthorDto getById(@PathVariable(name = Parameters.ID) Long id) {
        return authorService.getById(id);
    }

    @GetMapping
    public List<AuthorDto> get(@RequestParam(name = Parameters.NAME, required = false) String name) {
        return authorService.get(name);
    }

    @GetMapping(path = Paths.ID + Paths.BOOKS)
    public List<BookDto> getBooks(@PathVariable(name = Parameters.ID) Long id) {
        return authorService.getBooks(id);
    }

    @PutMapping(path = Paths.ID)
    public AuthorDto merge(@PathVariable(name = Parameters.ID) Long id, @RequestBody AuthorDto authorDto) {
        return authorService.merge(id, authorDto);
    }
}
