package books.product.repository;

import books.product.domain.ProductBook;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductBookRepository extends JpaRepository<ProductBook, Long> {

    ProductBook findProductBookById(Long id);

    List<ProductBook> findProductBooksByDisplay(boolean display, Pageable pageable);

}
