package books.admin.service;

import books.book.domain.ProductBook;
import books.book.domain.ProductCategory;
import books.book.repository.BookRepository;
import books.book.repository.ProductCategoryRepository;
import books.book.repository.ProductImageRepository;
import books.book.repository.PublisherRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AdminProductService {
    private final BookRepository bookRepo;
    private final ProductCategoryRepository productCategoryRepo;
    private final ProductImageRepository productImageRepo;
    private final PublisherRepository publisherRepo;

    public AdminProductService(BookRepository bookRepo, ProductCategoryRepository productCategoryRepo, ProductImageRepository productImageRepo, PublisherRepository publisherRepo) {
        this.bookRepo = bookRepo;
        this.productCategoryRepo = productCategoryRepo;
        this.productImageRepo = productImageRepo;
        this.publisherRepo = publisherRepo;
    }

    public void addProduct(ProductBook book, Map<String, Object> params) {
        ProductCategory productCategory = new ProductCategory();
        String category = params.get("category").toString();
        productCategory.setProductBook(book);
    }
}
