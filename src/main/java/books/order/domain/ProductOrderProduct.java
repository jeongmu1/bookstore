package books.order.domain;

import books.book.domain.ProductBook;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Table(name = "product_order_product")
@Data
public class ProductOrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_book_id", nullable = false)
    private ProductBook productBook;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_order_id", nullable = false)
    private ProductOrder productOrder;

    @NotNull
    @Column(name = "product_count", nullable = false)
    private int productCount;

    @NotNull
    @Column(name = "create_time", nullable = false)
    private Timestamp createTime;

}