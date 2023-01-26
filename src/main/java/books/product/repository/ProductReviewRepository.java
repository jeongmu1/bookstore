package books.product.repository;

import books.product.domain.ProductBook;
import books.product.domain.ProductReview;
import books.user.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface ProductReviewRepository extends CrudRepository<ProductReview, Long> {
    Set<ProductReview> findAllByProductBook(ProductBook productBook);

    Integer countProductReviewsByUser(User user);
}
