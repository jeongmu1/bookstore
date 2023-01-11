package books.user.domain;

import books.order.domain.PointHistoryDetail;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "user_point_history")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class UserPointHistory {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "point_history_detail_id", nullable = false)
    @ToString.Exclude
    private PointHistoryDetail pointHistoryDetail;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

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
        UserPointHistory that = (UserPointHistory) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}