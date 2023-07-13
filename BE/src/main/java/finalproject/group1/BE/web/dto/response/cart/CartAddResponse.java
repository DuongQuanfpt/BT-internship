package finalproject.group1.BE.web.dto.response.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * dto response of add-cart
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartAddResponse {
    /**
     * token cart for unauthenticated user
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
