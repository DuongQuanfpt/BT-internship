package finalproject.group1.BE.web.dto.response.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * dto response of cart detail in cart-info
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartInfoDetailResponse {
    /**
     * cart detail id
     */
    Integer id;

    /**
     * product id
     */
    Integer productId;

    /**
     * product name
     */
    String productName;

    /**
     * product thumbnail image path
     */
    String imagePath;

    /**
     * product thumbnail image name
     */
    String imageName;

    /**
     * amount of product in cart
     */
    Integer quantity;

    /**
     * price of each product
     */
    Float price;

    /**
     * total prices of product in cart
     */
    Float totalPrice;
}
