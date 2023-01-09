package books.book.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

import books.user.domain.User;

@Entity
@Table(name = "product_review")
@Data
public class ProductReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "product_score", nullable = false)
    private Byte productScore;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_book_id", nullable = false)
    private ProductBook productBook;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Lob
    @Column(name = "comment")
    private String comment;

    @NotNull
    @Column(name = "create_time", nullable = false, updatable = false)
    private Timestamp createTime;

    @PrePersist
    public void persistTime() {
        this.createTime = new Timestamp(System.currentTimeMillis());
    }
}