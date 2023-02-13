import books.order.domain.ProductOrder;
import books.order.domain.ProductOrderProduct;
import books.order.repository.OrderRepository;
import books.product.repository.ProductBookRepository;
import books.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = TestConfiguration.class)
class OrderTest {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductBookRepository productBookRepository;

    @Test
    void orderProductTest() {
        ProductOrder order = new ProductOrder();
        order.setUser(userRepository.findByUsername("admin"));
        orderRepository.save(order);

        ProductOrderProduct item = new ProductOrderProduct();
        item.setProductOrder(order);
        item.setProductBook(productBookRepository.findProductBookById(1L));
        item.setProductCount(2);

        System.out.println(order.getId());
        System.out.println(item.getId());
    }
}
