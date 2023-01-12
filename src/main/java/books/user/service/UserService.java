package books.user.service;

import books.order.domain.ProductOrder;
import books.user.common.RegistrationForm;
import books.user.domain.User;
import books.user.domain.UserAddress;
import books.user.domain.UserCreditCard;
import books.user.domain.UserPointHistory;

import java.util.List;
import java.util.Set;

public interface UserService {
    public User getUserInfo(User user);

    List<ProductOrder> getUserOrdersPage(User user, int page);

    ProductOrder getOrderDetail(String orderUuid);

    List<UserPointHistory> getUserPointHistoriesPage(User user);

    Set<UserAddress> getUserAddresses(User user);

    Set<UserCreditCard> getUserCreditCards(User user);

    public void processRegistration(RegistrationForm form);
}