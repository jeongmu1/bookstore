package books.product.repository;

import books.product.domain.ProductBook;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductBookRepository extends JpaRepository<ProductBook, Long>, JpaSpecificationExecutor<ProductBook> {

    ProductBook findProductBookById(Long id);

    List<ProductBook> findProductBooksByDisplay(boolean display, Pageable pageable);

    List<ProductBook> findProductBooksByTitleContaining(String title);

    List<ProductBook> findProductBooksByAuthorContaining(String author);

    List<ProductBook> findProductBooksByPublisherNameContaining(String publisher);

    List<ProductBook> findProductBooksByTitleContainingOrAuthorContainingOrPublisherNameContaining(String title, String author, String publisher);

    @Modifying
    @Query("update ProductBook pb set pb.stock = :stock where pb.id = :id")
    void updateStockById(@Param("id") Long id, @Param("stock") int stock);
}
