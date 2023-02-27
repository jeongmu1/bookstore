package books.user.repository;

import books.product.domain.ProductReview;
import books.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findByUsername(String username);

    User findUserByProductReviews(ProductReview productReview);

    void deleteByUsername(String username);
}