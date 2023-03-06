import books.common.DeliveryState;
import books.order.domain.ProductOrder;
import books.order.domain.ProductOrderProduct;
import books.order.repository.CartRepository;
import books.order.repository.OrderRepository;
import books.user.domain.User;
import books.user.repository.UserRepository;
import books.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.transaction.Transaction;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.HashSet;
import java.util.Set;


@Slf4j
@SpringBootTest
@ContextConfiguration(classes = TestConfiguration.class)
@Transactional
class UserTest {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private CartRepository cartRepository;

    @Test
    void queryTest() {
        User user = userRepository.findByUsername("admin");
        Set<ProductOrder> order = orderRepository.findAllByUser(user);
    }

    @Test
    void queryTest2() {
        User user = userRepository.findByUsername("admin");
    }

    @Test
    void findOrderInfosTest() {
        String username = "admin";
        String searchCriteria = "";
        String keyword = "";
        Set<String> deliveryStates = new HashSet<>();
        deliveryStates.add("배송중");
        deliveryStates.add("주문전");

        userService.findOrderInfos(username, deliveryStates,searchCriteria, keyword).forEach(orderInfoDto -> log.info(orderInfoDto.toString()));
    }

    @Test
    @Transactional
    void confirmDeliveryStateTest() throws IllegalAccessException {
        String deliveryCompleted = DeliveryState.DELIVERY_COMPLETED.toString();
        String confirmed = DeliveryState.CONFIRMED.toString();
        String username = "admin";

        Set<ProductOrder> orders = new HashSet<>();

        // orderId == 14, pop.size == 2
        ProductOrder order1 = orderRepository.findById(14L).get();
        ProductOrderProduct pop1_1 = order1.getProductOrderProducts().get(0);
        ProductOrderProduct pop1_2 = order1.getProductOrderProducts().get(1);
        orders.add(order1);

        pop1_1.setDeliveryState(deliveryCompleted);
        pop1_2.setDeliveryState(deliveryCompleted);

        // orderId == 25, pop.size == 2
        ProductOrder order2 = orderRepository.findById(25L).get();
        ProductOrderProduct pop2_1 = order2.getProductOrderProducts().get(0);
        ProductOrderProduct pop2_2 = order2.getProductOrderProducts().get(1);
        orders.add(order2);

        pop2_1.setDeliveryState(deliveryCompleted);
        pop2_2.setDeliveryState(deliveryCompleted);

        // orderId == 15, pop.size == 1
        ProductOrder order3 = orderRepository.findById(15L).get();
        ProductOrderProduct pop3_1 = order3.getProductOrderProducts().get(0);
        orders.add(order3);

        pop3_1.setDeliveryState(deliveryCompleted);

        userService.updateDeliveryStateToConfirmed(username, pop1_1.getId());
        userService.updateDeliveryStateToConfirmed(username, pop1_2.getId());
        userService.updateDeliveryStateToConfirmed(username, pop2_1.getId());
        userService.updateDeliveryStateToConfirmed(username, pop2_2.getId());
        userService.updateDeliveryStateToConfirmed(username, pop3_1.getId());

        orders.forEach(order -> {
            assertThat(order.getDeliveryState(), is(equalTo(confirmed)));
            log.info(order.getDeliveryState());
        });
    }

}
