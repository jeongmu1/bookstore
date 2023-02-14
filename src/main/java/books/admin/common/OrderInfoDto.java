package books.admin.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderInfoDto {
    private String updateTime;
    private String orderUuid;
    private String productName;
    private Long productId;
    private int quantity;
    private String userName;
    private String deliveryState;
    private Long id;
}
