package books.admin.service;

import books.admin.common.OrderInfoDto;
import books.admin.common.ProductBookForm;
import books.admin.common.UserInfoDto;
import books.admin.common.UserUpdateFormForAdmin;
import books.admin.common.ProductBookDto;
import books.product.domain.Category;
import books.product.domain.ProductReview;
import books.product.domain.Publisher;
import books.user.common.UserUpdateForm;

import java.util.List;
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

    void updateUser(UserUpdateFormForAdmin updateForm);

    UserUpdateFormForAdmin initializeUserUpdateForm(Long userId);

    List<ProductBookDto> findProductBookByConditions(String searchCriteria,
                                                     String keyword,
                                                     Boolean enabled);

    void updateProductStock(List<ProductBookDto> books);

    ProductBookForm getProductBookFormById(Long id);

    void updateProductBook(ProductBookForm bookForm);

    List<ProductReview> findProductReviewByConditions(String searchCriteria, String keyword);

    void deleteProductReviewById(Long id);
}
