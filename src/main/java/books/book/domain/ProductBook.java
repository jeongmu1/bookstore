package books.book.domain;

import books.order.domain.ProductOrderProduct;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "product_book")
@Data
public class ProductBook {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 45)
    @NotNull
    @Column(name = "title", nullable = false, length = 45)
    private String title;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "publisher_id", nullable = false)
    private Publisher publisher;

    @Size(max = 45)
    @NotNull
    @Column(name = "author", nullable = false, length = 45)
    private String author;

    @Size(max = 45)
    @Column(name = "translator", length = 45)
    private String translator;

    @NotNull
    @Column(name = "price", nullable = false)
    private int price;

    @NotNull
    @Column(name = "discount", nullable = false)
    private int discount;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @NotNull
    @Column(name = "create_time", nullable = false)
    private Timestamp createTime;

    @NotNull
    @Column(name = "update_time", nullable = false)
    private Timestamp updateTime;

    @OneToMany(mappedBy = "productBook")
    private Set<ProductReview> productReviews = new LinkedHashSet<>();

    @OneToMany(mappedBy = "productBook")
    private Set<ProductOrderProduct> productOrderProducts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "productBook")
    private Set<ProductCategory> productCategories = new LinkedHashSet<>();

    @OneToMany(mappedBy = "productBook")
    private Set<ProductImage> productImages = new LinkedHashSet<>();

}