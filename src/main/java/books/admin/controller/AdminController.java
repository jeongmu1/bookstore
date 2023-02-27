package books.admin.controller;

import books.admin.common.*;
import books.admin.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
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
                .addAttribute("publishers", adminService.findAllPublishers())
                .addAttribute("productBookForm", new ProductBookForm());

        return "admin/productForm";
    }

    @GetMapping(value = "/orderManager")
    public String showOrderManagementPage(Model model, Principal principal
            , @RequestParam(required = false) Set<String> deliveryStates
            , @RequestParam(required = false) String searchCondition
            , @RequestParam(required = false) String keyword) {
        model.addAttribute("deliveryStates", adminService.findAllDeliveryStates());
        model.addAttribute("orders", adminService.findOrderInfoByConditions(deliveryStates, searchCondition, keyword));
        return "admin/orderManager";
    }

    @PostMapping(value = "/orderManager")
    public String updateDeliveryState(@RequestParam Set<Long> ids, @RequestParam String deliveryState) {
        adminService.updateDeliveryState(ids, deliveryState);
        return "redirect:/admin/orderManager";
    }

    // 수정 필요
    @PostMapping(value = "/product")
    public String addProduct(@Valid ProductBookForm productBookForm) {
        adminService.addProduct(productBookForm);
        return "redirect:/";
    }

    @GetMapping(value = "/accountManager")
    public String showAccountManagerPage(Model model, Principal principal,
                                         @RequestParam(required = false) String authority,
                                         @RequestParam(required = false) String enabled,
                                         @RequestParam(required = false) String searchCriteria,
                                         @RequestParam(required = false) String keyword) {
        model.addAttribute("userInfos", adminService.findUserInfoByConditions(authority, enabled, searchCriteria, keyword));
        return "admin/accountManager";
    }

    @GetMapping(value = "/accountManager/withdraw")
    public String withdrawUser(@RequestParam Long id) {
        adminService.deleteUserById(id);
        return "redirect:/admin/accountManager";
    }

    @GetMapping(value = "/accountManager/updateUser")
    public String showUserUpdateForm(Model model, Principal principal,
                                     @RequestParam Long id) {
        model.addAttribute("updateForm", adminService.initializeUserUpdateForm(id));
        model.addAttribute("userInfo", adminService.findUserById(id));
        return "admin/updateUser";
    }

    @PostMapping(value = "/accountManager/updateUser")
    public String updateUser(UserUpdateForm updateForm) {
        adminService.updateUser(updateForm);
        return "redirect:/admin/accountManager";
    }

    @GetMapping(value = "/productManager")
    public String showProductManagerPage(Model model, Principal principal,
                                         @RequestParam(required = false) String searchCriteria,
                                         @RequestParam(required = false) String keyword,
                                         @RequestParam(required = false) Boolean enabled) {
        List<ProductBookDto> books = adminService.findProductBookByConditions(searchCriteria, keyword, enabled);
        model.addAttribute("books", books);
        model.addAttribute("bookForm", new BookForm(books));
        return "admin/productManager";
    }

    @PostMapping(value = "/productManager")
    public String updateStock(@ModelAttribute("books") BookForm bookForm) {
        adminService.updateProductStock(bookForm.getBooks());
        return "redirect:/admin/productManager";
    }

    @GetMapping(value = "/productManager/update")
    public String showProductUpdateForm(Model model, Principal principal,
                                        @RequestParam Long id) {
        model.addAttribute("categories", adminService.findAllCategories());
        model.addAttribute("publishers", adminService.findAllPublishers());

        model.addAttribute("productBookForm", adminService.getProductBookFormById(id));

        return "admin/productUpdateForm";
    }

    @PostMapping(value = "/productManager/update")
    public String updateProduct(@Valid ProductBookForm productBookForm) {
        adminService.updateProductBook(productBookForm);
        return "redirect:/admin/productManager";
    }

    @GetMapping(value = "/reviewManager")
    public String showReviewManagerPage(Model model, Principal principal,
                                        @RequestParam(required = false) String searchCriteria,
                                        @RequestParam(required = false) String keyword) {
        model.addAttribute("reviews", adminService.findProductReviewByConditions(searchCriteria, keyword));
        return "admin/reviewManager";
    }

    @PostMapping(value = "/reviewManager/delete")
    public String deleteReview(@RequestParam Long id) {
        adminService.deleteProductReviewById(id);
        return "redirect:/admin/reviewManager";
    }
}
