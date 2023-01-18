package books.common;

import books.user.service.UserService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.security.Principal;

@Aspect
@Component
public class ControllerAspect {
    private final UserService userService;

    public ControllerAspect(UserService userService) {
        this.userService = userService;
    }

    @Before(value = "execution(* books.*.controller.*Controller.show*(..)) && args(model, principal)", argNames = "model,principal")
    public void setLoginDetails(Object model, Object principal) {
        Model m = (Model) model;
        Principal p = (Principal) principal;

        if (p == null) {
            m.addAttribute("member", "Guest");
        } else {
            userService.loadUserByUsername(p.getName()).ifPresent(user -> m.addAttribute("member", user.getName()));
        }
    }
}
