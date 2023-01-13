package books.product.repository;

import books.product.domain.ProductBook;
import org.springframework.data.repository.CrudRepository;

public interface ProductBookRepository extends CrudRepository<ProductBook, Long> {

}
