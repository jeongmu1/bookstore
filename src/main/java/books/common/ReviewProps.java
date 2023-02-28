package books.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "bookstore.review")
@Data
public class ReviewProps {
    private Byte maxScore;
    private Byte minScore;
}
