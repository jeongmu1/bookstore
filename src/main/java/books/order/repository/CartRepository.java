package books.order.repository;

import books.order.domain.ProductOrder;
import books.order.domain.ProductOrderProduct;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface CartRepository extends CrudRepository<ProductOrderProduct, Long> {
    public Set<ProductOrderProduct> findAllByProductOrder(ProductOrder productOrder);
}
