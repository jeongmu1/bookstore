package books.admin.service;

import books.product.domain.Category;
import books.product.domain.ProductBook;
import books.product.domain.Publisher;
import books.product.repository.CategoryRepository;
import books.product.repository.PublisherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService{
    private final CategoryRepository categoryRepo;
    private final PublisherRepository publisherRepo;

    public AdminServiceImpl(CategoryRepository categoryRepo, PublisherRepository publisherRepo) {
        this.categoryRepo = categoryRepo;
        this.publisherRepo = publisherRepo;
    }

    @Override
    public void addProduct(ProductBook book) {

    }

    @Override
    public List<Category> findAllCategories() {
        return categoryRepo.findAll();
    }

    @Override
    public List<Publisher> findAllPublishers() {
        return publisherRepo.findAll();
    }
}
