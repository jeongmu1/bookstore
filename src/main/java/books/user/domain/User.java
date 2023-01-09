package books.user.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@Data
public class User {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50)
    @NotNull
    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Size(max = 45)
    @NotNull
    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Size(max = 64)
    @NotNull
    @Column(name = "email", nullable = false, length = 64)
    private String email;

    @Size(max = 60)
    @NotNull
    @Column(name = "password", nullable = false, length = 50)
    private String password;

    @Size(max = 15)
    @NotNull
    @Column(name = "phone", nullable = false, length = 15)
    private String phone;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private Byte enabled;

    @Size(max = 5)
    @Column(name = "zip_code", length = 5)
    private String zipCode;

    @Size(max = 45)
    @Column(name = "address_detail", length = 45)
    private String addressDetail;

    @NotNull
    @Column(name = "create_time", nullable = false, updatable = false)
    private Timestamp createTime;

    @NotNull
    @Column(name = "update_time", nullable = false)
    private Timestamp updateTime;

    @OneToMany(mappedBy = "user")
    private Set<Authority> authorities = new LinkedHashSet<>();

    @PrePersist
    public void persistTime() {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        this.createTime = currentTime;
        this.updateTime = currentTime;
    }

    @PreUpdate
    public void updateTime() {
        this.updateTime = new Timestamp(System.currentTimeMillis());
    }
}