package books.product.service.impl;

import books.home.common.ProductBookDto;
import books.product.domain.*;
import books.product.repository.*;
import books.product.service.ShopService;
import books.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public ShopServiceImpl(ProductBookRepository productBookRepo
            , ProductCategoryRepository productCategoryRepo
            , ProductImageRepository productImageRepo
            , ProductReviewRepository productReviewRepo
            , UserRepository userRepo) {
        this.productBookRepo = productBookRepo;
        this.productCategoryRepo = productCategoryRepo;
        this.productImageRepo = productImageRepo;
        this.productReviewRepo = productReviewRepo;
        this.userRepo = userRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductBook findProductDetails(Long productId) {
        ProductBook book = productBookRepo.findProductBookById(productId);
        book.setProductCategories(productCategoryRepo.findAllByProductBook(book));
        book.setProductImages(productImageRepo.findAllByProductBook(book));
        book.setProductReviews(productReviewRepo.findAllByProductBook(book));

        return book;
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
     * @param param 검색어
     * @param type  1:제목 2:작가 3:출판사 4:카테코리 5:통합
     * @return 검색된 제품 정보들 반환
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductBookDto> findProductsBySearch(String param, int type) {
        switch (type) {
            case 1:
                return searchTitle(param);
            case 2:
                return searchAuthor(param);
            case 3:
                return searchPublisher(param);
            case 4:
                return searchCategory(param);
            case 5:
                return searchTotal(param);
            default:
                throw new InvalidParameterException();
        }
    }

    private List<ProductBookDto> searchTotal(String param) {
        List<ProductBook> books = productBookRepo.findProductBooksByTitleContainingOrAuthorContainingOrPublisherNameContaining(param, param, param);
        return productBooksToDto(books);
    }

    private List<ProductBookDto> searchTitle(String param) {
        List<ProductBook> books = productBookRepo.findProductBooksByTitleContaining(param);
        return productBooksToDto(books);
    }

    private List<ProductBookDto> searchAuthor(String param) {
        List<ProductBook> books = productBookRepo.findProductBooksByAuthorContaining(param);
        return productBooksToDto(books);
    }

    private List<ProductBookDto> searchPublisher(String param) {
        List<ProductBook> books = productBookRepo.findProductBooksByPublisherNameContaining(param);
        return productBooksToDto(books);
    }

    private List<ProductBookDto> searchCategory(String param) {
        return productCategoryRepo
                .findProductCategoriesByCategoryId(Long.parseLong(param))
                .stream()
                .map(category -> productBookToDto(category.getProductBook()))
                .collect(Collectors.toList());
    }

    private ProductImage findProductImageByBook(ProductBook book) {
        return productImageRepo
                .findAllByProductBook(book)
                .stream()
                .filter(ProductImage::isEnabled)
                .findFirst()
                .orElse(null);
    }

    private List<ProductBookDto> productBooksToDto(List<ProductBook> productBooks) {
        return productBooks
                .stream()
                .map(this::productBookToDto)
                .collect(Collectors.toList());
    }

    private ProductBookDto productBookToDto(ProductBook productBook) {
        ProductBookDto productBookDto = new ProductBookDto(productBook, findProductImageByBook(productBook));
        productBookDto.setRate(findProductReviewRate(productBook));
        productBookDto.setSizeOfReviews(productBook.getProductReviews().size());
        return productBookDto;
    }
}