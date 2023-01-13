package books.user.controller;

import books.order.domain.ProductOrder;
import books.user.domain.User;
import books.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
        List<ProductOrder> orders = userService.getUserOrdersPage(user, page);

        model.addAttribute("orders", orders.stream().map(this::getOrderMap).collect(Collectors.toList()));
        model.addAttribute("user", getSimpleUserInfo(user));

        return "account/info";
    }

    @GetMapping("/orderDetail")
    public String showOrderDetail(@AuthenticationPrincipal User user
            , @RequestParam(value = "orderUuid") String orderUuid
            , Model model) {

        return "account/orderDetail";
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

    private Map<String, String> getSimpleUserInfo(User user) {
        Map<String, String> simpleUserInfoMap = new HashMap<>();
        simpleUserInfoMap.put("name", user.getName());
        simpleUserInfoMap.put("point", user.getPoint().toString());
        return simpleUserInfoMap;
    }

    private Map<String, String> getOrderMap(ProductOrder order) {
        Map<String, String> orderMap = new HashMap<>();
        orderMap.put("orderUuid", order.getOrderUuid());
        orderMap.put("rprsn_book", order.getRprsnBook());
        orderMap.put("deliveryName", order.getDeliveryName());
        orderMap.put("orderDate", order.getCreateTime().toString());
        return orderMap;
    }
}
