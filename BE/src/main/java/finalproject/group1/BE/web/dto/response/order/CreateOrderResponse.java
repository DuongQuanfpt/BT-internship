package finalproject.group1.BE.web.dto.response.order;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * dto response create order
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderResponse {
    /**
     * order displayId
     */
    String displayId;

    /**
     * order price
     */
    Float totalPrice;
}
