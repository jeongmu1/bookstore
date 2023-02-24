package books.admin.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
