package books.admin.common;

import lombok.Data;

@Data
public class ProductBookDto {
    private Long id;
    private String title;
    private String author;
    private String publisher;
    private Integer price;
    private Boolean enabled;
    private Integer stock;
    private Integer preparingStock;
}
