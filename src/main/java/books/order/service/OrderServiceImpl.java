package books.order.service;

import books.common.PointProps;
import books.order.domain.ProductOrderProduct;
import books.product.common.CartItemDto;
import books.product.domain.ProductBook;
import books.product.repository.ProductBookRepository;
import books.user.domain.User;
import books.user.domain.UserAddress;
import books.user.repository.UserAddressRepository;
import books.user.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private final UserAddressRepository userAddressRepo;
    private final UserRepository userRepo;
    private final PointProps pointProps;
    private final ProductBookRepository productBookRepo;

    public OrderServiceImpl(UserAddressRepository userAddressRepo
            , UserRepository userRepo
            , PointProps pointProps
            , ProductBookRepository productBookRepo) {
        this.userAddressRepo = userAddressRepo;
        this.userRepo = userRepo;
        this.pointProps = pointProps;
        this.productBookRepo = productBookRepo;
    }

    @Override
    public UserAddress findDefaultUserAddress(Principal principal) {
        Optional<UserAddress> userAddress = userAddressRepo.findUserAddressByUserAndDefaultFlag(userRepo.findByUsername(principal.getName()), true);
        return userAddress.orElse(getEmptyUserAddressEntity());
    }

    @Override
    public List<CartItemDto> findProduct(Long productBookId, int quantity) {
        return Collections.singletonList(productBookEntityToDto(productBookRepo.findProductBookById(productBookId), quantity));
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
}
