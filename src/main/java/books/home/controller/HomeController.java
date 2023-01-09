package books.home.controller;

import books.book.repository.CategoryRepository;
import books.book.repository.ProductCategoryRepository;
import books.home.repository.ProductBookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
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
