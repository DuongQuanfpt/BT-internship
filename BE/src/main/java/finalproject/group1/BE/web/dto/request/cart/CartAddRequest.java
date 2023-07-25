package finalproject.group1.BE.web.dto.request.cart;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *dto request of add-cart
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartAddRequest {
    /**
     * token cart for unauthenticated user
     */
    @Size(max = 20)
    String token;

    /**
     * product id
     */
    @Min(1)
    Integer productId;

    /**
     * product quantity
     */
    @Min(1)
    Integer quantity;
}
