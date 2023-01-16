package books.product.service;

import books.product.domain.Category;
import books.product.domain.ProductBook;
import books.product.domain.ProductCategory;
import books.product.domain.ProductReview;
import books.product.repository.*;
import books.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ShopServiceImpl implements ShopService {
    private final ProductBookRepository productBookRepo;
    private final ProductCategoryRepository productCategoryRepo;
    private final ProductImageRepository productImageRepo;
    private final ProductReviewRepository productReviewRepo;
    private final PublisherRepository publisherRepo;
    private final CategoryRepository categoryRepo;
    private final UserRepository userRepo;

    @Autowired
    public ShopServiceImpl(ProductBookRepository productBookRepo
            , ProductCategoryRepository productCategoryRepo
            , ProductImageRepository productImageRepo
            , ProductReviewRepository productReviewRepo
            , PublisherRepository publisherRepo
            , CategoryRepository categoryRepo
            , UserRepository userRepo) {
        this.productBookRepo = productBookRepo;
        this.productCategoryRepo = productCategoryRepo;
        this.productImageRepo = productImageRepo;
        this.productReviewRepo = productReviewRepo;
        this.publisherRepo = publisherRepo;
        this.categoryRepo = categoryRepo;
        this.userRepo = userRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductBook getProductDetails(Long productId) {
        ProductBook book = productBookRepo.findProductBookById(productId);
        book.setProductCategories(productCategoryRepo.findAllByProductBook(book));
        book.setProductImages(productImageRepo.findAllByProductBook(book));
        book.setProductReviews(productReviewRepo.findAllByProductBook(book));

        return book;
    }

    @Override
    public Set<Category> getCategoriesByBook(ProductBook book) {
        return book
                .getProductCategories()
                .stream()
                .map(ProductCategory::getCategory)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public double getProductReviewRate(ProductBook book) {
        double average = book.getProductReviews()
                .stream()
                .mapToDouble(ProductReview::getProductScore)
                .average()
                .orElse(0);
        if (average != 0) average = (double) Math.round(average * 10) / 10;
        return average;
    }

    @Override
    public Set<ProductReview> setUserInProductReviews(Set<ProductReview> reviews) {
        reviews.forEach(review -> review.setUser(userRepo.findUserByProductReviews(review)));
        return reviews;
    }
}