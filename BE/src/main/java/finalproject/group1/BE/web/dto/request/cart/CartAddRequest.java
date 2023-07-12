package finalproject.group1.BE.web.dto.request.cart;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CartAddRequest {
    @Size(min = 20 , max = 20)
    String token;

    @Min(1)
    Integer productId;

    @Min(1)
    Integer quantity;
}
