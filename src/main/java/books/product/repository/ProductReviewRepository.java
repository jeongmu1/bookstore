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
import java.util.Optional;
import java.util.Set;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long>, JpaSpecificationExecutor<ProductReview> {
    List<ProductReview> findAllByProductBook(ProductBook productBook);

    Integer countProductReviewsByUser(User user);

    void deleteByUser(User user);

    Integer countProductReviewsByUserAndProductBook(User user, ProductBook book);

    @NotNull
    @EntityGraph(attributePaths = {"productBook", "user"})
    List<ProductReview> findAll(Specification specification);

    @NotNull
    @EntityGraph(attributePaths = {"productBook", "user"})
    List<ProductReview> findProductReviewsByUser(User user);

    // Review 조회 시 DTO 를 View 로 전달하도록 변경 예정
    @NotNull
    @EntityGraph(attributePaths = {"productBook"})
    Optional<ProductReview> findById(@NotNull Long id);
}
