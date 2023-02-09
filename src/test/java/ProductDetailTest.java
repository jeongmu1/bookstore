import books.product.domain.*;
import books.product.service.ShopService;
import books.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Set;

@SpringBootTest
@ContextConfiguration(classes = TestConfiguration.class)
class ProductDetailTest {
    @Autowired
    private ShopService shopService;
}
