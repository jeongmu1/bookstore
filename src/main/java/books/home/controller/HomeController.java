package books.home.controller;

import books.home.service.HomeService;
import books.product.repository.CategoryRepository;
import books.product.repository.ProductBookRepository;
import books.product.repository.ProductCategoryRepository;
import books.user.domain.User;
import books.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/")
public class HomeController {

    private final HomeService homeService;

    private final UserService userService;

    public HomeController(HomeService homeService, UserService userService) {
        this.homeService = homeService;
        this.userService = userService;
    }

    @GetMapping
    public String showMainPage(Model model, Principal principal) {
        model.addAttribute("categories", homeService.findAllCategories());
        model.addAttribute("displays", homeService.findDisplayBooksForDTOs());
        return "index";
    }
}
