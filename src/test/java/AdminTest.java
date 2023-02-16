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
        deliveryStateIds.add("준비중");
        String searchCondition = "username";
        String keyword = "admin";

        adminService.findOrderInfoByConditions(deliveryStateIds, searchCondition, keyword)
                        .forEach(System.out::println);
    }

    @Test
    void updateOrdersTest() {
        Set<Long> popIds = new HashSet<>();
        popIds.add(39L);
        adminService.updateDeliveryState(popIds, "배송중");
    }

    @Test
    void findUserInfoTest() {
        String authority = null;
        String enabled = null;
        String criteria = "name";
        String keyword = "관리자";

        adminService.findUserInfoByConditions(authority, enabled, criteria, keyword)
                .forEach(System.out::println);
    }
}
