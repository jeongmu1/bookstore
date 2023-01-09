package books.book.repository;

import books.book.domain.ProductBook;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<ProductBook, Long> {
}
