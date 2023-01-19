package books.product.repository;

import books.product.domain.ProductBook;
import books.product.domain.ProductImage;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface ProductImageRepository extends CrudRepository<ProductImage, Long> {
    public Set<ProductImage> findAllByProductBook(ProductBook productBook);
}
