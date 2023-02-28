import books.product.repository.ProductBookRepository;
import books.product.repository.ProductReviewRepository;
import books.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Logger;


@Slf4j
@SpringBootTest
@ContextConfiguration(classes = TestConfiguration.class)
class ProductDetailTest {
    private final ProductBookRepository productBookRepository;
    private final ProductReviewRepository productReviewRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProductDetailTest(ProductBookRepository productBookRepository, ProductReviewRepository productReviewRepository, UserRepository userRepository) {
        this.productBookRepository = productBookRepository;
        this.productReviewRepository = productReviewRepository;
        this.userRepository = userRepository;
    }

    @Test
    @Transactional(readOnly = true)
    void testQuery() {
        // 1차캐시 테스트
        String username = "admin";
        Long productBookId = 1L;
        log.info(productReviewRepository.countProductReviewsByUserAndProductBook(userRepository.findByUsername(username), productBookRepository.findProductBookById(productBookId)).toString());
        log.info(productBookRepository.findProductBookById(productBookId).toString());
    }
}
