package books.product.service.impl;

import books.common.PointProps;
import books.order.domain.ProductOrder;
import books.order.domain.ProductOrderProduct;
import books.order.repository.CartRepository;
import books.order.repository.DeliveryStateRepository;
import books.order.repository.OrderRepository;
import books.product.common.CartItemDto;
import books.product.domain.ProductBook;
import books.product.repository.ProductBookRepository;
import books.product.service.CartService;
import books.user.domain.User;
import books.user.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    private final UserRepository userRepo;
    private final OrderRepository orderRepo;
    private final CartRepository cartRepo;
    private final DeliveryStateRepository deliveryStateRepo;
    private final ProductBookRepository productBookRepo;
    private final PointProps pointProps;

    public CartServiceImpl(UserRepository userRepo, OrderRepository orderRepo, CartRepository cartRepo, DeliveryStateRepository deliveryStateRepo, ProductBookRepository productBookRepo, PointProps pointProps) {
        this.userRepo = userRepo;
        this.orderRepo = orderRepo;
        this.cartRepo = cartRepo;
        this.deliveryStateRepo = deliveryStateRepo;
        this.productBookRepo = productBookRepo;
        this.pointProps = pointProps;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartItemDto> findCartByUser(@NotNull Principal principal) {
        return cartRepo
                .findAllByProductOrder(getOrderEntity(userRepo
                        .findByUsername(principal.getName())))
                .stream()
                .map(this::itemEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void addProductInCart(Principal principal, int amount, long itemId) {
        ProductOrderProduct product = findItem(principal, itemId);
        if (product != null) {
            product.setProductCount(product.getProductCount() + amount);
            cartRepo.save(product);
            return;
        }
        cartRepo.save(
                getCartItemEntity(
                        userRepo.findByUsername(principal.getName())
                        , productBookRepo.findProductBookById(itemId)
                        , amount
                ));

    }

    @Override
    public Map<String, Object> getTotalPrice(@NotNull List<CartItemDto> cart) {
        AtomicInteger totalPrice = new AtomicInteger();
        AtomicInteger totalPoint = new AtomicInteger();
        cart.forEach(item -> {
            int amount = item.getAmount();
            totalPrice.addAndGet(item.getPrice() * amount);
            totalPoint.addAndGet(item.getPoint() * amount);
        });

        Map<String, Object> totalMap = new HashMap<>();
        totalMap.put("totalPrice", totalPrice);
        totalMap.put("totalPoint", totalPoint);

        return totalMap;
    }

    @Override
    public void modifyProductCount(@NotNull Principal principal, @NotNull String amounts) {
        List<Integer> intAmounts = Arrays.stream(amounts.split(",")).map(Integer::valueOf).collect(Collectors.toList());

        List<CartItemDto> cart = cartRepo
                .findAllByProductOrder(getOrderEntity(userRepo
                        .findByUsername(principal.getName())))
                .stream()
                .map(this::itemEntityToDtoForChangeCount)
                .collect(Collectors.toList());
        for (int i = 0; i < cart.size(); i++) {
            CartItemDto itemDto = cart.get(i);
            ProductOrderProduct product = findItem(principal, itemDto.getItemId());
            Objects.requireNonNull(product).setProductCount(intAmounts.get(i));
            cartRepo.save(product);
        }
    }

    @Override
    public void deleteProduct(Principal principal, long itemId) {
        ProductOrderProduct item = findItem(principal, itemId);
        if (item != null) {
            cartRepo.deleteById(item.getId());
            return;
        }

        throw new NullPointerException();
    }

    @Override
    @Transactional
    public void deleteAll(@NotNull Principal principal) {
        cartRepo.deleteAllByProductOrder(
                getOrderEntity(userRepo.findByUsername(principal.getName()))
        );
    }

    private @Nullable ProductOrderProduct findItem(@NotNull Principal principal, long itemId) {
        List<ProductOrderProduct> cart = cartRepo
                .findAllByProductOrder(getOrderEntity(userRepo
                        .findByUsername(principal.getName())));

        for (ProductOrderProduct product : cart) {
            if (product.getProductBook().getId() == itemId) {
                return product;
            }
        }

        return null;
    }

    private @NotNull ProductOrderProduct getCartItemEntity(User user, ProductBook book, int amount) {
        ProductOrderProduct cartItem = new ProductOrderProduct();
        cartItem.setProductOrder(getOrderEntity(user));
        cartItem.setProductBook(book);
        cartItem.setProductCount(amount);
        return cartItem;
    }

    private ProductOrder getOrderEntity(User user) {
        Optional<ProductOrder> optionalProductOrder = orderRepo.findProductOrderByUserAndEnabled(user, false);
        return optionalProductOrder.orElseGet(() -> getOrderNewEntityForCart(user));
    }

    private @NotNull ProductOrder getOrderNewEntityForCart(User user) {
        ProductOrder order = new ProductOrder();
        order.setUser(user);
        order.setOrderUuid(UUID.randomUUID().toString());
        order.setDeliveryState(deliveryStateRepo.findDeliveryStateById(1));
        orderRepo.save(order);
        return order;
    }

    private CartItemDto itemEntityToDto(@NotNull ProductOrderProduct cart) {
        ProductBook book = cart.getProductBook();
        return CartItemDto.builder()
                .id(cart.getId())
                .itemId(book.getId())
                .title(book.getTitle())
                .price(book.getPrice())
                .fileName(book.getProductImages().iterator().next().getFileName())
                .amount(cart.getProductCount())
                .createTime(cart.getCreateTime())
                .point(book.getPrice() / pointProps.getSavingRate())
                .build();

    }

    private CartItemDto itemEntityToDtoForChangeCount(@NotNull ProductOrderProduct cart) {
        ProductBook book = cart.getProductBook();
        return CartItemDto.builder()
                .id(cart.getId())
                .amount(cart.getProductCount())
                .itemId(book.getId())
                .build();
    }
}
