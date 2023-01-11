package books.user.domain;

import books.book.domain.ProductReview;
import books.order.domain.ProductOrder;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
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

    @NotNull
    @Column(name = "point", nullable = false)
    private Integer point;

    @NotNull
    @Column(name = "create_time", nullable = false)
    private Timestamp createTime;

    @NotNull
    @Column(name = "update_time", nullable = false)
    private Timestamp updateTime;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private Set<Authority> authorities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private Set<ProductOrder> productOrders = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private Set<UserPointHistory> userPointHistories = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private Set<UserAddress> userAddresses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private Set<ProductReview> productReviews = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private Set<UserCreditCard> userCreditCards = new LinkedHashSet<>();

    @PrePersist
    public void persistTime() {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        this.createTime = currentTime;
        this.updateTime = currentTime;
        this.enabled = true;
    }

    @PreUpdate
    public void updateTime() {
        this.updateTime = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}