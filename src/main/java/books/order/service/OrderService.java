package books.order.service;

import books.order.common.*;
import books.product.common.CartItemDto;
import books.user.domain.UserAddress;

import java.util.List;

public interface OrderService {
    UserAddress findDefaultUserAddress(String username);

    List<CartItemDto> findProduct(Long productBookId, int quantity);

    void addOrderByCartItems(OrderForm orderForm, String username) throws NoItemException, NotEnoughPointException, OutOfUnitPointUsage, TooMuchPointsException;

    void addOrderByProduct(OrderForm orderForm, String username, Long productBookId, int quantity) throws NotEnoughPointException, OutOfUnitPointUsage, TooMuchPointsException;
}
