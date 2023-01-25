import books.product.domain.*;
import books.product.service.ShopService;
import books.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Set;

@SpringBootTest
@ContextConfiguration(classes = TestConfiguration.class)
class ProductDetailTest {
    @Autowired
    private ShopService shopService;

    @Test
    void getDetails() {
        ProductBook book = shopService.findProductDetails(new Long(1));
        Set<ProductCategory> categories =  book.getProductCategories();
        Set<ProductImage> images = book.getProductImages();
        Set<ProductReview> reviews = shopService.findUserInProductReviews(book.getProductReviews());
        Publisher publisher = book.getPublisher();

        System.out.println(categories);

        System.out.println(images);
        System.out.println(reviews);
        System.out.println(publisher);

        System.out.println(book.toString());

        for (ProductCategory productCategory : categories) {
            System.out.println(productCategory.getCategory().toString());
        }

        for (ProductReview review : reviews) {
            System.out.println(review.getUser().getUsername());
        }
    }

    @Test
    void getAvgScore() {
        ProductBook book = shopService.findProductDetails(new Long(1));
        System.out.println(shopService.findProductReviewRate(book));
    }
}
