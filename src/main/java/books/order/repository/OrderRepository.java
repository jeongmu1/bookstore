package books.order.repository;

import books.order.domain.ProductOrder;
import books.user.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OrderRepository extends CrudRepository<ProductOrder, Long> {
    public Optional<Set<ProductOrder>> findAllByUser(User user);

    public Optional<ProductOrder> findProductOrderByOrderUuid(String orderUuid);
    public Optional<List<ProductOrder>> findAllByUserOrderByCreateTimeDesc(User user, Pageable pageable);
}