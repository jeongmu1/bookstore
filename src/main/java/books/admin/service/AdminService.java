package books.admin.service;

import books.admin.common.OrderInfoDto;
import books.admin.common.ProductBookForm;
import books.common.DeliveryState;
import books.product.domain.Category;
import books.product.domain.Publisher;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AdminService {

    void addProduct(ProductBookForm book);

    List<Category> findAllCategories();

    List<Publisher> findAllPublishers();

    List<String> findAllDeliveryStates();

    List<OrderInfoDto> findOrderInfoByConditions(Set<String> deliveryStates, String searchCriteria, String keyword);

    void updateDeliveryState(Set<Long> productOrderProductIds, String deliveryState);

//    void updateDeliveryState(Set<OrderInfoDto> )
}
