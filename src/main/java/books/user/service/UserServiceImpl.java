package books.user.service;

import books.common.PageSizeProps;
import books.user.domain.*;
import books.order.domain.ProductOrder;
import books.order.repository.CartRepository;
import books.order.repository.OrderRepository;
import books.user.common.RegistrationForm;
import books.user.repository.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final UserAuthorityRepository authorityRepo;
    private final UserAddressRepository userAddressRepo;
    private final UserCCRepository userCCRepo;
    private final CartRepository cartRepo;
    private final OrderRepository orderRepo;
    private final PasswordEncoder encoder;
    private final UserPointHistoryRepository userPointHistoryRepo;
    private final PageSizeProps pageSizeProps;

    public UserServiceImpl(UserRepository userRepo
            , UserAuthorityRepository authorityRepo
            , UserAddressRepository userAddressRepo
            , UserCCRepository userCCRepo, CartRepository cartRepo
            , OrderRepository orderRepo, PasswordEncoder encoder
            , UserPointHistoryRepository userPointHistoryRepo
            , PageSizeProps pageSizeProps) {
        this.userRepo = userRepo;
        this.authorityRepo = authorityRepo;
        this.userAddressRepo = userAddressRepo;
        this.userCCRepo = userCCRepo;
        this.cartRepo = cartRepo;
        this.orderRepo = orderRepo;
        this.encoder = encoder;
        this.userPointHistoryRepo = userPointHistoryRepo;
        this.pageSizeProps = pageSizeProps;
    }

    @Override
    public User getUserInfo(User user) {

        return user;
    }

    @Override
    public List<ProductOrder> getUserOrdersPage(User user, int page) {
        return orderRepo
                .findAllByUserOrderByCreateTimeDesc(user, PageRequest.of(page - 1, pageSizeProps.getUserOrders()))
                .orElse(null);
    }

    @Override
    public ProductOrder getOrderDetail(String orderUuid) {
       return orderRepo.findProductOrderByOrderUuid(orderUuid)
                .map(this::applyProductOrder).orElse(null);
    }

    @Override
    public List<UserPointHistory> getUserPointHistoriesPage(User user) {
        return userPointHistoryRepo
                .findAllByUserOrderByCreateTimeDesc(user)
                .orElse(null);
    }

    @Override
    public Set<UserAddress> getUserAddresses(User user) {
        return userAddressRepo
                .findAllByUser(user)
                .orElse(null);
    }

    @Override
    public Set<UserCreditCard> getUserCreditCards(User user) {
        return userCCRepo
                .findAllByUser(user)
                .orElse(null);
    }

    @Override
    public void processRegistration(RegistrationForm form) {
        userRepo.save(form.toUser(encoder));
        userRepo.findByUsername(form.getUsername()).ifPresent(this::addUserAuthority);
    }

    private void addUserAuthority(User user) {
        Authority auth = new Authority();
        auth.setUser(user);
        auth.setAuthority("ROLE_USER");
        authorityRepo.save(auth);
    }

    private ProductOrder applyProductOrder(ProductOrder productOrder) {
        productOrder.setProductOrderProducts(cartRepo.findAllByProductOrder(productOrder));
        return productOrder;
    }
}