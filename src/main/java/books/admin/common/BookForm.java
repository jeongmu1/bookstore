package books.admin.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class BookForm {
    private List<ProductBookDto> books;

    public BookForm() {
        this.books = new ArrayList<>();
    }
}
