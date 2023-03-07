package books.user.domain;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
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

    @NotNull
    @Column(name = "point_change", nullable = false)
    private Integer pointChange;

    @NotNull
    @Column(name = "change_result", nullable = false)
    private Integer changeResult;

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

    @Builder
    public UserPointHistory(final Long id,
                            final PointHistoryDetail pointHistoryDetail,
                            final User user,
                            final Timestamp createTime,
                            final Integer pointChange,
                            final Integer changeResult) {
        this.id = id;
        this.pointHistoryDetail = pointHistoryDetail;
        this.user = user;
        this.createTime = createTime;
        this.pointChange = pointChange;
        this.changeResult = changeResult;
    }
}