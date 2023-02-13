package books.order.repository;

import books.order.domain.ProductOrder;
import books.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.Set;

public interface OrderRepository extends JpaRepository<ProductOrder, Long>, JpaSpecificationExecutor<ProductOrder> {
    Optional<ProductOrder> findProductOrderByUserAndEnabled(User user, boolean enabled);

    Integer countProductOrdersByUserAndDeliveryState(User user, String deliveryState);

    Set<ProductOrder> findAllByUser(User user);

}