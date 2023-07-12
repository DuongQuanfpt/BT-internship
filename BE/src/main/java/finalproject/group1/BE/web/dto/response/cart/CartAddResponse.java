package finalproject.group1.BE.web.dto.response.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * dto for response of add-cart
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartAddResponse {
    /**
     * token of cart with no owner
     */
    String token;

    /**
     * total quantity of products in cart
     */
    Integer quantity;

    /**
     * cart version
     */
    Integer versionNo;
}
