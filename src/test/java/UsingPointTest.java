import books.common.PointProps;
import books.order.common.*;
import books.order.service.OrderService;
import books.product.domain.ProductBook;
import books.product.repository.ProductBookRepository;
import books.product.service.CartService;
import books.user.domain.User;
import books.user.repository.UserPointHistoryRepository;
import books.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;


@Slf4j
@SpringBootTest
@ContextConfiguration(classes = TestConfiguration.class)
@Transactional
class UsingPointTest {
    @Autowired
    private UserPointHistoryRepository userPointHistoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductBookRepository productBookRepository;
    @Autowired
    private PointProps pointProps;
    @Autowired
    private CartService cartService;

    @Test
    @Transactional
    void testUsePoint() throws Exception {
        User user = userRepository.findByUsername("test2");
        user.setPoint(5000);
        user.setPointStamp(10);
        OrderForm orderForm = getTemporaryOrderForm(1000);
        int beforeHistorySize = user.getUserPointHistories().size();
        orderService.addOrderByProduct(orderForm, user.getUsername(), 1L, 1);

        assertThat(user.getPoint(), is(4000));
        assertThat(user.getPointStamp(), is(0));
        assertThat(userPointHistoryRepository.findAllByUser(user).size(), is(beforeHistorySize + 1));
    }

    @Test
    @Transactional
    void testSavePoint() throws Exception {
        Long bookId = 1L;
        int quantity = 3;
        int beforePoint = 5000;

        ProductBook book = productBookRepository.findProductBookById(bookId);
        User user = userRepository.findByUsername("test2");
        user.setPoint(beforePoint);
        int beforePointStamp = user.getPointStamp();


        OrderForm orderForm = getTemporaryOrderForm(null);
        log.info("Before Point : " + user.getPoint());

        orderService.addOrderByProduct(orderForm, user.getUsername(), bookId, quantity);

        assertThat(user.getPoint(), is(beforePoint + book.getPrice() / pointProps.getSavingRate()));
        log.info("After Point : " + user.getPoint());
        assertThat(user.getPointStamp(), is(beforePointStamp + quantity));
    }

    @Test
    @Transactional
    void shouldThrowExceptionWhenNotEnoughPointStamp() throws Exception {
        int defaultPoint = 5000;
        int usingPoint = 1000;
        int defaultPointStamp = 9;
        Long productBookId = 1L;
        int quantity = 1;

        User user = userRepository.findByUsername("test2");
        user.setPoint(defaultPoint);
        user.setPointStamp(defaultPointStamp);
        OrderForm orderForm = getTemporaryOrderForm(usingPoint);
        int beforeHistorySize = user.getUserPointHistories().size();

        assertThrows(NotEnoughPointStampException.class, () ->
                orderService.addOrderByProduct(orderForm, user.getUsername(), productBookId, quantity));
        assertThat(user.getPoint(), is(defaultPoint));
        assertThat(user.getPointStamp(), is(defaultPointStamp));
        assertThat(userPointHistoryRepository.findAllByUser(user).size(), is(beforeHistorySize));
    }

    @Test
    @Transactional
    void shouldThrowExceptionWhenNotEnoughPoints() {
        User user = userRepository.findByUsername("test2");
        user.setPoint(5000);

        OrderForm orderForm = getTemporaryOrderForm(6000);
        assertThrows(NotEnoughPointException.class, () ->
                orderService.addOrderByProduct(orderForm, user.getUsername(), 1L, 1));

    }

    @Test
    @Transactional
    void shouldThrowExceptionWhenOutOfUnitPointUsage() {
        User user = userRepository.findByUsername("test2");
        user.setPoint(5000);

        OrderForm orderForm = getTemporaryOrderForm(1234);
        assertThrows(OutOfUnitPointUsage.class, () ->
                orderService.addOrderByProduct(orderForm, user.getUsername(), 1L, 1));
    }

    @Test
    @Transactional
    void shouldThrowExceptionWhenTooMuchPoints() {
        User user = userRepository.findByUsername("test2");
        user.setPoint(60000);
        OrderForm orderForm = getTemporaryOrderForm(50000);
        assertThrows(TooMuchPointsException.class, () ->
                orderService.addOrderByProduct(orderForm, user.getUsername(), 1L, 1));
    }

    private OrderForm getTemporaryOrderForm(Integer usingPoint) {
        return OrderForm.builder()
                .ccNumber("7173631156263632")
                .ccExpiration("12/12")
                .ccCvv("123")
                .deliveryName("테스트수취인")
                .deliveryAddress("주소")
                .deliveryZipCode("우편번호")
                .phone("010-1234-1234")
                .usingPoint(usingPoint)
                .build();
    }
}
