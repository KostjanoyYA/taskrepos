package ru.cft.focusstart.repository.author;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cft.focusstart.entity.Author;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<Author> findByNameContainingIgnoreCase(String name);
}
