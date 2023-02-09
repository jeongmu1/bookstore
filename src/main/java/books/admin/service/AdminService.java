package books.admin.service;

import books.admin.common.ProductBookForm;
import books.product.domain.Category;
import books.product.domain.Publisher;

import java.util.List;

public interface AdminService {

    void addProduct(ProductBookForm book);

    List<Category> findAllCategories();
    List<Publisher> findAllPublishers();
}
