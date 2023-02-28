package books.product.controller;

import books.home.service.HomeService;
import books.product.service.ShopService;
import books.user.common.ProductReviewForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/shop")
public class ShopController {
    private final ShopService shopService;
    private final HomeService homeService;

    public ShopController(ShopService shopService
            , HomeService homeService) {
        this.shopService = shopService;
        this.homeService = homeService;
    }

    @GetMapping("/product")
    public String showProductDetail(Model model, Principal principal, @RequestParam long itemId) {
        model.addAttribute("book", shopService.findProductDetails(itemId));
        return "shop/productDetail";
    }

    /**
     * @param param 검색어 입력
     * @param type  검색 타입 설정 (1:제목, 2:작가, 3:출판사, 4:카테고리, 5:통합)
     * @return 뷰 반환
     */
    @GetMapping("/search")
    public String showSearchResult(Model model, Principal principal
            , @RequestParam String param
            , @RequestParam int type
    ) {
        model.addAttribute("categories", homeService.findAllCategories());
        model.addAttribute("products", shopService.findProductsBySearch(param, type));
        return "shop/search";
    }

    @GetMapping("/product/reviewForm")
    public String showProductReviewForm(Model model, Principal principal,
                                        @RequestParam Long id) {
        model.addAttribute("bookId", id);
        model.addAttribute("reviewForm", new ProductReviewForm());
        return "shop/reviewForm";
    }

    @PostMapping("/product/reviewForm")
    public String confirmProductReview(RedirectAttributes redirectAttributes,
                                       Principal principal,
                                       ProductReviewForm reviewForm) {
        shopService.addProductReview(principal.getName(), reviewForm);
        redirectAttributes.addAttribute("itemId", reviewForm.getProductBookId());
        return "redirect:/shop/product";
    }
}
