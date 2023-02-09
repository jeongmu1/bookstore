package books.admin.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Data
@NoArgsConstructor
public class ProductBookForm {
    private String title;
    private Long publisher;
    private String author;
    private int price;
    private String description;
    private int stock;
    private Set<Long> categories;
    private MultipartFile bookImage;
}
