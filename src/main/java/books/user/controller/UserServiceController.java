package books.user.controller;

import books.user.domain.User;
import books.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/account")
public class UserServiceController {

    private final UserService userService;

    @Autowired
    public UserServiceController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showUserDetailsMain(@AuthenticationPrincipal User user
            , Model model
            , @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        model.addAttribute("orders", userService.getUserOrdersPage(user, page));
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("user.username", user.getUsername());
        userInfo.put("user.name", user.getName());
        userInfo.put("user.point", user.getPoint().toString());
        // 아직 구현안됨
        return "account/info";
    }

    @GetMapping("/orderDetail")
    public String showOrderDetail(@AuthenticationPrincipal User user
            , @RequestParam(value = "orderUuid") String orderUuid
            , Model model) {
        model.addAttribute("order", userService.getOrderDetail(orderUuid));
        return "account/orderDetail";
        // 아직 미구현
    }

    @GetMapping("/pointHistory")
    public String showPointHistories(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("histories", userService.getUserPointHistoriesPage(user));

        return "account/pointHistory";
    }

    @GetMapping("/addressInfo")
    public String showUserAddresses(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("addresses", userService.getUserAddresses(user));
        return "account/addressInfo";
    }

    @GetMapping("/payInfo")
    public String showUserCreditCards(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("creditCards", userService.getUserCreditCards(user));
        return "account/payInfo";
    }
}
