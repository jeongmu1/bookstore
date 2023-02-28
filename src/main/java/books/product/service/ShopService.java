package books.product.service;

import books.home.common.ProductBookDto;
import books.product.domain.Category;
import books.product.domain.ProductBook;
import books.product.domain.ProductReview;
import books.user.common.ProductReviewForm;

import java.util.List;
import java.util.Set;

public interface ShopService {

    ProductBookDto findProductDetails(Long productId);

    Set<Category> findCategoriesByBook(ProductBook book);

    double findProductReviewRate(ProductBook book);

    Set<ProductReview> findUserInProductReviews(Set<ProductReview> reviews);

    List<ProductBookDto> findProductsBySearch(String param, int type);

    void addProductReview(String username, ProductReviewForm reviewForm);
}
