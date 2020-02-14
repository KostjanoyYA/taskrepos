package ru.cft.focusstart.repository.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.cft.focusstart.entity.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    String GET_QUERY =
            "select b " +
            "from Book b join fetch b.author " +
            "where lower(b.name) like lower(concat('%', :name, '%')) " +
            "  and lower(b.author.name) like lower(concat('%', :authorName, '%'))";

    @Query(GET_QUERY)
    List<Book> find(@Param("name") String name, @Param("authorName") String authorName);
}
