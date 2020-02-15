package ru.kostyanoy.repository.carowner;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kostyanoy.entity.CarOwner;

import java.util.List;

public interface CarOwnerRepository extends JpaRepository<CarOwner, Long> {

    List<CarOwner> findByNameContainingIgnoreCase(String name);
}
