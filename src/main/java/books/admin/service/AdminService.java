package books.admin.service;

import books.product.domain.Category;
import books.product.domain.ProductBook;
import books.product.domain.Publisher;

import java.util.List;

public interface AdminService {

    public void addProduct(ProductBook book);

    public List<Category> findAllCategories();
    public List<Publisher> findAllPublishers();
}
