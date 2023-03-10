package books.product.service.impl;

import books.common.EntityConverter;
import books.common.ReviewProps;
import books.home.common.ProductBookDto;
import books.product.domain.*;
import books.product.repository.*;
import books.product.service.ShopService;
import books.user.common.ProductReviewForm;
import books.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ShopServiceImpl implements ShopService {
    private final ProductBookRepository productBookRepo;
    private final ProductCategoryRepository productCategoryRepo;
    private final ProductImageRepository productImageRepo;
    private final ProductReviewRepository productReviewRepo;
    private final UserRepository userRepo;
    private final ReviewProps reviewProps;

    public ShopServiceImpl(ProductBookRepository productBookRepo, ProductCategoryRepository productCategoryRepo, ProductImageRepository productImageRepo, ProductReviewRepository productReviewRepo, UserRepository userRepo, ReviewProps reviewProps) {
        this.productBookRepo = productBookRepo;
        this.productCategoryRepo = productCategoryRepo;
        this.productImageRepo = productImageRepo;
        this.productReviewRepo = productReviewRepo;
        this.userRepo = userRepo;
        this.reviewProps = reviewProps;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductBookDto findProductDetails(Long productId) {
        ProductBook book = productBookRepo.findProductBookById(productId);
        book.setProductCategories(productCategoryRepo.findAllByProductBook(book));
        book.setProductImages(productImageRepo.findAllByProductBook(book));
        book.setProductReviews(productReviewRepo.findAllByProductBook(book));
        return EntityConverter.convertProductBook(book);
    }

    @Override
    public Set<Category> findCategoriesByBook(ProductBook book) {
        return book
                .getProductCategories()
                .stream()
                .map(ProductCategory::getCategory)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public double findProductReviewRate(ProductBook book) {
        double average = book.getProductReviews()
                .stream()
                .mapToDouble(ProductReview::getProductScore)
                .average()
                .orElse(0);
        if (average != 0) average = (double) Math.round(average * 10) / 10;
        return average;
    }

    @Override
    public Set<ProductReview> findUserInProductReviews(Set<ProductReview> reviews) {
        reviews.forEach(review -> review.setUser(userRepo.findUserByProductReviews(review)));
        return reviews;
    }

    /**
     * @param param ?????????
     * @param type  1:?????? 2:?????? 3:????????? 4:???????????? 5:??????
     * @return ????????? ?????? ????????? ??????
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductBookDto> findProductsBySearch(String param, int type) {
        switch (type) {
            case 1:
                return productBooksToDto(productBookRepo.findProductBooksByTitleContaining(param));
            case 2:
                return productBooksToDto(productBookRepo.findProductBooksByAuthorContaining(param));
            case 3:
                return productBooksToDto(productBookRepo.findProductBooksByPublisherNameContaining(param));
            case 4:
                return productCategoryRepo
                        .findProductCategoriesByCategoryId(Long.parseLong(param))
                        .stream()
                        .map(category -> EntityConverter.convertProductBook(category.getProductBook()))
                        .collect(Collectors.toList());
            case 5:
                return productBooksToDto(productBookRepo.findProductBooksByTitleContainingOrAuthorContainingOrPublisherNameContaining(param, param, param));
            default:
                throw new InvalidParameterException();
        }
    }

    private List<ProductBookDto> productBooksToDto(List<ProductBook> productBooks) {
        return productBooks
                .stream()
                .map(EntityConverter::convertProductBook)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addProductReview(String username, ProductReviewForm reviewForm) {
        ProductBook book = productBookRepo.findProductBookById(reviewForm.getProductBookId());
        // ?????? ???????????? ??????
        if (productReviewRepo.countProductReviewsByUserAndProductBook(
                userRepo.findByUsername(username), book) > 0){
            throw new  DuplicateKeyException("User can write only one review per product.");
        }
        // ?????? ?????? ?????? ??????
        qualifyProductScore(reviewForm.getProductScore());

        ProductReview review = new ProductReview();
        review.setProductScore(reviewForm.getProductScore());
        review.setProductBook(book); // 1????????? ??????
        review.setUser(userRepo.findByUsername(username));
        review.setComment(reviewForm.getComment());

        productReviewRepo.save(review);
    }

    private void qualifyProductScore(Byte productScore) {
        if (productScore < reviewProps.getMinScore() || productScore > reviewProps.getMaxScore()) {
            throw new IllegalArgumentException("productScore is out of range");
        }
    }

    @Override
    public void checkDuplicateReview(String username, Long productId) throws IllegalAccessException {
        if (productReviewRepo.countProductReviewsByUserAndProductBook(userRepo.findByUsername(username), productBookRepo.findById(productId).orElseThrow()) > 0) {
            throw new IllegalAccessException();
        }
    }
}