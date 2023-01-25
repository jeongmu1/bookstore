package books.home.service;

import books.home.common.ProductBookDto;
import books.product.domain.Category;

import java.util.List;
import java.util.Set;

public interface HomeService {
    public Set<Category> findAllCategories();

    public List<ProductBookDto> findDisplayBooksForDtos();
}
