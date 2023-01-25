package books.product.controller;

import books.common.PointProps;
import books.home.service.HomeService;
import books.product.domain.ProductBook;
import books.product.service.ShopService;
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
    private final HomeService homeService;

    public ShopController(ShopService shopService, PointProps pointProps, HomeService homeService) {
        this.shopService = shopService;
        this.pointProps = pointProps;
        this.homeService = homeService;
    }

    @GetMapping("/product")
    public String showProductDetail(Model model, Principal principal, @RequestParam long itemId) {
        ProductBook book = shopService.findProductDetails(itemId);
        model.addAttribute("book", book);
        model.addAttribute("categories", shopService.findCategoriesByBook(book));
        model.addAttribute("image", book.getProductImages().iterator().next());
        model.addAttribute("reviews", shopService.findUserInProductReviews(book.getProductReviews()));
        model.addAttribute("publisher", book.getPublisher().getName());
        model.addAttribute("rate", shopService.findProductReviewRate(book));
        model.addAttribute("sizeOfReviews", book.getProductReviews().size());
        model.addAttribute("savingRate", (book.getPrice() / pointProps.getSavingRate()));
        return "shop/productDetail";
    }

    /**
     *
     * @param param 검색어 입력
     * @param type 검색 타입 설정 (1:제목, 2:작가, 3:출판사, 4:카테고리, 5:통합)
     * @return 뷰 반환
     */
    @GetMapping("/search")
    public String showSearchResult(Model model, Principal principal, @RequestParam String param, @RequestParam int type) {
        model.addAttribute("categories", homeService.findAllCategories());
        model.addAttribute("products", shopService.findProductsBySearch(param, type));
        return "shop/search";
    }
}
