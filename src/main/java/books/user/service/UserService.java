package books.user.service;

import books.admin.common.OrderInfoDto;
import books.admin.common.UserInfoDto;
import books.product.domain.ProductReview;
import books.user.common.*;
import books.user.domain.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;

public interface UserService {
    UserDto findUserInfo(Principal principal);

    void processRegistration(RegistrationForm form);

    User loadUserByUsername(String username);

    List<PointHistoryDto> findUserPointHistory(Principal principal);

    UserInfoDto findUserInfoDto(String username);

    UserUpdateForm initializeUserUpdateFormByUsername(String username);

    void updateUserByForm(String username, UserUpdateForm updateForm);

    void deleteUserByUsername(String username);

    List<ProductReview> findProductReviewsByUsername(String username);

    ProductReview findProductReviewForUpdateById(String username, Long id) throws IllegalAccessException;

    void updateProductReview(String username, ProductReviewForm form) throws IllegalAccessException;

    void deleteProductReviewById(String username, Long id) throws IllegalAccessException;

    List<OrderInfoDto> findOrderInfos(String username, Set<String> deliveryStates, String searchCriteria, String keyword);

    List<String> findAllDeliveryStates();
}