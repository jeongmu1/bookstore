import books.order.domain.ProductOrder;
import books.order.repository.OrderRepository;
import books.user.domain.User;
import books.user.repository.UserRepository;
import books.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Set;


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
}
