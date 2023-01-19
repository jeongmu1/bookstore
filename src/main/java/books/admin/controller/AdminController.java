package books.admin.controller;

import books.admin.service.AdminService;
import books.product.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public String showAdminPage() {
        return null;
    }

    @GetMapping(value = "/product")
    public String showProductForm(Model model) {
        model.addAttribute("categories", adminService.findAllCategories())
                .addAttribute("publishers", adminService.findAllPublishers());

        return "productForm";
    }

    // 수정 필요
    @PostMapping(value = "/product")
    public String addProduct(@Valid ProductBook productBook,
                             @RequestParam Map<String, Object> params) {

        return null;
    }

}
