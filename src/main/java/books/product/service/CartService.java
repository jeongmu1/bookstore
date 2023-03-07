package books.product.service;


import books.product.common.CartItemDto;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface CartService {
    List<CartItemDto> findCartByUser(String username);

    void addProductInCart(String username, int amount, long itemId);

    Map<String, Object> getTotalPrice(List<CartItemDto> cart);

    void modifyProductCount(String username, String amounts);

    void deleteProduct(String username, long itemId);

    void deleteAll(String username);
}
