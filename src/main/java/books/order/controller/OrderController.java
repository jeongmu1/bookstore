package books.order.controller;

import books.order.service.OrderService;
import books.product.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final CartService cartService;

    public OrderController(OrderService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @GetMapping
    public String showOrderForm(Model model, Principal principal) {
        model.addAttribute("address", orderService.findDefaultUserAddress(principal));
        model.addAttribute("cart", cartService.findCartByUser(principal));
        return "order/orderForm";
    }

    @GetMapping("/buyOne")
    public String showOnlyOrderForm(Model model, Principal principal
            , @RequestParam Long productBookId
            , @RequestParam int quantity) {
        model.addAttribute("address", orderService.findDefaultUserAddress(principal));
        model.addAttribute("cart", orderService.findProduct(productBookId, quantity));
        return "order/orderForm";
    }
}
