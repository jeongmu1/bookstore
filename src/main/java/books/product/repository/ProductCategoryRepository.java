package books.product.repository;

import books.product.domain.ProductBook;
import books.product.domain.ProductCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface ProductCategoryRepository extends CrudRepository<ProductCategory, Long> {
    Set<ProductCategory> findAllByProductBook(ProductBook book);
    Set<ProductCategory> findProductCategoriesByCategoryId(Long id);
}
