package books.user.controller;

import books.user.common.RegistrationForm;
import books.user.domain.Authority;
import books.user.domain.User;
import books.user.repository.UserAuthorityRepository;
import books.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    private final UserRepository userRepo;
    private final UserAuthorityRepository authorityRepo;
    private final PasswordEncoder encoder;

    @Autowired
    public RegistrationController(UserRepository userRepo, UserAuthorityRepository authorityRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.authorityRepo = authorityRepo;
        this.encoder = encoder;
    }

    @GetMapping
    public String registerForm() {
        return "register";
    }

    @PostMapping
    public String processRegistration(RegistrationForm form) {
        userRepo.save(form.toUser(encoder));
        userRepo.findByUsername(form.getUsername()).ifPresent(this::addUserAuthority);
        return "redirect:/login";
    }

    private void addUserAuthority(User user) {
        Authority auth = new Authority();
        auth.setUser(user);
        auth.setAuthority("ROLE_USER");
        authorityRepo.save(auth);
    }
}
