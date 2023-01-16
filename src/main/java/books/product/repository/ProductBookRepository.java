package books.product.repository;

import books.product.domain.ProductBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductBookRepository extends JpaRepository<ProductBook, Long> {

    public ProductBook findProductBookById(Long id);

}
