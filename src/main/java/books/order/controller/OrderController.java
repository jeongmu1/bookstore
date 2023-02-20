package books.order.controller;

import books.order.common.NoItemException;
import books.order.common.OrderForm;
import books.order.service.OrderService;
import books.product.common.CartItemDto;
import books.product.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final CartService cartService;

    public OrderController(OrderService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @GetMapping("/cart")
    public String showCartOrderForm(Model model, Principal principal) {
        List<CartItemDto> cartItemDtos = cartService.findCartByUser(principal);
        model.addAttribute("address", orderService.findDefaultUserAddress(principal));
        model.addAttribute("cart", cartItemDtos);
        model.addAttribute("total", cartService.getTotalPrice(cartItemDtos));
        model.addAttribute("orderForm", new OrderForm());
        model.addAttribute("type", "cart");
        return "order/orderForm";
    }

    @GetMapping("/product")
    public String showProductOrderForm(Model model, Principal principal
            , @RequestParam int quantity
            , @RequestParam Long productBookId) {
        List<CartItemDto> cartItemDtos = orderService.findProduct(productBookId, quantity);
        model.addAttribute("address", orderService.findDefaultUserAddress(principal));
        model.addAttribute("cart", cartItemDtos);
        model.addAttribute("total", cartService.getTotalPrice(cartItemDtos));
        model.addAttribute("orderForm", new OrderForm());
        model.addAttribute("type", "product");
        model.addAttribute("productBookId", productBookId).addAttribute("quantity", quantity);
        return "order/orderForm";
    }

    @PostMapping("/product")
    public String createOrderByProduct(
            @Valid OrderForm orderForm
            , Principal principal
            , @RequestParam Long productBookId
            , @RequestParam int quantity) {
        orderService.addOrderByProduct(orderForm, principal, productBookId, quantity);
        return "redirect:/";
    }

    @PostMapping("/cart")
    public String createOrderByCart(@Valid OrderForm orderForm, Principal principal) throws NoItemException {
        orderService.addOrderByCartItems(orderForm, principal);
        return "redirect:/";
    }
}