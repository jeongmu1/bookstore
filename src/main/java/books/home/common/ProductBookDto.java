package books.home.common;

import books.product.domain.ProductBook;
import books.product.domain.ProductImage;
import lombok.Data;

@Data
public class ProductBookDto {
    private Long id;
    private String title;
    private String author;
    private String publisher;
    private int price;
    private double rate;
    private String description;
    private ProductImage productImage;
    private int sizeOfReviews;

    public ProductBookDto(ProductBook book, ProductImage image) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.price = book.getPrice();
        this.publisher = book.getPublisher().getName();
        this.description = book.getDescription();
        this.productImage = image;
    }
}
