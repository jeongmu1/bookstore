package books.product.service;


import books.product.common.CartItemDto;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface CartService {
    List<CartItemDto> findCartByUser(Principal principal);

    void addProductInCart(Principal principal, int amount, long itemId);

    Map<String, Object> getTotalPrice(List<CartItemDto> cart);

    void modifyProductCount(Principal principal, String amounts);

    void deleteProduct(Principal principal, long itemId);

    void deleteAll(Principal principal);
}
