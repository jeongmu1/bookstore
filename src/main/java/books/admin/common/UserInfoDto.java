package books.admin.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoDto {
    private Long id;
    private boolean enabled;
    private String username;
    private String name;
    private String phone;
    private String createTime;
    private String authority;
}
