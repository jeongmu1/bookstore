package books.home.controller;

import books.home.service.HomeService;
import books.product.common.SearchForm;
import books.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/")
public class HomeController {

    private final HomeService homeService;

    public HomeController(HomeService homeService, UserService userService) {
        this.homeService = homeService;
    }

    @GetMapping
    public String showMainPage(Model model, Principal principal) {
        model.addAttribute("categories", homeService.findAllCategories());
        model.addAttribute("displays", homeService.findDisplayBooksForDtos());
        return "index";
    }

    @GetMapping("/search")
    public String redirectToSearch(SearchForm searchForm, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("param", searchForm.getParam());
        redirectAttributes.addAttribute("type", searchForm.getType());
        return "redirect:/shop/search";
    }
}
