package books.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "bookstore.page-size")
@Data
public class PageSizeProps {
    private int userOrders;
    private int pointHistories;
    private int mainProducts;
}
