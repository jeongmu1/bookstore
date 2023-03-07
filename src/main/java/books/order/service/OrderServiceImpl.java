package books.order.service;

import books.common.DeliveryState;
import books.common.PointProps;
import books.order.common.NoItemException;
import books.order.common.OrderForm;
import books.order.domain.ProductOrder;
import books.order.domain.ProductOrderProduct;
import books.order.repository.CartRepository;
import books.order.repository.OrderRepository;
import books.product.common.CartItemDto;
import books.product.domain.ProductBook;
import books.product.repository.ProductBookRepository;
import books.user.domain.User;
import books.user.domain.UserAddress;
import books.user.domain.UserPointHistory;
import books.user.repository.PointHistoryDetailRepository;
import books.user.repository.UserAddressRepository;
import books.user.repository.UserPointHistoryRepository;
import books.user.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final UserAddressRepository userAddressRepo;
    private final UserRepository userRepo;
    private final PointProps pointProps;
    private final ProductBookRepository productBookRepo;
    private final OrderRepository orderRepo;
    private final CartRepository cartRepo;
    private final PointHistoryDetailRepository pointHistoryDetailRepo;
    private final UserPointHistoryRepository userPointHistoryRepo;

    public OrderServiceImpl(UserAddressRepository userAddressRepo, UserRepository userRepo, PointProps pointProps, ProductBookRepository productBookRepo, OrderRepository orderRepo, CartRepository cartRepo, PointHistoryDetailRepository pointHistoryDetailRepo, UserPointHistoryRepository userPointHistoryRepo) {
        this.userAddressRepo = userAddressRepo;
        this.userRepo = userRepo;
        this.pointProps = pointProps;
        this.productBookRepo = productBookRepo;
        this.orderRepo = orderRepo;
        this.cartRepo = cartRepo;
        this.pointHistoryDetailRepo = pointHistoryDetailRepo;
        this.userPointHistoryRepo = userPointHistoryRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public UserAddress findDefaultUserAddress(String username) {
        return userAddressRepo
                .findUserAddressByUserAndDefaultFlag(
                        userRepo.findByUsername(username), true
                ).orElse(getEmptyUserAddressEntity());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartItemDto> findProduct(Long productBookId, int quantity) {
        return Collections
                .singletonList(
                        productBookEntityToDto(productBookRepo.findProductBookById(productBookId), quantity
                        ));
    }

    private UserAddress getEmptyUserAddressEntity() {
        UserAddress userAddress = new UserAddress();
        userAddress.setAddress("");
        userAddress.setDeliveryName("");
        userAddress.setZipCode("");
        userAddress.setPhone("");
        return userAddress;
    }

    private CartItemDto productBookEntityToDto(ProductBook book, int quantity) {
        return CartItemDto.builder()
                .itemId(book.getId())
                .title(book.getTitle())
                .price(book.getPrice())
                .fileName(book.getProductImages().iterator().next().getFileName())
                .amount(quantity)
                .point(book.getPrice() / pointProps.getSavingRate())
                .build();
    }

    @Override
    @Transactional
    public void addOrderByCartItems(OrderForm orderForm, String username)
            throws NoItemException {
        ProductOrder order = getProductOrder(username);

        if (cartRepo.findAllByProductOrder(order).isEmpty()) throw new NoItemException("No items in cart");
        order.getProductOrderProducts().forEach(pop -> pop.setDeliveryState(DeliveryState.PREPARING.toString()));

        order = setProductOrder(orderForm, getProductOrder(username));
        orderRepo.save(order);

        addPointHistory(username, order);
    }

    @Override
    @Transactional
    public void addOrderByProduct(OrderForm orderForm, String username, Long productBookId, int quantity) {
        ProductOrderProduct pop = new ProductOrderProduct();
        ProductBook book = productBookRepo.findProductBookById(productBookId);
        pop.setProductCount(quantity);
        pop.setProductBook(book);
        pop.setDeliveryState(DeliveryState.PREPARING.toString());

        ProductOrder order = setProductOrder(orderForm, new ProductOrder());
        order.setUser(userRepo.findByUsername(username));
        orderRepo.save(order);
        order.setEnabled(true);
        pop.setProductOrder(order);
        order.setProductOrderProducts(Collections.singletonList(pop));
        cartRepo.save(pop);

        addPointHistory(username, order);
    }

    private ProductOrder getProductOrder(String username) {
        return orderRepo.findProductOrderByUserAndEnabled(userRepo.findByUsername(username), false)
                .orElseThrow();
    }

    @NotNull
    private ProductOrder setProductOrder(OrderForm form, ProductOrder order) {
        order.setCcNumber(form.getCcNumber());
        order.setCcExpiration(form.getCcExpiration());
        order.setCcCvv(form.getCcCvv());
        order.setDeliveryName(form.getDeliveryName());
        order.setDeliveryZipCode(form.getDeliveryZipCode());
        order.setDeliveryAddress(form.getDeliveryAddress());
        order.setDeliveryPhone(form.getPhone());
        order.setEnabled(true);
        order.setDeliveryState(DeliveryState.PREPARING.toString());

        return order;
    }

    private void addPointHistory(String username, ProductOrder order) {
        User user = userRepo.findByUsername(username);
        int pointChange = order
                .getProductOrderProducts()
                .stream()
                .mapToInt(pop -> pop
                        .getProductBook()
                        .getPrice() / pointProps.getSavingRate())
                .sum();
        int totalPoint = user.getPoint() + pointChange;

        UserPointHistory userPointHistory = new UserPointHistory();
        userPointHistory.setUser(user);
        userPointHistory.setPointHistoryDetail(pointHistoryDetailRepo.findPointHistoryDetailById(2));
        userPointHistory.setPointChange(pointChange);
        userPointHistory.setChangeResult(totalPoint);

        userPointHistoryRepo.save(userPointHistory);
        user.setPoint(totalPoint);
    }
}
