import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringJUnitConfig
@WebAppConfiguration
@ComponentScan(basePackages = {"books"})
public class TestConfiguration {


}
