package books.order.service;

import books.common.PointProps;
import books.order.common.NoItemException;
import books.order.common.OrderForm;
import books.order.common.OverStockException;
import books.order.domain.ProductOrder;
import books.order.domain.ProductOrderProduct;
import books.order.repository.CartRepository;
import books.order.repository.DeliveryStateRepository;
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

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class OrderServiceImpl implements OrderService {
    private final UserAddressRepository userAddressRepo;
    private final UserRepository userRepo;
    private final PointProps pointProps;
    private final ProductBookRepository productBookRepo;
    private final DeliveryStateRepository deliveryStateRepo;
    private final OrderRepository orderRepo;
    private final CartRepository cartRepo;
    private final PointHistoryDetailRepository pointHistoryDetailRepo;

    public OrderServiceImpl(UserAddressRepository userAddressRepo, UserRepository userRepo, PointProps pointProps, ProductBookRepository productBookRepo, DeliveryStateRepository deliveryStateRepo, OrderRepository orderRepo, CartRepository cartRepo, PointHistoryDetailRepository pointHistoryDetailRepo, UserPointHistoryRepository userPointHistoryRepo) {
        this.userAddressRepo = userAddressRepo;
        this.userRepo = userRepo;
        this.pointProps = pointProps;
        this.productBookRepo = productBookRepo;
        this.deliveryStateRepo = deliveryStateRepo;
        this.orderRepo = orderRepo;
        this.cartRepo = cartRepo;
        this.pointHistoryDetailRepo = pointHistoryDetailRepo;
        this.userPointHistoryRepo = userPointHistoryRepo;
    }

    private final UserPointHistoryRepository userPointHistoryRepo;

    @Override
    @Transactional(readOnly = true)
    public UserAddress findDefaultUserAddress(Principal principal) {
        return userAddressRepo
                .findUserAddressByUserAndDefaultFlag(
                        userRepo.findByUsername(principal.getName()), true
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

    @Override
    @Transactional
    public void addOrderByCartItems(OrderForm orderForm, Principal principal) throws OverStockException, NoItemException {
        ProductOrder order = getProductOrder(principal);

        if (cartRepo.findAllByProductOrder(order).isEmpty()) throw new NoItemException("No items in cart");
        for (ProductOrderProduct product : order.getProductOrderProducts()) accept(product);

        order = convertFormToProductOrder(orderForm, getProductOrder(principal));
        orderRepo.save(order);

        addPointHistory(principal, order);
    }

    @Override
    @Transactional
    public void addOrderByProduct(OrderForm orderForm, Principal principal, Long productBookId, int quantity) throws OverStockException {
        ProductOrderProduct item = new ProductOrderProduct();
        ProductBook book = productBookRepo.findProductBookById(productBookId);
        book.setStock(book.getStock() - quantity);
        productBookRepo.save(book);

        if (quantity > book.getStock()) throw new OverStockException("Out of Stock!");
        ProductOrder order = convertFormToProductOrder(orderForm);
        order.setUser(userRepo.findByUsername(principal.getName()));
        orderRepo.save(order);

        item.setProductBook(book);
        item.setProductCount(quantity);
        item.setProductOrder(order);
        order.setProductOrderProducts(Collections.singletonList(item));

        addPointHistory(principal, order);
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

    private ProductOrder getProductOrder(Principal principal) {
        return orderRepo.findProductOrderByUserAndEnabled(userRepo.findByUsername(principal.getName()), false)
                .orElseThrow();
    }

    private ProductOrder convertFormToProductOrder(OrderForm form) {
        ProductOrder order = new ProductOrder();
        return setProductOrder(form, order);
    }

    private ProductOrder convertFormToProductOrder(OrderForm form, ProductOrder order) {
        return setProductOrder(form, order);
    }

    @NotNull
    private ProductOrder setProductOrder(OrderForm form, ProductOrder order) {
        order.setDeliveryState(deliveryStateRepo.findDeliveryStateById(2));
        order.setCcNumber(form.getCcNumber());
        order.setCcExpiration(form.getCcExpiration());
        order.setCcCvv(form.getCcCvv());
        order.setDeliveryName(form.getDeliveryName());
        order.setDeliveryZipCode(form.getDeliveryZipCode());
        order.setDeliveryAddress(form.getDeliveryAddress());
        order.setDeliveryPhone(form.getPhone());
        order.setEnabled(true);

        return order;
    }

    private void addPointHistory(Principal principal, ProductOrder order) {
        User user = userRepo.findByUsername(principal.getName());
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

    private void accept(ProductOrderProduct productOrderProduct) throws OverStockException {
        ProductBook book = productOrderProduct.getProductBook();
        if (productOrderProduct.getProductCount() > book.getStock()) throw new OverStockException("Out of Stock!");
        book.setStock(book.getStock() - productOrderProduct.getProductCount());
        productBookRepo.save(book);
    }
}
