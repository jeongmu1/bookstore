package books.user.controller;

import books.admin.common.UserInfoDto;
import books.admin.service.AdminService;
import books.user.common.UserUpdateForm;
import books.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/account")
public class UserController {

    private final UserService userService;
    private final AdminService adminService;

    public UserController(UserService userService, AdminService adminService) {
        this.userService = userService;
        this.adminService = adminService;
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
        UserInfoDto userInfoDto = userService.findUserInfoDto(principal.getName());
        model.addAttribute("userInfo", userInfoDto);
        model.addAttribute("updateForm", adminService.initializeUserUpdateForm(userInfoDto.getId()));
        return "account/updateUser";
    }

    @PostMapping("/update")
    public String updateUser(Principal principal, UserUpdateForm updateForm) {
        userService.updateUserByForm(principal.getName(), updateForm);
        return "redirect:/account";
    }

    @PostMapping("/delete")
    public String deleteUser(Principal principal) {
        userService.deleteUserByUsername(principal.getName());
        return "redirect:/logout";
    }
}
