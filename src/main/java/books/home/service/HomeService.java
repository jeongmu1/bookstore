package books.home.service;

import books.home.common.ProductBookDTO;
import books.product.domain.Category;
import books.product.domain.ProductBook;
import books.product.domain.ProductImage;

import java.util.List;
import java.util.Set;

public interface HomeService {
    public Set<Category> findAllCategories();

    public List<ProductBookDTO> findDisplayBooksForDTOs();
}
