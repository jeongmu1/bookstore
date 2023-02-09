package books.home.service;

import books.common.PageSizeProps;
import books.common.ProductBookConverter;
import books.home.common.ProductBookDto;
import books.product.domain.Category;
import books.product.domain.ProductBook;
import books.product.domain.ProductImage;
import books.product.repository.CategoryRepository;
import books.product.repository.ProductBookRepository;
import books.product.repository.ProductImageRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class HomeServiceImpl implements HomeService {
    private final ProductBookRepository productBookRepo;
    private final CategoryRepository categoryRepo;
    private final ProductImageRepository productImageRepo;

    private final PageSizeProps pageSizeProps;

    public HomeServiceImpl(ProductBookRepository productBookRepo, CategoryRepository categoryRepo, ProductImageRepository productImageRepo, PageSizeProps pageSizeProps) {
        this.productBookRepo = productBookRepo;
        this.categoryRepo = categoryRepo;
        this.productImageRepo = productImageRepo;
        this.pageSizeProps = pageSizeProps;
    }

    @Override
    public Set<Category> findAllCategories() {
        return categoryRepo.findAllByOrderById();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductBookDto> findDisplayBooksForDtos() {
        return productBookRepo
                .findProductBooksByDisplay(true, PageRequest.of(0, pageSizeProps.getMainProducts()))
                .stream()
                .map(book -> {
                    book.setProductImages(productImageRepo.findAllByProductBook(book));
                    return ProductBookConverter.convertToDto(book);
                }).collect(Collectors.toList());

    }

    public List<ProductBook> queryTest() {
        return productBookRepo.findProductBooksByDisplay(true, PageRequest.of(0, pageSizeProps.getMainProducts()));
    }
}
