package books.book.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Entity
@Table(name = "product_image")
@Data
public class ProductImage {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_book_id", nullable = false)
    private ProductBook productBook;

    @Size(max = 45)
    @NotNull
    @Column(name = "file_name", nullable = false, length = 45)
    private String fileName;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @NotNull
    @Column(name = "create_time", nullable = false, updatable = false)
    private Timestamp createTime;

    @PrePersist
    public void persistTime() {
        this.createTime = new Timestamp(System.currentTimeMillis());
        this.enabled = true;
    }
}