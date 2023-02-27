package books.order.repository;

import books.order.domain.ProductOrder;
import books.order.domain.ProductOrderProduct;
import books.product.domain.ProductBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Set;

public interface CartRepository extends JpaRepository<ProductOrderProduct, Long>, JpaSpecificationExecutor<ProductOrderProduct> {
    List<ProductOrderProduct> findAllByProductOrder(ProductOrder productOrder);

    void deleteAllByProductOrder(ProductOrder productOrder);

    Integer countProductOrderProductsByProductOrder(ProductOrder order);

    Set<ProductOrderProduct> findByProductBook(ProductBook book);
}
