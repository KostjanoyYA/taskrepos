package ru.cft.focusstart.service.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cft.focusstart.api.dto.BookDto;
import ru.cft.focusstart.entity.Author;
import ru.cft.focusstart.entity.Book;
import ru.cft.focusstart.exception.InvalidParametersException;
import ru.cft.focusstart.exception.ObjectNotFoundException;
import ru.cft.focusstart.mapper.BookMapper;
import ru.cft.focusstart.repository.author.AuthorRepository;
import ru.cft.focusstart.repository.book.BookRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.cft.focusstart.service.validation.Validator.*;

@Service
@RequiredArgsConstructor
public class DefaultBookService implements BookService {

    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Override
    @Transactional
    public BookDto create(BookDto bookDto) {
        validate(bookDto);

        Book book = add(null, bookDto);

        return bookMapper.toDto(book);
    }

    @Override
    @Transactional(readOnly = true)
    public BookDto getById(Long id) {
        checkNotNull("id", id);

        Book book = getBook(id);

        return bookMapper.toDto(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> get(String name, String authorName) {
        return bookRepository.find(name == null ? "" : name, authorName == null ? "" : authorName)
                .stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookDto merge(Long id, BookDto bookDto) {
        checkNotNull("id", id);
        validate(bookDto);

        Book book = bookRepository.findById(id)
                .map(existing -> update(existing, bookDto))
                .orElseGet(() -> add(id, bookDto));

        return bookMapper.toDto(book);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        checkNotNull("id", id);

        Book book = getBook(id);

        bookRepository.delete(book);
    }

    private void validate(BookDto bookDto) {
        checkNull("book.id", bookDto.getId());
        checkSize("book.name", bookDto.getName(), 1, 256);
        checkSize("book.description", bookDto.getDescription(), 1, 4096);
        checkSize("book.isbn", bookDto.getIsbn(), 1, 64);
        checkNotNull("book.authorId", bookDto.getAuthorId());
    }

    private Book add(Long id, BookDto bookDto) {
        Author author = getAuthor(bookDto.getAuthorId());

        Book book = new Book();
        book.setId(id);
        book.setName(bookDto.getName());
        book.setDescription(bookDto.getDescription());
        book.setIsbn(bookDto.getIsbn());
        book.setAuthor(author);

        return bookRepository.save(book);
    }

    private Author getAuthor(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new InvalidParametersException(String.format("Author with id %s not found", id)));
    }

    private Book getBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Book with id %s not found", id)));
    }

    private Book update(Book book, BookDto bookDto) {
        book.setName(bookDto.getName());
        book.setDescription(bookDto.getDescription());
        book.setIsbn(bookDto.getIsbn());
        if (!bookDto.getAuthorId().equals(book.getAuthor().getId())) {
            Author newAuthor = getAuthor(bookDto.getAuthorId());
            book.setAuthor(newAuthor);
        }

        return book;
    }
}
