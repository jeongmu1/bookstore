package books.user.controller;

import books.user.domain.User;
import books.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class UserServiceController {

    private final UserService userService;

    @Autowired
    public UserServiceController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showUserDetailsMain(@AuthenticationPrincipal User user, Model model) { // 유저 정보 조회
        model.addAttribute("user", user);
        return "userDetail";
    }

    @GetMapping("/update")
    public String updateUserInfoForm(@AuthenticationPrincipal User user) { // 유저 정보 수정

        return "updateUserForm";
    }

    @PostMapping("/update")
    public String updateUser() {
        return null;
    }

    @GetMapping("/address")
    public String showUserAddresses(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("addresses", userService.getUserAddresses(user));
        return "userAddress";
    }

    @GetMapping("/address/update")
    public String updateUserAddressForm(@AuthenticationPrincipal User user) {

        return "updateUserAddressForm";
    }

    @GetMapping("/orderHistory")
    public String showOrderHistory(@AuthenticationPrincipal User user) {

        return "orderHistory";
    }

    @GetMapping("/pointDetail")
    public String showPointDetail(@AuthenticationPrincipal User user) { // 포인트 변동내역 조회

        return "pointDetail";
    }
}
