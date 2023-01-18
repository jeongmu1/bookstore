package books.user.service;

import books.order.domain.ProductOrder;
import books.user.common.RegistrationForm;
import books.user.domain.User;
import books.user.domain.UserAddress;
import books.user.domain.UserCreditCard;
import books.user.domain.UserPointHistory;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    User getUserInfo(User user);

    List<ProductOrder> getUserOrdersPage(User user, int page);

    ProductOrder getOrderDetail(String orderUuid);

    List<UserPointHistory> getUserPointHistoriesPage(User user);

    Set<UserAddress> getUserAddresses(User user);

    Set<UserCreditCard> getUserCreditCards(User user);

    void processRegistration(RegistrationForm form);

    Optional<User> loadUserByUsername(String username);

}