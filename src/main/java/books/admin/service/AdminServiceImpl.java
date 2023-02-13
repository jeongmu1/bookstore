package books.admin.service;

import books.admin.common.FailToUploadException;
import books.admin.common.OrderInfoDto;
import books.admin.common.ProductBookForm;
import books.common.BookProps;
import books.common.DeliveryState;
import books.common.DeliveryStateConverter;
import books.order.domain.ProductOrder;
import books.order.domain.ProductOrderProduct;
import books.order.repository.OrderRepository;
import books.product.domain.*;
import books.product.repository.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.JoinType;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    private final CategoryRepository categoryRepo;
    private final PublisherRepository publisherRepo;
    private final ProductBookRepository productBookRepo;
    private final BookProps bookProps;
    private final ProductImageRepository productImageRepo;
    private final ProductCategoryRepository productCategoryRepo;
    private final OrderRepository orderRepo;

    public AdminServiceImpl(CategoryRepository categoryRepo, PublisherRepository publisherRepo, ProductBookRepository productBookRepo, BookProps bookProps, ProductImageRepository productImageRepo, ProductCategoryRepository productCategoryRepo, OrderRepository orderRepo) {
        this.categoryRepo = categoryRepo;
        this.publisherRepo = publisherRepo;
        this.productBookRepo = productBookRepo;
        this.bookProps = bookProps;
        this.productImageRepo = productImageRepo;
        this.productCategoryRepo = productCategoryRepo;
        this.orderRepo = orderRepo;
    }

    @Override
    @Transactional
    public void addProduct(ProductBookForm bookForm) throws FailToUploadException {
        ProductBook book = convertProductBookFormToProductBook(bookForm);
        book.setPublisher(publisherRepo.findPublisherById(bookForm.getPublisher()));
        book = productBookRepo.save(book);

        ProductBook finalBook = book;
        bookForm.getCategories().forEach(categoryId -> {
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductBook(finalBook);
            productCategory.setCategory(categoryRepo.findCategoryById(categoryId));
            productCategoryRepo.save(productCategory);
        });

        // Image 저장
        MultipartFile file = bookForm.getBookImage();
        if (file.isEmpty()) {
            throw new NullPointerException("There is no uploaded image");
        }
        ProductImage productImage = getNewProductImage(book, file);
        Path savePath = Paths.get(
                bookProps.getImages() + productImage.getFileName());
        try {
            file.transferTo(savePath);
        } catch (IOException e) {
            throw new FailToUploadException(e);
        }

        productImageRepo.save(productImage);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findAllCategories() {
        return categoryRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Publisher> findAllPublishers() {
        return publisherRepo.findAll();
    }

    private ProductBook convertProductBookFormToProductBook(ProductBookForm bookForm) {
        ProductBook book = new ProductBook();
        book.setTitle(bookForm.getTitle());
        book.setPublisher(publisherRepo.findPublisherById(bookForm.getPublisher()));
        book.setAuthor(bookForm.getAuthor());
        book.setPrice(bookForm.getPrice());
        if (bookForm.getStock() > 0) book.setEnabled(true);
        book.setStock(bookForm.getStock());
        book.setDescription(bookForm.getDescription());
        return book;
    }

    private ProductImage getNewProductImage(ProductBook book, MultipartFile imageFile) {
        ProductImage image = new ProductImage();
        image.setProductBook(book);
        image.setEnabled(false);
        image.setFileName(UUID.randomUUID() + imageFile.getOriginalFilename());

        return image;
    }

    @Override
    public List<String> findAllDeliveryStates() {
        return Arrays
                .stream(DeliveryState.values())
                .filter(deliveryState -> deliveryState != DeliveryState.BEFORE)
                .map(DeliveryStateConverter::deliveryStateToString)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderInfoDto> findOrderInfoByConditions(
            Set<String> deliveryStates
            , String searchCriteria
            , String keyword) {

        Specification<ProductOrder> spec = Specification.where(null);
        if (deliveryStates != null && !deliveryStates.isEmpty()) {
            spec = Objects.requireNonNull(spec).and(hasDeliveryState(deliveryStates));
        } else {
            spec = Objects.requireNonNull(spec).and(getEmptyDeliveryState());
        }

        if (searchCriteria != null && !searchCriteria.isEmpty() && keyword != null && !keyword.isEmpty()) {
            if (searchCriteria.equals("orderUuid"))
                spec = Objects.requireNonNull(spec).and(hasOrderUuid(keyword));
            else if (searchCriteria.equals("username")) {
                spec = Objects.requireNonNull(spec).and(hasUserUsername(keyword));
            }
        }

        return orderRepo.findAll(spec, Sort.by(Sort.Direction.DESC, "updateTime"))
                .stream()
                .map(this::convertProductOrderToDtos)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private List<OrderInfoDto> convertProductOrderToDtos(ProductOrder order) {
        return order.getProductOrderProducts()
                .stream()
                .map(pop -> convertProductOrderProductToDto(pop, order))
                .collect(Collectors.toList());
    }

    private OrderInfoDto convertProductOrderProductToDto(ProductOrderProduct pop, ProductOrder order) {
        return OrderInfoDto.builder()
                .updateTime(new SimpleDateFormat("yyyy-MM-dd").format(order.getUpdateTime()))
                .orderUuid(order.getOrderUuid())
                .productName(pop.getProductBook().getTitle())
                .quantity(pop.getProductCount())
                .userName(order.getUser().getName())
                .deliveryState(DeliveryStateConverter.deliveryStateToString(DeliveryState.valueOf(pop.getDeliveryState())))
                .productOrderProductId(pop.getId())
                .build();
    }

    private Specification<ProductOrder> hasDeliveryState(Set<String> deliveryStates) {
        return (root, criteriaQuery, criteriaBuilder)
                -> root
                .get("deliveryState")
                .in(deliveryStates
                        .stream()
                        .map(deliveryState -> DeliveryStateConverter.stringToDeliveryState(deliveryState).toString())
                        .collect(Collectors.toSet())
                );
    }

    private Specification<ProductOrder> getEmptyDeliveryState() {
        return hasDeliveryState(
                Arrays.stream(DeliveryState.values())
                        .filter(deliveryState -> deliveryState != DeliveryState.BEFORE)
                        .map(DeliveryStateConverter::deliveryStateToString)
                        .collect(Collectors.toSet())
        );
    }

    private Specification<ProductOrder> hasOrderUuid(String orderUuid) {
        return ((root, criteriaQuery, criteriaBuilder)
                -> criteriaBuilder.like(root.get("orderUuid"), "%" + orderUuid + "%"));
    }

    private Specification<ProductOrder> hasUserUsername(String username) {
        return (root, criteriaQuery, criteriaBuilder)
                -> criteriaBuilder.like(root.get("user").get("username"), "%" + username + "%");
    }


}
