package books.admin.controller;

import books.admin.common.ProductBookForm;
import books.admin.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;
import java.util.Set;

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


}
