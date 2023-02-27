package books.user.service;

import books.admin.common.UserInfoDto;
import books.admin.service.AdminService;
import books.common.DeliveryState;
import books.common.EntityConverter;
import books.product.repository.ProductReviewRepository;
import books.user.common.PointHistoryDto;
import books.user.common.UserDto;
import books.user.common.UserUpdateForm;
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
    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepo, UserAuthorityRepository authorityRepo, CartRepository cartRepo, OrderRepository orderRepo, PasswordEncoder encoder, UserPointHistoryRepository userPointHistoryRepo, ProductReviewRepository productReviewRepository, AdminService adminService, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.authorityRepo = authorityRepo;
        this.cartRepo = cartRepo;
        this.orderRepo = orderRepo;
        this.encoder = encoder;
        this.userPointHistoryRepo = userPointHistoryRepo;
        this.productReviewRepository = productReviewRepository;
        this.adminService = adminService;
        this.passwordEncoder = passwordEncoder;
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

    @Override
    @Transactional(readOnly = true)
    public UserInfoDto findUserInfoDto(String username) {
        return EntityConverter.convertUser(userRepo.findByUsername(username));
    }

    @Override
    public UserUpdateForm initializeUserUpdateFormByUsername(String username) {
        User user = userRepo.findByUsername(username);
        UserUpdateForm form = new UserUpdateForm();
        form.setName(user.getName());
        form.setPhone(user.getPhone());
        return form;
    }

    @Override
    @Transactional
    public void updateUserByForm(String username, UserUpdateForm updateForm) {
        User user = userRepo.findByUsername(username);

        if (!updateForm.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(updateForm.getPassword()));
        }

        if (!updateForm.getName().equals(user.getName())) {
            user.setName(updateForm.getName());
        }

        if (!updateForm.getPhone().equals(user.getPhone())) {
            user.setPhone(updateForm.getPhone());
        }

    }

    @Override
    public void deleteUserByUsername(String username) {
        adminService.deleteUserById(userRepo.findByUsername(username).getId());
    }
}