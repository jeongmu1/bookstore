package books.home.controller;

import books.home.repository.CategoryRepository;
import books.home.repository.ProductBookRepository;
import books.home.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/home")
public class HomeController {

    private ProductBookRepository productBookRepo;
    private ProductCategoryRepository productCategoryRepo;
    private CategoryRepository categoryRepo;

    @Autowired
    public HomeController(ProductBookRepository productBookRepo
            , ProductCategoryRepository productCategoryRepo
            , CategoryRepository categoryRepo) {
        this.productBookRepo = productBookRepo;
        this.productCategoryRepo = productCategoryRepo;
        this.categoryRepo = categoryRepo;
    }

//    @GetMapping
//    public String showMainPage(Model model) {
//        return "home";
//    }
}
