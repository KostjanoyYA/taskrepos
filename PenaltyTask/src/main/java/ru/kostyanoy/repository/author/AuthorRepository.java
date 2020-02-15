package ru.kostyanoy.repository.author;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kostyanoy.entity.CarOwner;

import java.util.List;

public interface AuthorRepository extends JpaRepository<CarOwner, Long> {

    List<CarOwner> findByNameContainingIgnoreCase(String name);
}
