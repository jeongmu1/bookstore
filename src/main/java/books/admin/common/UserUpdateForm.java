package books.admin.common;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserUpdateForm {
    private Long id;
    private Boolean enabled;
    private String password;
    private String name;
    private String phone;
    private String authority;
}
