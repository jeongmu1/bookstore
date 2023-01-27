package books.user.domain;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "user_address")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
public class UserAddress {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 45)
    @NotNull
    @Column(name = "delivery_name", nullable = false, length = 45)
    private String deliveryName;

    @Size(max = 5)
    @NotNull
    @Column(name = "zip_code", nullable = false, length = 5)
    private String zipCode;

    @Size(max = 45)
    @NotNull
    @Column(name = "address", nullable = false, length = 45)
    private String address;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @NotNull
    @Column(name = "default_flag", nullable = false)
    private boolean defaultFlag;

    @NotNull
    @Column(name = "create_time", nullable = false)
    private Timestamp createTime;

    @NotNull
    @Column(name = "update_time", nullable = false)
    private Timestamp updateTime;

    @Size(max = 15)
    @NotNull
    @Column(name = "phone", nullable = false, length = 15)
    private String phone;

    @PrePersist
    public void prePersist() {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        this.createTime = currentTime;
        this.updateTime = currentTime;
        this.defaultFlag = false;
    }

    @PreUpdate
    public void preUpdate() {
        this.updateTime = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserAddress that = (UserAddress) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}