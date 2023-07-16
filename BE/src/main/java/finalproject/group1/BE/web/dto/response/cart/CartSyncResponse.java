package finalproject.group1.BE.web.dto.response.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * dto response of sync cart
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartSyncResponse {
    /**
     * total products quantity in cart
     */
    int totalQuantity;
}
