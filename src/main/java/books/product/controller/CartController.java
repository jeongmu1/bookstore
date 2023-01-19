package books.product.controller;

import books.product.common.CartItemDto;
import books.product.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public String showCartItems(Model model, Principal principal) {
        List<CartItemDto> cart = cartService.findCartByUser(principal);
        model.addAttribute("cart", cart);
        model.addAttribute("total", cartService.getTotalPrice(cart));
        return "shop/cart";
    }

    @PostMapping
    public String productAdd(Principal principal, int cartCnt, @RequestParam long itemId) {
        cartService.addProductInCart(principal, cartCnt, itemId);
        return "redirect:/cart";
    }

    @PostMapping("/amount")
    public String productModify(Principal principal, String count) {
        cartService.modifyProductCount(principal, count);
        return "redirect:/cart";
    }

    @PostMapping("/delete")
    public String productRemove(Principal principal, @RequestParam long itemId) {
        cartService.deleteProduct(principal, itemId);
        return "redirect:/cart";
    }

    @PostMapping("/deleteAll")
    public String productRemoveAll(Principal principal) {
        cartService.deleteAll(principal);
        return "redirect:/cart";
    }
}
