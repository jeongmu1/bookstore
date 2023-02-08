package books.order.service;

import books.order.common.NoItemException;
import books.order.common.OrderForm;
import books.order.common.OverStockException;
import books.product.common.CartItemDto;
import books.user.domain.UserAddress;

import java.security.Principal;
import java.util.List;

public interface OrderService {
    UserAddress findDefaultUserAddress(Principal principal);
    List<CartItemDto> findProduct(Long productBookId, int quantity);
    void addOrderByCartItems(OrderForm orderForm, Principal principal) throws OverStockException, NoItemException;

    void addOrderByProduct(OrderForm orderForm, Principal principal, Long productBookId, int quantity) throws OverStockException;
}
