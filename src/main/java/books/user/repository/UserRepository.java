package books.user.repository;

import books.product.domain.ProductReview;
import books.user.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

    User findUserByProductReviews(ProductReview productReview);
}