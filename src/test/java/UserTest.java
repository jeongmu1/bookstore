import books.order.domain.ProductOrder;
import books.order.repository.OrderRepository;
import books.user.domain.User;
import books.user.repository.UserRepository;
import books.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;


@Slf4j
@SpringBootTest
@ContextConfiguration(classes = TestConfiguration.class)
class UserTest {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

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
}
