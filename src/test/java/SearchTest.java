import books.product.service.ShopService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = TestConfiguration.class)
class SearchTest {
    @Autowired
    ShopService shopService;

    @Test
    void searchCategoryTest() {
        System.out.println(shopService.findProductsBySearch("20", 4));
    }
}
