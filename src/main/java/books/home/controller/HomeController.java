package books.home.controller;

import books.product.repository.CategoryRepository;
import books.product.repository.ProductBookRepository;
import books.product.repository.ProductCategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
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
