package ru.kostyanoy.service.author;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kostyanoy.api.dto.AuthorDto;
import ru.kostyanoy.api.dto.BookDto;
import ru.kostyanoy.entity.CarOwner;
import ru.kostyanoy.exception.ObjectNotFoundException;
import ru.kostyanoy.mapper.ReportMapper;
import ru.kostyanoy.mapper.StatisticsMapper;
import ru.kostyanoy.repository.author.AuthorRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.kostyanoy.service.validation.Validator.*;

@Service
@RequiredArgsConstructor
public class DefaultAuthorService implements AuthorService {

    private final AuthorRepository authorRepository;

    private final StatisticsMapper statisticsMapper;

    private final ReportMapper reportMapper;

    @Override
    @Transactional
    public AuthorDto create(AuthorDto authorDto) {
        validate(authorDto);

        CarOwner carOwner = add(null, authorDto);

        return statisticsMapper.toDto(carOwner);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorDto getById(Long id) {
        checkNotNull("id", id);

        CarOwner carOwner = getAuthor(id);

        return statisticsMapper.toDto(carOwner);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorDto> get(String name) {
        return authorRepository.findByNameContainingIgnoreCase(name == null ? "" : name)
                .stream()
                .map(statisticsMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> getBooks(Long id) {
        checkNotNull("id", id);

        return Optional.ofNullable(getAuthor(id).getBooks())
                .orElse(Collections.emptyList())
                .stream()
                .map(reportMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AuthorDto merge(Long id, AuthorDto authorDto) {
        checkNotNull("id", id);
        validate(authorDto);

        CarOwner carOwner = authorRepository.findById(id)
                .map(existing -> update(existing, authorDto))
                .orElseGet(() -> add(id, authorDto));

        return statisticsMapper.toDto(carOwner);
    }

    private void validate(AuthorDto authorDto) {
        checkNull("author.id", authorDto.getId());
        checkSize("author.name", authorDto.getName(), 1, 256);
        checkSize("author.description", authorDto.getDescription(), 1, 4096);
    }

    private CarOwner add(Long id, AuthorDto authorDto) {
        CarOwner carOwner = new CarOwner();
        carOwner.setId(id);
        carOwner.setName(authorDto.getName());
        carOwner.setDescription(authorDto.getDescription());

        return authorRepository.save(carOwner);
    }

    private CarOwner getAuthor(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("CarOwner with id %s not found", id)));
    }

    private CarOwner update(CarOwner carOwner, AuthorDto authorDto) {
        carOwner.setName(authorDto.getName());
        carOwner.setDescription(authorDto.getDescription());

        return carOwner;
    }
}
