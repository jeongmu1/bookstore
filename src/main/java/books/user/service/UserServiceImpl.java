package books.user.service;

import books.common.DeliveryState;
import books.common.PageSizeProps;
import books.product.repository.ProductReviewRepository;
import books.user.common.PointHistoryDto;
import books.user.common.UserDto;
import books.user.domain.*;
import books.order.repository.CartRepository;
import books.order.repository.OrderRepository;
import books.user.common.RegistrationForm;
import books.user.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final UserAuthorityRepository authorityRepo;
    private final CartRepository cartRepo;
    private final OrderRepository orderRepo;
    private final PasswordEncoder encoder;
    private final UserPointHistoryRepository userPointHistoryRepo;
    private final ProductReviewRepository productReviewRepository;

    public UserServiceImpl(UserRepository userRepo, UserAuthorityRepository authorityRepo, CartRepository cartRepo, OrderRepository orderRepo, PasswordEncoder encoder, UserPointHistoryRepository userPointHistoryRepo, ProductReviewRepository productReviewRepository) {
        this.userRepo = userRepo;
        this.authorityRepo = authorityRepo;
        this.cartRepo = cartRepo;
        this.orderRepo = orderRepo;
        this.encoder = encoder;
        this.userPointHistoryRepo = userPointHistoryRepo;
        this.productReviewRepository = productReviewRepository;
    }

    @Override
    public UserDto findUserInfo(Principal principal) {
        return principalToDto(principal);
    }

    @Override
    public User loadUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    @Transactional
    public void processRegistration(RegistrationForm form) {
        Authority auth = new Authority();
        auth.setUser(userRepo.save(form.toUser(encoder)));
        auth.setAuthority("ROLE_USER");
        authorityRepo.save(auth);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PointHistoryDto> findUserPointHistory(Principal principal) {
        return userPointHistoryRepo
                .findAllByUser(userRepo.findByUsername(principal.getName()))
                .stream()
                .map(pointHistory
                        -> new PointHistoryDto.Builder()
                        .createTime(new SimpleDateFormat("yyyy-MM-dd").format(pointHistory.getCreateTime()))
                        .historyDetail(pointHistory.getPointHistoryDetail().getHistoryDetail())
                        .pointChange(pointHistory.getPointChange())
                        .changeResult(pointHistory.getChangeResult())
                        .using(pointHistory.getPointHistoryDetail().isUsing())
                        .build()
                ).collect(Collectors.toList());
    }

    private UserDto principalToDto(Principal principal) {
        User user = userRepo.findByUsername(principal.getName());
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .point(user.getPoint())
                .reviews(productReviewRepository.countProductReviewsByUser(user))
                .cart(countProductOrderProduct(user))
                .payedOrders(orderRepo.countProductOrdersByUserAndDeliveryState(user, DeliveryState.PREPARING.toString()))
                .shippingOrders(orderRepo.countProductOrdersByUserAndDeliveryState(user, DeliveryState.DELIVERING.toString()))
                .completedOrders(orderRepo.countProductOrdersByUserAndDeliveryState(user, DeliveryState.CONFIRMED.toString()))
                .build();
    }

    private Integer countProductOrderProduct(User user) {
        return orderRepo
                .findProductOrderByUserAndEnabled(user, false)
                .map(cartRepo::countProductOrderProductsByProductOrder)
                .orElse(0);
    }
}