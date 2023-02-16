package books.admin.service;

import books.admin.common.OrderInfoDto;
import books.admin.common.ProductBookForm;
import books.admin.common.UserInfoDto;
import books.admin.common.UserUpdateForm;
import books.common.DeliveryState;
import books.product.domain.Category;
import books.product.domain.Publisher;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AdminService {

    void addProduct(ProductBookForm book);

    List<Category> findAllCategories();

    List<Publisher> findAllPublishers();

    List<String> findAllDeliveryStates();

    List<OrderInfoDto> findOrderInfoByConditions(Set<String> deliveryStates, String searchCriteria, String keyword);

    void updateDeliveryState(Set<Long> productOrderProductIds, String deliveryState);

    List<UserInfoDto> findUserInfoByConditions(String authority, String enabled, String searchCriteria, String keyword);

    UserInfoDto findUserById(Long id);

    void deleteUserById(Long userId);

    void updateUser(UserUpdateForm updateForm);

    UserUpdateForm initializeUserUpdateForm(Long userId);
}
