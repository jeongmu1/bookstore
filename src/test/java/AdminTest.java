import books.admin.service.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
@ContextConfiguration(classes = TestConfiguration.class)
class AdminTest {
    @Autowired
    private AdminService adminService;

    @Test
    void findOrdersTest() {
        Set<String> deliveryStateIds = new HashSet<>();
        deliveryStateIds.add("주문전");
        String searchCondition = "username";
        String keyword = "admin";
        adminService.findOrderInfoByConditions(deliveryStateIds, searchCondition, keyword)
                        .forEach(System.out::println);
//        System.out.println(adminService.findOrderInfoByConditions(deliveryStateIds, searchCondition, keyword));
    }
}
