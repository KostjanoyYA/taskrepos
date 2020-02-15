package ru.kostyanoy.repository.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kostyanoy.entity.Car;

import java.util.List;

public interface BookRepository extends JpaRepository<Car, Long> {

    String GET_QUERY =
            "select b " +
            "from Car b join fetch b.author " +
            "where lower(b.name) like lower(concat('%', :name, '%')) " +
            "  and lower(b.author.name) like lower(concat('%', :authorName, '%'))";

    @Query(GET_QUERY)
    List<Car> find(@Param("name") String name, @Param("authorName") String authorName);
}
