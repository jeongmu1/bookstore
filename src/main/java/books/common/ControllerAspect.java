package books.common;

import books.product.common.CartItemDto;
import books.product.service.CartService;
import books.user.service.UserService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.List;

@Aspect
@Component
public class ControllerAspect {
    private final UserService userService;
    private final CartService cartService;

    public ControllerAspect(UserService userService, CartService cartService) {
        this.userService = userService;
        this.cartService = cartService;
    }

    @Before(value = "execution(* books.*.controller.*Controller.show*(..)) && args(model, principal, ..)", argNames = "model,principal")
    public void initializeDetails(Object model, Object principal) {
        Model m = (Model) model;
        Principal p = (Principal) principal;


        if (p == null) {
            m.addAttribute("member", "Guest");
            m.addAttribute("cartTotal", "0");
            m.addAttribute("cartTotalPrice", "0");

        } else {
            m.addAttribute("member", userService.loadUserByUsername(p.getName()).getName());
            List<CartItemDto> cart = cartService.findCartByUser(p.getName());
            m.addAttribute("cartTotal", cart.size());
            m.addAttribute("cartTotalPrice", cartService.getTotalPrice(cart).get("totalPrice"));
        }
    }
}
