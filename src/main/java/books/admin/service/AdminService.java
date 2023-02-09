package books.admin.service;

import books.admin.common.ProductBookForm;
import books.product.domain.Category;
import books.product.domain.ProductBook;
import books.product.domain.Publisher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AdminService {

    public void addProduct(ProductBookForm book, HttpServletRequest request);

    public List<Category> findAllCategories();
    public List<Publisher> findAllPublishers();
}
