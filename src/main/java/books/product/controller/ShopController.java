package books.product.controller;

import books.product.repository.ProductBookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/shop")
public class ShopController {

    private final ProductBookRepository

    @GetMapping("/product")
    public String showProductDetail(@RequestParam int itemId
            , Model model) {

        return null;
    }
}
