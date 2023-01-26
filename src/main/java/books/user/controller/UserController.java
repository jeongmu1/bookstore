package books.user.controller;

import books.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/account")
public class UserController {

    private final UserService userService;

    @Autowired
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
}
