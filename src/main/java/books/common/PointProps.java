package books.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "bookstore.point")
@Data
public class PointProps {
    private int savingRate;
    private int unitPointUsage;
}

