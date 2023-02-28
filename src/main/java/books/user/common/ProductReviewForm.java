package books.user.common;

import lombok.Data;

@Data
public class ProductReviewForm {
    private Long id;
    private Long productBookId;
    private String comment;
    private Byte productScore;
}
