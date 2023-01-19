import books.home.service.HomeService;
import books.home.service.HomeServiceImpl;
import books.product.domain.ProductBook;
import books.product.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;


@SpringBootTest
@ContextConfiguration(classes = TestConfiguration.class)
class HomeServiceTest {
    @Autowired
    private HomeServiceImpl homeService;

    @Test
    void queryTest() {
        System.out.println(homeService.findAllCategories());
        System.out.println(homeService.findDisplayBooksForDTOs());
    }

}
