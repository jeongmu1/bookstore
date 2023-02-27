package books.admin.service;

import books.admin.common.*;
import books.common.BookProps;
import books.common.DeliveryState;
import books.common.DeliveryStateConverter;
import books.admin.common.ProductBookDto;
import books.common.EntityConverter;
import books.order.domain.ProductOrder;
import books.order.domain.ProductOrderProduct;
import books.order.repository.CartRepository;
import books.order.repository.OrderRepository;
import books.product.domain.*;
import books.product.repository.*;
import books.user.domain.Authority;
import books.user.domain.User;
import books.user.repository.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static books.auth.common.UserRole.*;
import static books.common.DeliveryState.*;

@Service
public class AdminServiceImpl implements AdminService {
    private final CategoryRepository categoryRepo;
    private final PublisherRepository publisherRepo;
    private final ProductBookRepository productBookRepo;
    private final BookProps bookProps;
    private final ProductImageRepository productImageRepo;
    private final ProductCategoryRepository productCategoryRepo;
    private final UserRepository userRepo;
    private final CartRepository cartRepo;
    private final UserAuthorityRepository userAuthorityRepo;
    private final UserPointHistoryRepository userPointHistoryRepo;
    private final UserAddressRepository userAddressRepo;
    private final OrderRepository orderRepo;
    private final UserCCRepository userCCRepo;
    private final ProductReviewRepository productReviewRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserAuthorityRepository authorityRepo;
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public AdminServiceImpl(CategoryRepository categoryRepo, PublisherRepository publisherRepo, ProductBookRepository productBookRepo, BookProps bookProps, ProductImageRepository productImageRepo, ProductCategoryRepository productCategoryRepo, UserRepository userRepo, CartRepository cartRepo, UserAuthorityRepository userAuthorityRepo, UserPointHistoryRepository userPointHistoryRepo, UserAddressRepository userAddressRepo, OrderRepository orderRepo, UserCCRepository userCCRepo, ProductReviewRepository productReviewRepo, PasswordEncoder passwordEncoder, UserAuthorityRepository authorityRepo) {
        this.categoryRepo = categoryRepo;
        this.publisherRepo = publisherRepo;
        this.productBookRepo = productBookRepo;
        this.bookProps = bookProps;
        this.productImageRepo = productImageRepo;
        this.productCategoryRepo = productCategoryRepo;
        this.userRepo = userRepo;
        this.cartRepo = cartRepo;
        this.userAuthorityRepo = userAuthorityRepo;
        this.userPointHistoryRepo = userPointHistoryRepo;
        this.userAddressRepo = userAddressRepo;
        this.orderRepo = orderRepo;
        this.userCCRepo = userCCRepo;
        this.productReviewRepo = productReviewRepo;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepo = authorityRepo;
    }

    @Override
    @Transactional
    public void addProduct(ProductBookForm bookForm) {
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
        saveMultipartFile(bookForm, book);
    }

    private void saveMultipartFile(ProductBookForm bookForm, ProductBook book) {
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
                .filter(deliveryState -> deliveryState != BEFORE)
                .map(DeliveryStateConverter::deliveryStateToString)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderInfoDto> findOrderInfoByConditions(Set<String> deliveryStates, String searchCriteria, String keyword) {
        Specification<ProductOrderProduct> spec = Specification.where(null);
        if (!(deliveryStates == null || deliveryStates.isEmpty())) {
            spec = Objects.requireNonNull(spec).and(hasDeliveryStates(deliveryStates));
        } else {
            spec = Objects.requireNonNull(spec).and(getEmptyDeliveryState());
        }

        if (!(searchCriteria == null || searchCriteria.isEmpty() || keyword == null || keyword.isEmpty())) {
            switch (searchCriteria) {
                case "orderUuid":
                    spec = Objects.requireNonNull(spec).and(hasOrderUuid(keyword));
                    break;
                case "username":
                    spec = Objects.requireNonNull(spec).and(hasUserUsername(keyword));
                    break;
                case "productName":
                    spec = Objects.requireNonNull(spec).and(hasProductName(keyword));
                    break;
                case "productId":
                    spec = Objects.requireNonNull(spec).and(hasProductId(keyword));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }

        return cartRepo.findAll(spec, Sort.by(Sort.Direction.DESC, "productOrder.updateTime"))
                .stream()
                .map(EntityConverter::convertProductOrderProduct)
                .collect(Collectors.toList());
    }

    private Specification<ProductOrderProduct> hasDeliveryStates(Set<String> deliveryStates) {
        return (root, criteriaQuery, criteriaBuilder)
                -> root
                .get("deliveryState")
                .in(deliveryStates
                        .stream()
                        .map(deliveryState -> Objects.requireNonNull(DeliveryStateConverter.stringToDeliveryState(deliveryState)).toString())
                        .collect(Collectors.toSet())
                );
    }

    private Specification<ProductOrderProduct> getEmptyDeliveryState() {
        return hasDeliveryStates(
                Arrays.stream(DeliveryState.values())
                        .filter(deliveryState -> deliveryState != BEFORE)
                        .map(DeliveryStateConverter::deliveryStateToString)
                        .collect(Collectors.toSet())
        );
    }

    private Specification<ProductOrderProduct> hasOrderUuid(String orderUuid) {
        return (root, criteriaQuery, criteriaBuilder)
                -> criteriaBuilder.like(root.get("productOrder").get("orderUuid"), setLikeKeyword(orderUuid));
    }

    private Specification<ProductOrderProduct> hasUserUsername(String username) {
        return ((root, criteriaQuery, criteriaBuilder)
                -> criteriaBuilder.like(root.get("productOrder").get("user").get("username"), setLikeKeyword(username)));
    }

    private Specification<ProductOrderProduct> hasProductName(String productName) {
        return ((root, criteriaQuery, criteriaBuilder)
                -> criteriaBuilder.like(root.get("productBook").get("title"), setLikeKeyword(productName)));
    }

    private Specification<ProductOrderProduct> hasProductId(String productId) {
        return ((root, criteriaQuery, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("productBook").get("id"), productId));
    }

    @Override
    @Transactional
    public void updateDeliveryState(Set<Long> productOrderProductIds, String deliveryState) {
        productOrderProductIds.forEach(popId -> {
            ProductOrderProduct pop = cartRepo.findById(popId).orElseThrow();
            DeliveryState enumDeliveryState = DeliveryStateConverter.stringToDeliveryState(deliveryState);
            String ds = enumDeliveryState.toString();

            if (pop.getDeliveryState().equals(PREPARING.toString())
                    && !pop.getDeliveryState().equals(ds) && !ds.equals(DELIVERING.toString())) {
                pop.getProductBook().setStock(pop.getProductBook().getStock() - pop.getProductCount());
            }
            pop.setDeliveryState(ds);

            ProductOrder order = pop.getProductOrder();

            if ((int) pop.getProductOrder().getProductOrderProducts()
                    .stream()
                    .filter(item -> item.getDeliveryState().equals(ds))
                    .count() == order.getProductOrderProducts().size()) {
                order.setDeliveryState(ds);
            }
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserInfoDto> findUserInfoByConditions(
            String authority,
            String enabled,
            String searchCriteria,
            String keyword) {

        Specification<User> spec = Specification.where(null);
        if (!(authority == null || authority.isEmpty())) {
            spec = Objects.requireNonNull(spec).and((root, criteriaQuery, criteriaBuilder)
                    -> criteriaBuilder.equal(root.join("authorities").get("authority"), authority));
        }

        if (enabled != null && !enabled.equals("entire")) {
            spec = Objects.requireNonNull(spec)
                    .and((root, criteriaQuery, criteriaBuilder)
                            -> criteriaBuilder.equal(root.get("enabled"), Boolean.valueOf(enabled)));
        }

        if (!(searchCriteria == null || searchCriteria.isEmpty() || keyword == null || keyword.isEmpty())) {
            switch (searchCriteria) {
                case "id":
                    spec = Objects.requireNonNull(spec)
                            .and(((root, criteriaQuery, criteriaBuilder)
                                    -> criteriaBuilder.equal(root.get(searchCriteria), keyword)));
                    break;
                case "username":
                case "name":
                case "phone":
                    spec = Objects.requireNonNull(spec)
                            .and((root, criteriaQuery, criteriaBuilder)
                                    -> criteriaBuilder.equal(root.get(searchCriteria), setLikeKeyword(keyword)));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }

        return userRepo.findAll(spec)
                .stream()
                .map(EntityConverter::convertUser)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserInfoDto findUserById(Long id) {
        return EntityConverter.convertUser(userRepo.findById(id).orElseThrow());
    }

    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        User user = userRepo.findById(userId).orElseThrow();
        user.getProductOrders().forEach(order -> {
            if (order.getDeliveryState().equals(BEFORE.toString())) {
                cartRepo.deleteAll(order.getProductOrderProducts());
                orderRepo.delete(order);
            }
            order.setUser(null);
        });
        userPointHistoryRepo.deleteByUser(user);
        userAuthorityRepo.deleteByUser(user);
        userCCRepo.deleteByUser(user);
        userAddressRepo.deleteByUser(user);
        productReviewRepo.deleteByUser(user);
        userRepo.delete(user);
    }

    @Override
    @Transactional
    public void updateUser(UserUpdateForm updateForm) {
        User user = userRepo.findById(updateForm.getId()).orElseThrow();
        user.setEnabled(updateForm.getEnabled());
        user.setName(updateForm.getName());
        user.setPhone(updateForm.getPhone());
        updateAuthorities(user, updateForm.getAuthority()); // 권한 설정

        // 기존 비밀번호화 일치하는지 확인 후 변경
        if (!updateForm.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updateForm.getPassword()));
        }
    }

    private void updateAuthorities(User user, String authority) {
        switch (authority) {
            case "ROLE_USER":
                user.getAuthorities()
                        .stream()
                        .filter(auth -> !auth.getAuthority().equals(ROLE_USER.toString()))
                        .forEach(authorityRepo::delete);
                break;
            case "ROLE_ADMIN":
                Authority auth = new Authority();
                auth.setUser(user);
                auth.setAuthority(ROLE_ADMIN.toString());
                authorityRepo.save(auth);
                break;
            default:
                throw new IllegalArgumentException();
        }

    }

    @Override
    public UserUpdateForm initializeUserUpdateForm(Long userId) {
        UserUpdateForm updateForm = new UserUpdateForm();
        updateForm.setId(userId);
        return updateForm;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductBookDto> findProductBookByConditions(String searchCriteria, String keyword, Boolean enabled) {

        Specification<ProductBook> spec = Specification.where(null);
        if (spec == null) throw new AssertionError();
        if (!(searchCriteria == null || searchCriteria.isEmpty()) && !(keyword == null || keyword.isEmpty())) {
            switch (searchCriteria) {
                // equal
                case "id":
                    spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                            criteriaBuilder.equal(root.get(searchCriteria), keyword));
                    break;
                // like
                case "title":
                case "author":
                    spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                            criteriaBuilder.like(root.get(searchCriteria), setLikeKeyword(keyword)));
                    break;
                // join
                case "publisher":
                    spec = spec.and(((root, criteriaQuery, criteriaBuilder) ->
                            criteriaBuilder.like(root.get("publisher").get("name"), keyword)));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }

        if (enabled != null) {
            spec = Objects.requireNonNull(spec).and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("enabled"), enabled));
        }
        return productBookRepo.findAll(spec)
                .stream()
                .map(this::convertProductBookToDto)
                .collect(Collectors.toList());
    }

    private ProductBookDto convertProductBookToDto(ProductBook book) {
        ProductBookDto dto = new ProductBookDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setPublisher(book.getPublisher().getName());
        dto.setPrice(book.getPrice());
        dto.setEnabled(book.isEnabled());
        dto.setStock(book.getStock());
        dto.setPreparingStock(cartRepo.findByProductBook(book)
                .stream()
                .filter(pop -> pop.getDeliveryState().equals(PREPARING.toString()))
                .mapToInt(ProductOrderProduct::getProductCount)
                .sum());

        return dto;
    }

    @Override
    public void updateProductStock(List<ProductBookDto> books) {
        // 성능 문제로 개선 예정
        books.forEach(bookDto -> productBookRepo.updateStockById(bookDto.getId(), bookDto.getStock()));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductBookForm getProductBookFormById(Long id) {
        ProductBookForm bookForm = new ProductBookForm();
        ProductBook book = productBookRepo.findProductBookById(id);

        bookForm.setId(book.getId());
        bookForm.setTitle(book.getTitle());
        bookForm.setPublisher(book.getPublisher().getId());
        bookForm.setAuthor(book.getAuthor());
        bookForm.setPrice(book.getPrice());
        bookForm.setDescription(book.getDescription());
        bookForm.setStock(book.getStock());
        bookForm.setCategories(book.getProductCategories()
                .stream()
                .map(productCategory ->
                        productCategory.getCategory().getId())
                .collect(Collectors.toSet()));
        return bookForm;
    }

    @Override
    @Transactional
    public void updateProductBook(ProductBookForm bookForm) {
        ProductBook book = productBookRepo.findById(bookForm.getId()).orElseThrow();
        book.setTitle(bookForm.getTitle());
        book.setPublisher(publisherRepo.findPublisherById(bookForm.getPublisher()));
        book.setAuthor(bookForm.getAuthor());
        book.setPrice(bookForm.getPrice());
        book.setDescription(bookForm.getDescription());
        book.setStock(bookForm.getStock());

        Set<ProductCategory> categories = book.getProductCategories();
        productCategoryRepo.deleteAll(categories);
        book.setProductCategories(bookForm.getCategories().stream().map(categoryId -> {
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductBook(book);
            productCategory.setCategory(categoryRepo.findCategoryById(categoryId));
            productCategoryRepo.save(productCategory);
            return productCategory;
        }).collect(Collectors.toSet()));

        if (!(bookForm.getBookImage() == null || bookForm.getBookImage().isEmpty())) {
            saveMultipartFile(bookForm, book);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductReview> findProductReviewByConditions(String searchCriteria, String keyword) {
        Specification<ProductReview> spec = Specification.where(null);
        if (!(searchCriteria == null || keyword == null) && !(searchCriteria.isBlank() || keyword.isBlank())) {
            switch (searchCriteria) {
                case "title":
                    spec = Objects.requireNonNull(spec)
                            .and((root, criteriaQuery, criteriaBuilder) ->
                                    criteriaBuilder.like(root.get("productBook").get("title"), setLikeKeyword(keyword)));
                    break;
                case "username":
                    spec = Objects.requireNonNull(spec)
                            .and(((root, criteriaQuery, criteriaBuilder) ->
                                    criteriaBuilder.like(root.get("user").get("username"), setLikeKeyword(keyword))));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        return productReviewRepo.findAll(spec);
    }

    private String setLikeKeyword(String keyword) {
        return "%" + keyword + "%";
    }

    @Override
    public void deleteProductReviewById(Long id) {
        productReviewRepo.deleteById(id);
    }
}
