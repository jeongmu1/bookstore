import books.admin.service.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = TestConfiguration.class)
class AdminTest {
    @Autowired
    private AdminService adminService;

    @Test
    void addBookTest() {

    }
}
