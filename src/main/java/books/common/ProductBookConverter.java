package books.common;

import books.home.common.ProductBookDto;
import books.product.domain.ProductBook;
import books.product.domain.ProductReview;

public class ProductBookConverter {

    public static ProductBookDto convertToDto(ProductBook book) {
        return ProductBookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher().getName())
                .price(book.getPrice())
                .rate(book.getProductReviews()
                        .stream()
                        .mapToDouble(ProductReview::getProductScore)
                        .average()
                        .orElse(0))
                .description(book.getDescription())
                .sizeOfReviews(book.getProductReviews().size())
                .imageFileName(book.getProductImages()
                        .stream()
                        .findFirst()
                        .orElseThrow()
                        .getFileName())
                .enabled(book.isEnabled())
                .outOfStock(book.getStock() < 1)
                .reviews(book.getProductReviews())
                .build();
    }
}
