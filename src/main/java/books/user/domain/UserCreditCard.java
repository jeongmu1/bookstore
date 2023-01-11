package books.user.domain;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "user_credit_card")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class UserCreditCard {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 16)
    @NotNull
    @Column(name = "cc_number", nullable = false, length = 16)
    private String ccNumber;

    @Size(max = 5)
    @NotNull
    @Column(name = "cc_expiration", nullable = false, length = 5)
    private String ccExpiration;

    @Size(max = 3)
    @NotNull
    @Column(name = "cc_cvv", nullable = false, length = 3)
    private String ccCvv;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @NotNull
    @Column(name = "create_time", nullable = false)
    private Timestamp createTime;

    @PrePersist
    public void persistTime() {
        this.createTime = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserCreditCard that = (UserCreditCard) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}