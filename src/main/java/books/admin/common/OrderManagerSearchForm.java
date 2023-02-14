package books.admin.common;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class OrderManagerSearchForm {
    private String searchCondition;
    private Set<Long> deliveryStates;
    private String keyword;
}
