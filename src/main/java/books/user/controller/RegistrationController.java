package books.user.controller;

import books.user.common.RegistrationForm;
import books.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String registerForm() {
        return "account/register";
    }

    @PostMapping
    public String processRegistration(RegistrationForm form) {
        userService.processRegistration(form);
        return "redirect:/login";
    }

}
