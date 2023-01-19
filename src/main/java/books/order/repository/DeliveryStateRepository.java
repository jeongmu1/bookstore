package books.order.repository;

import books.order.domain.DeliveryState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryStateRepository extends JpaRepository<DeliveryState, Long> {
    DeliveryState findDeliveryStateById(Integer id);
}
