package books.order.service;

import books.product.common.CartItemDto;
import books.user.domain.UserAddress;

import java.security.Principal;
import java.util.List;

public interface OrderService {
    UserAddress findDefaultUserAddress(Principal principal);
    List<CartItemDto> findProduct(Long productBookId, int quantity);
}
