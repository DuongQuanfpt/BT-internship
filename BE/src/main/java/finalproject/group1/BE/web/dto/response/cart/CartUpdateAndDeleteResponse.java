package finalproject.group1.BE.web.dto.response.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartUpdateAndDeleteResponse {
    /**
     * total quantity of cart
     */
    private int totalQuantity;
}
