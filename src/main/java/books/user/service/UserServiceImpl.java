package books.user.service;

import books.admin.common.OrderInfoDto;
import books.admin.common.UserInfoDto;
import books.admin.service.AdminService;
import books.common.DeliveryState;
import books.common.DeliveryStateConverter;
import books.common.EntityConverter;
import books.common.ReviewProps;
import books.order.domain.ProductOrder;
import books.order.domain.ProductOrderProduct;
import books.product.domain.ProductReview;
import books.product.repository.ProductReviewRepository;
import books.user.common.*;
import books.user.domain.*;
import books.order.repository.CartRepository;
import books.order.repository.OrderRepository;
import books.user.repository.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static books.common.DeliveryState.BEFORE;


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
    private final ReviewProps reviewProps;

    public UserServiceImpl(UserRepository userRepo, UserAuthorityRepository authorityRepo, CartRepository cartRepo, OrderRepository orderRepo, PasswordEncoder encoder, UserPointHistoryRepository userPointHistoryRepo, ProductReviewRepository productReviewRepository, AdminService adminService, PasswordEncoder passwordEncoder, ReviewProps reviewProps) {
        this.userRepo = userRepo;
        this.authorityRepo = authorityRepo;
        this.cartRepo = cartRepo;
        this.orderRepo = orderRepo;
        this.encoder = encoder;
        this.userPointHistoryRepo = userPointHistoryRepo;
        this.productReviewRepository = productReviewRepository;
        this.adminService = adminService;
        this.passwordEncoder = passwordEncoder;
        this.reviewProps = reviewProps;
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

    // ???????????? ??????
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
                .deliveryCompletedOrders(orderRepo.countProductOrdersByUserAndDeliveryState(user, DeliveryState.DELIVERY_COMPLETED.toString()))
                .completedOrders(orderRepo.countProductOrdersByUserAndDeliveryState(user, DeliveryState.CONFIRMED.toString()))
                .pointStamp(user.getPointStamp())
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

    @Override
    public List<ProductReview> findProductReviewsByUsername(String username) {
        return productReviewRepository.findProductReviewsByUser(userRepo.findByUsername(username));
    }

    @Override
    public ProductReview findProductReviewForUpdateById(String username, Long id) throws IllegalAccessException {
        ProductReview review = productReviewRepository.findById(id).orElseThrow();
        qualifyAccessToProductReview(username, review);
        return productReviewRepository.findById(id).orElseThrow();
    }


    @Override
    @Transactional
    public void updateProductReview(String username, ProductReviewForm form) throws IllegalAccessException {
        ProductReview review = productReviewRepository.findById(form.getId()).orElseThrow();
        qualifyAccessToProductReview(username, review);

        if (!review.getComment().equals(form.getComment())) {
            review.setComment(form.getComment());
        }

        if (!review.getProductScore().equals(form.getProductScore())) {
            qualifyProductReviewScore(review.getProductScore());
            review.setProductScore(form.getProductScore());
        }
    }

    private void qualifyAccessToProductReview(String username, ProductReview productReview) throws IllegalAccessException {
        if (!productReview.getUser().getId().equals(userRepo.findByUsername(username).getId())) {
            throw new IllegalAccessException("Access denied");
        }
    }

    @Override
    public void deleteProductReviewById(String username, Long id) throws IllegalAccessException {
        ProductReview review = productReviewRepository.findById(id).orElseThrow();
        qualifyAccessToProductReview(username, review);
        productReviewRepository.delete(review);
    }

    private void qualifyProductReviewScore(Byte score) {
        if (score < reviewProps.getMinScore() || score > reviewProps.getMaxScore()) {
            throw new IllegalArgumentException("Out range of score");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderInfoDto> findOrderInfos(String username, Set<String> deliveryStates, String searchCriteria, String keyword) {
        Specification<ProductOrderProduct> spec = Specification.where((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("productOrder").get("user").get("username"), username));
        if (!(deliveryStates == null || deliveryStates.isEmpty())) {
            // ??????????????? ?????? ?????? ????????? ????????????
            deliveryStates.forEach(state -> {
                if (state.equals(DeliveryStateConverter.deliveryStateToString(BEFORE))) {
                    throw new IllegalArgumentException();
                }
            });

            spec = Objects.requireNonNull(spec).and((root, criteriaQuery, criteriaBuilder)
                    -> root
                    .get("deliveryState")
                    .in(deliveryStates
                            .stream()
                            .map(deliveryState -> Objects.requireNonNull(DeliveryStateConverter.stringToDeliveryState(deliveryState)).toString())
                            .collect(Collectors.toSet())
                    ));
        }

        if (!(searchCriteria == null || searchCriteria.isBlank()) && !(keyword == null || keyword.isBlank())) {
            switch (searchCriteria) {
                case "orderUuid":
                    spec = Objects.requireNonNull(spec).and((root, criteriaQuery, criteriaBuilder) ->
                            criteriaBuilder.like(root.get("productOrder").get("orderUuid"), "%" + keyword + "%"));
                    break;
                case "productName":
                    spec = Objects.requireNonNull(spec).and((root, criteriaQuery, criteriaBuilder) ->
                            criteriaBuilder.like(root.get("productBook").get("title"), "%" + keyword + "%"));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }

        return cartRepo.findAll(spec)
                .stream()
                .filter(pop -> !pop.getDeliveryState().equals(BEFORE.toString()))
                .map(EntityConverter::convertProductOrderProduct)
                .collect(Collectors.toList());

    }

    @Override
    public List<String> findAllDeliveryStates() {
        return Arrays
                .stream(DeliveryState.values())
                .filter(deliveryState -> deliveryState != BEFORE)
                .map(DeliveryStateConverter::deliveryStateToString)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateDeliveryStateToConfirmed(String username, Long popId) throws IllegalAccessException {
        ProductOrderProduct pop = cartRepo.findById(popId).orElseThrow();
        ProductOrder order = pop.getProductOrder();

        if (!order.getUser().getUsername().equals(username)) {
            throw new IllegalAccessException();
        }
        if (!pop.getDeliveryState().equals(DeliveryState.DELIVERY_COMPLETED.toString())) {
            throw new IllegalArgumentException("?????? ?????? ????????? ????????????.");
        }

        pop.setDeliveryState(DeliveryState.CONFIRMED.toString());

        // ????????? ??? ???????????? ??????????????? ?????????????????? ?????? ????????? ??? ?????? ?????? ??????
        if (order.getProductOrderProducts().size()
                == order.getProductOrderProducts()
                .stream()
                .filter(productOrderProduct -> productOrderProduct.getDeliveryState().equals(DeliveryState.CONFIRMED.toString()))
                .count()) {
            order.setDeliveryState(DeliveryState.CONFIRMED.toString());
        }
    }
}