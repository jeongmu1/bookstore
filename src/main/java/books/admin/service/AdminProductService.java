package books.admin.service;

import books.product.domain.ProductBook;
import books.product.domain.ProductCategory;
import books.product.repository.ProductBookRepository;
import books.product.repository.ProductCategoryRepository;
import books.product.repository.ProductImageRepository;
import books.product.repository.PublisherRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AdminProductService {
    private final ProductBookRepository bookRepo;
    private final ProductCategoryRepository productCategoryRepo;
    private final ProductImageRepository productImageRepo;
    private final PublisherRepository publisherRepo;

    public AdminProductService(ProductBookRepository bookRepo, ProductCategoryRepository productCategoryRepo, ProductImageRepository productImageRepo, PublisherRepository publisherRepo) {
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
