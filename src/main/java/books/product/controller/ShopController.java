package books.product.controller;

import books.common.PointProps;
import books.product.domain.ProductBook;
import books.product.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/shop")
public class ShopController {
    private final ShopService shopService;
    private final PointProps pointProps;

    @Autowired
    public ShopController(ShopService shopService, PointProps pointProps) {
        this.shopService = shopService;
        this.pointProps = pointProps;
    }

    @GetMapping("/product")
    public String showProductDetail(@RequestParam long itemId
            , Model model, Principal principal) {
        ProductBook book = shopService.getProductDetails(itemId);
        model.addAttribute("book", book);
        model.addAttribute("categories", shopService.getCategoriesByBook(book));
        model.addAttribute("image", book.getProductImages().iterator().next());
        model.addAttribute("reviews", shopService.setUserInProductReviews(book.getProductReviews()));
        model.addAttribute("publisher", book.getPublisher().getName());
        model.addAttribute("rate",shopService.getProductReviewRate(book));
        model.addAttribute("sizeOfReviews", book.getProductReviews().size());
        model.addAttribute("savingRate", (book.getPrice() / pointProps.getSavingRate()));
        return "shop/productDetail";
    }
}
