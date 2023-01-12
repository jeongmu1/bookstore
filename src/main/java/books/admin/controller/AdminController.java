package books.admin.controller;

import books.admin.service.AdminProductService;
import books.book.domain.*;
import books.book.repository.CategoryRepository;
import books.book.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AdminProductService adminProductService;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;

    @Autowired
    public AdminController(AdminProductService adminProductService,
                           CategoryRepository categoryRepository,
                           PublisherRepository publisherRepository) {
        this.adminProductService = adminProductService;
        this.categoryRepository = categoryRepository;
        this.publisherRepository = publisherRepository;
    }

    @GetMapping
    public String showAdminPage() {
        return null;
    }

    @GetMapping(value = "/product")
    public String showProductForm(Model model) {
        List<Category> categories = new ArrayList<>();
        categoryRepository.findAll().forEach(categories::add);

        List<Publisher> publishers = new ArrayList<>();
        publisherRepository.findAll().forEach(publishers::add);

        model.addAttribute("categories", categories).addAttribute("publishers", publishers);

        return "productForm";
    }

    // 수정 필요
    @PostMapping(value = "/product")
    public String addProduct(@Valid ProductBook productBook,
                             @RequestParam Map<String, Object> params) {
        adminProductService.addProduct(productBook, params);

        return null;
    }

}
