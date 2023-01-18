package books.admin.common;

import books.product.domain.ProductBook;
import lombok.Data;

import javax.validation.Valid;

@Data
public class ProductBookForm {

    private String title;
    private String publisher;
    private String author;
    private int price;
    private String description;
    private int stock;
    private boolean enabled;

    public ProductBook toProductBook() {
        return new ProductBook();
    }
}
