package books.order.repository;

import books.order.domain.ProductOrder;
import books.user.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OrderRepository extends CrudRepository<ProductOrder, Long> {
    Optional<ProductOrder> findProductOrderByOrderUuid(String orderUuid);

    Optional<List<ProductOrder>> findAllByUserOrderByCreateTimeDesc(User user, Pageable pageable);

    Optional<ProductOrder> findProductOrderByUserAndEnabled(User user, boolean enabled);

    Integer countByUserAndDeliveryStateId(User user, int deliveryStateId);

    Set<ProductOrder> findAllByUser(User user);
}