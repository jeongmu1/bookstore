package books.product.repository;

import books.product.domain.ProductBook;
import books.product.domain.ProductReview;
import books.user.domain.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long>, JpaSpecificationExecutor<ProductReview> {
    List<ProductReview> findAllByProductBook(ProductBook productBook);

    Integer countProductReviewsByUser(User user);

    void deleteByUser(User user);

    @NotNull
    @EntityGraph(attributePaths = {"productBook", "user"})
    List<ProductReview> findAll(Specification specification);
}
