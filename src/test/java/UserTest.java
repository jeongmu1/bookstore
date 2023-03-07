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
import java.util.List;
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

        Set<ProductOrder> orderList = orderRepository.findAllByUser(userRepository.findByUsername(username));
        for (ProductOrder order : orderList) {
            for (ProductOrderProduct pop : order.getProductOrderProducts()) {
                pop.setDeliveryState(deliveryCompleted);
                userService.updateDeliveryStateToConfirmed(username, pop.getId());
            }
        }

        orderList.forEach(order -> {
            log.info("id : " + order.getId() + "\tdeliveryState : " + order.getDeliveryState());
            assertThat(order.getDeliveryState(), is(equalTo(confirmed)));
        });
    }


}
