package books.user.common;

import books.user.domain.User;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;

@Data
public class RegistrationForm {
    private String username;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String zipCode;
    private String addressDetail;

    public User toUser(PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setUsername(username);
        user.setUsername(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setPhone(phone);
        return user;
    }
}
