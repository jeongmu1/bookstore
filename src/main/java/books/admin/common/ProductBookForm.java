package books.admin.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@NoArgsConstructor
public class ProductBookForm {
    @NotBlank
    private String title;
    private Long publisher;
    @NotBlank
    private String author;
    @NotNull
    private int price;
    @NotBlank
    private String description;
    @NotNull
    private int stock;
    @NotEmpty
    private Set<Long> categories;
    @NotNull
    private MultipartFile bookImage;
}
