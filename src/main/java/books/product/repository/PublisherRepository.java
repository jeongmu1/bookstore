package books.product.repository;

import books.product.domain.ProductBook;
import books.product.domain.Publisher;
import org.springframework.data.repository.CrudRepository;

public interface PublisherRepository extends CrudRepository<Publisher, Long> {

    public Publisher findPublisherByProductBooks(ProductBook book);

}
