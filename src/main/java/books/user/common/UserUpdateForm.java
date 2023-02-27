package books.user.common;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserUpdateForm {
    private String name;
    private String password;
    private String phone;
}
