package books.product.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * A DTO for the {@link books.order.domain.ProductOrderProduct} entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {
    private Long id;
    private Long itemId;
    private String title;
    private int price;
    private String fileName;
    private int amount;
    private Timestamp createTime;
    private int point;

}