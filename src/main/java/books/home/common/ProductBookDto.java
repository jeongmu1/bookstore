package books.home.common;

import books.product.domain.ProductReview;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductBookDto {
    private Long id;
    private String title;
    private String author;
    private String publisher;
    private int price;
    private double rate;
    private String description;
    private int sizeOfReviews;
    private String imageFileName;
    private boolean enabled;
    private boolean outOfStock;
    private List<ProductReview> reviews;
}
