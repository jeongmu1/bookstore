package books.order.service;

import books.order.common.NoItemException;
import books.order.common.OrderForm;
import books.product.common.CartItemDto;
import books.user.domain.UserAddress;

import java.util.List;

public interface OrderService {
    UserAddress findDefaultUserAddress(String username);
    List<CartItemDto> findProduct(Long productBookId, int quantity);
    void addOrderByCartItems(OrderForm orderForm, String username) throws NoItemException;

    void addOrderByProduct(OrderForm orderForm, String username, Long productBookId, int quantity) ;
}
