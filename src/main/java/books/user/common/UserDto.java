package books.user.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private Long id;
    private String name;
    private int point;
    private int reviews;
    private int cart;
    private int payedOrders;
    private int shippingOrders;
    private int completedOrders;
}
