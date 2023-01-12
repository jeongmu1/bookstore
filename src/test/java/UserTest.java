import books.user.domain.User;
import books.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;


@SpringBootTest
@ContextConfiguration(classes = TestConfiguration.class)
class UserTest {

    @Autowired
    private UserService userService;

    @Test
    void findPointHistoriesTest() throws Exception {
        User user = new User();
        user.setUsername("admin");

        userService.getUserPointHistoriesPage(user).get(0).getPointHistoryDetail();
    }
}
