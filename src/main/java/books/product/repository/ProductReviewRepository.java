package books.product.repository;

import books.product.domain.ProductReview;
import org.springframework.data.repository.CrudRepository;

public interface ProductReviewRepository extends CrudRepository<ProductReview, Long> {
}
