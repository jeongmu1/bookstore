package books.order.repository;

import books.order.domain.ProductOrder;
import books.order.domain.ProductOrderProduct;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CartRepository extends CrudRepository<ProductOrderProduct, Long>, JpaSpecificationExecutor<ProductOrderProduct> {
    List<ProductOrderProduct> findAllByProductOrder(ProductOrder productOrder);

    void deleteAllByProductOrder(ProductOrder productOrder);

    Integer countProductOrderProductsByProductOrder(ProductOrder order);
}
