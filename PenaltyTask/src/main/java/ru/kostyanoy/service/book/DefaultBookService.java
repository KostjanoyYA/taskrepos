package ru.kostyanoy.service.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kostyanoy.api.dto.BookDto;
import ru.kostyanoy.entity.Car;
import ru.kostyanoy.entity.CarOwner;
import ru.kostyanoy.exception.InvalidParametersException;
import ru.kostyanoy.exception.ObjectNotFoundException;
import ru.kostyanoy.mapper.ReportMapper;
import ru.kostyanoy.repository.author.AuthorRepository;
import ru.kostyanoy.repository.book.BookRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.kostyanoy.service.validation.Validator.*;

@Service
@RequiredArgsConstructor
public class DefaultBookService implements BookService {

    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    private final ReportMapper reportMapper;

    @Override
    @Transactional
    public BookDto create(BookDto bookDto) {
        validate(bookDto);

        Car car = add(null, bookDto);

        return reportMapper.toDto(car);
    }

    @Override
    @Transactional(readOnly = true)
    public BookDto getById(Long id) {
        checkNotNull("id", id);

        Car car = getBook(id);

        return reportMapper.toDto(car);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> get(String name, String authorName) {
        return bookRepository.find(name == null ? "" : name, authorName == null ? "" : authorName)
                .stream()
                .map(reportMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookDto merge(Long id, BookDto bookDto) {
        checkNotNull("id", id);
        validate(bookDto);

        Car car = bookRepository.findById(id)
                .map(existing -> update(existing, bookDto))
                .orElseGet(() -> add(id, bookDto));

        return reportMapper.toDto(car);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        checkNotNull("id", id);

        Car car = getBook(id);

        bookRepository.delete(car);
    }

    private void validate(BookDto bookDto) {
        checkNull("book.id", bookDto.getId());
        checkSize("book.name", bookDto.getName(), 1, 256);
        checkSize("book.description", bookDto.getDescription(), 1, 4096);
        checkSize("book.isbn", bookDto.getIsbn(), 1, 64);
        checkNotNull("book.authorId", bookDto.getAuthorId());
    }

    private Car add(Long id, BookDto bookDto) {
        CarOwner carOwner = getAuthor(bookDto.getAuthorId());

        Car car = new Car();
        car.setId(id);
        car.setName(bookDto.getName());
        car.setDescription(bookDto.getDescription());
        car.setIsbn(bookDto.getIsbn());
        car.setAuthor(carOwner);

        return bookRepository.save(car);
    }

    private CarOwner getAuthor(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new InvalidParametersException(String.format("CarOwner with id %s not found", id)));
    }

    private Car getBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Car with id %s not found", id)));
    }

    private Car update(Car car, BookDto bookDto) {
        car.setName(bookDto.getName());
        car.setDescription(bookDto.getDescription());
        car.setIsbn(bookDto.getIsbn());
        if (!bookDto.getAuthorId().equals(car.getAuthor().getId())) {
            CarOwner newCarOwner = getAuthor(bookDto.getAuthorId());
            car.setAuthor(newCarOwner);
        }

        return car;
    }
}
