package books.user.controller;

import books.user.common.ProductReviewForm;
import books.user.common.UserUpdateForm;
import books.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;

@Controller
@RequestMapping("/account")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showUserDetailsMain(Model model, Principal principal) {
        model.addAttribute("user", userService.findUserInfo(principal));
        return "account/info";
    }

    @GetMapping("/pointInfo")
    public String showPointHistory(Model model, Principal principal) {
        model.addAttribute("histories", userService.findUserPointHistory(principal));
        return "account/pointHistory";
    }

    @GetMapping("/update")
    public String showUserUpdateForm(Model model, Principal principal) {
        model.addAttribute("userInfo", userService.findUserInfoDto(principal.getName()));
        model.addAttribute("updateForm", userService.initializeUserUpdateFormByUsername(principal.getName()));
        return "account/updateUser";
    }

    @PostMapping("/update")
    public String updateUser(Principal principal, UserUpdateForm updateForm) {
        userService.updateUserByForm(principal.getName(), updateForm);
        return "redirect:/account";
    }

    @GetMapping("/delete")
    public String deleteUser(Principal principal) {
        userService.deleteUserByUsername(principal.getName());
        return "redirect:/logout";
    }

    @GetMapping("/reviews")
    public String showReviews(Model model, Principal principal) {
        model.addAttribute("reviews", userService.findProductReviewsByUsername(principal.getName()));
        return "account/reviews";
    }

    @GetMapping("/reviews/update")
    public String showReviewUpdateForm(Model model, Principal principal,
                                       @RequestParam Long id) throws IllegalAccessException {
        model.addAttribute("review", userService.findProductReviewForUpdateById(principal.getName(), id));
        model.addAttribute("updateForm", new ProductReviewForm());
        return "account/reviewUpdateForm";
    }

    @PostMapping("/reviews/update")
    public String updateProductReview(Principal principal, ProductReviewForm productReviewForm) throws IllegalAccessException {
        userService.updateProductReview(principal.getName(), productReviewForm);
        return "redirect:/account/reviews";
    }

    @PostMapping("/reviews/delete")
    public String deleteProductReview(Principal principal,
                                      @RequestParam Long id) throws IllegalAccessException {
        userService.deleteProductReviewById(principal.getName(), id);
        return "redirect:/account/reviews";
    }

    @GetMapping("/orders")
    public String showOrders(Model model, Principal principal,
                             @RequestParam(required = false) Set<String> deliveryStates,
                             @RequestParam(required = false) String searchCriteria,
                             @RequestParam(required = false) String keyword) {
        model.addAttribute("orders", userService.findOrderInfos(principal.getName(), deliveryStates, searchCriteria, keyword));
        model.addAttribute("deliveryStates", userService.findAllDeliveryStates()); // adminService 의 메소드와 중복됨
        return "account/orders";
    }
}
