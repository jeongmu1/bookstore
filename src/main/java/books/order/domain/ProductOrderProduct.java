package books.order.domain;

import books.product.domain.ProductBook;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "product_order_product")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ProductOrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "product_book_id", nullable = false)
    @ToString.Exclude
    private ProductBook productBook;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_order_id", nullable = false)
    @ToString.Exclude
    private ProductOrder productOrder;

    @NotNull
    @Column(name = "product_count", nullable = false)
    private int productCount;

    @NotNull
    @Column(name = "create_time", nullable = false, updatable = false)
    private Timestamp createTime;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @PrePersist
    public void prePersist() {
        this.createTime = new Timestamp(System.currentTimeMillis());
        this.enabled = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProductOrderProduct that = (ProductOrderProduct) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}