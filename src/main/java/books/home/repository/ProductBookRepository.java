package books.home.repository;

import books.book.domain.ProductBook;
import org.springframework.data.repository.CrudRepository;

public interface ProductBookRepository extends CrudRepository<ProductBook, Long> {
}