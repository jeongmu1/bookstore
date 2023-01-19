package books.product.repository;

import books.product.domain.ProductBook;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductBookRepository extends JpaRepository<ProductBook, Long> {

    public ProductBook findProductBookById(Long id);

    public List<ProductBook> findProductBooksByDisplay(boolean display, Pageable pageable);

}
