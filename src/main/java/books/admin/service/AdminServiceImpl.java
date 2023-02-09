package books.admin.service;

import books.admin.common.FailToUploadException;
import books.admin.common.ProductBookForm;
import books.common.BookProps;
import books.product.domain.*;
import books.product.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class AdminServiceImpl implements AdminService {
    private final CategoryRepository categoryRepo;
    private final PublisherRepository publisherRepo;
    private final ProductBookRepository productBookRepo;
    private final BookProps bookProps;
    private final ProductImageRepository productImageRepo;
    private final ProductCategoryRepository productCategoryRepo;

    public AdminServiceImpl(CategoryRepository categoryRepo, PublisherRepository publisherRepo, ProductBookRepository productBookRepo, BookProps bookProps, ProductImageRepository productImageRepo, ProductCategoryRepository productCategoryRepo) {
        this.categoryRepo = categoryRepo;
        this.publisherRepo = publisherRepo;
        this.productBookRepo = productBookRepo;
        this.bookProps = bookProps;
        this.productImageRepo = productImageRepo;
        this.productCategoryRepo = productCategoryRepo;
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
    public List<Category> findAllCategories() {
        return categoryRepo.findAll();
    }

    @Override
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

}
