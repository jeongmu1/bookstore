package books.common;

import books.admin.common.OrderInfoDto;
import books.admin.common.UserInfoDto;
import books.home.common.ProductBookDto;
import books.order.domain.ProductOrder;
import books.order.domain.ProductOrderProduct;
import books.product.domain.ProductBook;
import books.product.domain.ProductReview;
import books.user.domain.Authority;
import books.user.domain.User;

import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.stream.Collectors;

import static books.auth.common.UserRole.ROLE_ADMIN;
import static books.auth.common.UserRole.ROLE_USER;

public class EntityConverter {

    private EntityConverter() {
        throw new IllegalStateException("Utility class");
    }

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public static ProductBookDto convertProductBook(ProductBook book) {
        return ProductBookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher().getName())
                .price(book.getPrice())
                .rate(book.getProductReviews()
                        .stream()
                        .mapToDouble(ProductReview::getProductScore)
                        .average()
                        .orElse(0))
                .description(book.getDescription())
                .sizeOfReviews(book.getProductReviews().size())
                .imageFileName(book.getProductImages()
                        .stream()
                        .findFirst()
                        .orElseThrow()
                        .getFileName())
                .enabled(book.isEnabled())
                .outOfStock(book.getStock() < 1)
                .reviews(book.getProductReviews())
                .build();
    }

    public static OrderInfoDto convertProductOrderProduct(ProductOrderProduct pop) {
        ProductOrder order = pop.getProductOrder();
        String username = "null";
        if (order.getUser() != null) {
            username = order.getUser().getUsername();
        }
        return OrderInfoDto.builder()
                .updateTime(new SimpleDateFormat(DATE_FORMAT).format(order.getUpdateTime()))
                .orderUuid(order.getOrderUuid())
                .productName(pop.getProductBook().getTitle())
                .productId(pop.getProductBook().getId())
                .quantity(pop.getProductCount())
                .username(username)
                .deliveryState(DeliveryStateConverter.deliveryStateToString(DeliveryState.valueOf(pop.getDeliveryState())))
                .id(pop.getId())
                .deliveryName(order.getDeliveryName())
                .waitingConfirmationPurchase(pop.getDeliveryState().equals(DeliveryState.DELIVERY_COMPLETED.toString()))
                .build();
    }

    public static UserInfoDto convertUser(User user) {
        return UserInfoDto.builder()
                .id(user.getId())
                .name(user.getName())
                .createTime(new SimpleDateFormat(DATE_FORMAT).format(user.getCreateTime()))
                .phone(user.getPhone())
                .username(user.getUsername())
                .enabled(user.isEnabled())
                .authority(getAuthorityByUser(user))
                .build();
    }

    private static String getAuthorityByUser(User user) {
        Set<String> authorities = user.getAuthorities()
                .stream()
                .map(Authority::getAuthority)
                .filter(authority -> authority.equals(ROLE_ADMIN.toString()))
                .collect(Collectors.toSet());

        if (!authorities.isEmpty()) {
            return ROLE_ADMIN.toString();
        }
        return ROLE_USER.toString();
    }
}
