package finalproject.group1.BE.web.dto.request.cart;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * dto request of cart-info, sync-cart
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartRequest {
    /**
     * cart token
     */
    @Size(min = 20 , max = 20)
    String token;
}
