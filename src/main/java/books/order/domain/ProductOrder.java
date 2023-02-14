package books.order.domain;

import books.user.domain.User;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.*;

@Entity
@Table(name = "product_order")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ProductOrder {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @Size(max = 16)
    @Column(name = "cc_number", nullable = false, length = 16)
    private String ccNumber;

    @Size(max = 5)
    @Column(name = "cc_expiration", nullable = false, length = 5)
    private String ccExpiration;

    @Size(max = 3)
    @Column(name = "cc_cvv", nullable = false, length = 3)
    private String ccCvv;

    @Size(max = 45)
    @Column(name = "delivery_name", nullable = false, length = 45)
    private String deliveryName;

    @Size(max = 5)
    @Column(name = "delivery_zip_code", nullable = false, length = 5)
    private String deliveryZipCode;

    @Size(max = 45)
    @Column(name = "delivery_address", nullable = false, length = 45)
    private String deliveryAddress;

    @Size(max = 15)
    @Column(name = "delivery_phone", nullable = false, length = 15)
    private String deliveryPhone;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @NotNull
    @Column(name = "create_time", nullable = false, updatable = false)
    private Timestamp createTime;

    @OneToMany(mappedBy = "productOrder")
    @ToString.Exclude
    private List<ProductOrderProduct> productOrderProducts = new ArrayList<>();

    @Size(max = 36)
    @NotNull
    @Column(name = "order_uuid", nullable = false, length = 36)
    private String orderUuid;

    @NotNull
    @Column(name = "update_time", nullable = false)
    private Timestamp updateTime;

    @NotNull
    @Column(name = "delivery_state", nullable = false)
    private String deliveryState;

    @PrePersist
    public void persist() {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        this.createTime = currentTime;
        this.updateTime = currentTime;
        this.enabled = false;

        this.orderUuid = UUID.randomUUID().toString();
    }

    @PreUpdate
    void update() {
        this.updateTime = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProductOrder that = (ProductOrder) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}