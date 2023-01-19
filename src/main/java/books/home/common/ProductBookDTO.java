package books.home.common;

import books.product.domain.ProductBook;
import books.product.domain.ProductImage;
import lombok.Data;

@Data
public class ProductBookDTO {
    private Long id;
    private String title;
    private String author;
    private int price;
    private String description;
    private ProductImage productImage;

    public ProductBookDTO(ProductBook book, ProductImage image) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.price = book.getPrice();
        this.description = book.getDescription();
        this.productImage = image;
    }
}
