package books.product.service;

import books.product.domain.Category;
import books.product.domain.ProductBook;
import books.product.domain.ProductReview;

import java.util.Set;

public interface ShopService {

    public ProductBook getProductDetails(Long productId);

    public Set<Category> getCategoriesByBook(ProductBook book);

    public double getProductReviewRate(ProductBook book);

    public Set<ProductReview> setUserInProductReviews(Set<ProductReview> reviews);
}
