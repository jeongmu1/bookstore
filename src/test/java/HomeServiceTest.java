import books.home.service.HomeServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;



@SpringBootTest
@ContextConfiguration(classes = TestConfiguration.class)
class HomeServiceTest {
    @Autowired
    private HomeServiceImpl homeService;

    @Test
    void queryTest() {
        System.out.println(homeService.findAllCategories());
        System.out.println(homeService.findDisplayBooksForDtos());
    }

}
