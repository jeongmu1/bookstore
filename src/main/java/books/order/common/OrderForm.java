package books.order.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.CreditCardNumber;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
public class OrderForm {
    @CreditCardNumber(message = "올바른 신용카드 번호를 입력하세요")
    private String ccNumber;
    @Pattern(regexp = "^(0[1-9]|1[0-2])(/)([1-9]\\d)$", message = "MM/YY 형식으로 입력하세요")
    private String ccExpiration;
    @Digits(integer = 3, fraction = 0, message = "올바른 CVV 를 입력하세요")
    private String ccCvv;
    @NotBlank(message = "수취자 이름을 입력하세요")
    private String deliveryName;
    @Digits(integer = 5, fraction = 0, message = "올바른 우편번호를 입력하세요")
    private String deliveryZipCode;
    @NotBlank(message = "주소를 입력하세요")
    private String deliveryAddress;
    @Pattern(regexp = "(01\\d)(-)(\\d\\d\\d\\d)(-)(\\d\\d\\d\\d)", message = "휴대폰 번호를 - 포함하여 입력하세요")
    private String phone;
}
