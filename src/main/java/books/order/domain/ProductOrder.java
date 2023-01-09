package books.order.domain;

import books.user.domain.User;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "product_order")
@Data
public class ProductOrder {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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

    @Size(max = 45)
    @NotNull
    @Column(name = "delivery_name", nullable = false, length = 45)
    private String deliveryName;

    @Size(max = 5)
    @NotNull
    @Column(name = "delivery_zip_code", nullable = false, length = 5)
    private String deliveryZipCode;

    @Size(max = 45)
    @NotNull
    @Column(name = "delivery_address", nullable = false, length = 45)
    private String deliveryAddress;

    @Size(max = 15)
    @NotNull
    @Column(name = "delivery_phone", nullable = false, length = 15)
    private String deliveryPhone;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @NotNull
    @Column(name = "create_time", nullable = false, updatable = false)
    private Timestamp createTime;

    @OneToMany(mappedBy = "productOrder")
    private Set<ProductOrderProduct> productOrderProducts = new LinkedHashSet<>();

    @PrePersist
    public void persistTime() {
        this.createTime = new Timestamp(System.currentTimeMillis());
    }
}