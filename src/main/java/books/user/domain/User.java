package books.user.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Entity
@Table(name = "user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 50)
    @NotNull
    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Size(max = 64)
    @NotNull
    @Column(name = "email", nullable = false, length = 64)
    private String email;

    @Size(max = 50)
    @NotNull
    @Column(name = "password", nullable = false, length = 50)
    private String password;

    @Size(max = 15)
    @NotNull
    @Column(name = "phone", nullable = false, length = 15)
    private String phone;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Size(max = 5)
    @Column(name = "zip_code", length = 5)
    private String zipCode;

    @Size(max = 45)
    @Column(name = "address_detail", length = 45)
    private String addressDetail;

    @NotNull
    @Column(name = "create_time", nullable = false)
    private Timestamp createTime;

    @NotNull
    @Column(name = "update_time", nullable = false)
    private Timestamp updateTime;

}