package finalproject.group1.BE.web.dto.response.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * dto response of cart info
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartInfoResponse {
    /**
     * cart id
     */
    Integer id;

    /**
     * total product price
     */
    Float totalPrice;

    /**
     * cart version
     */
    Integer versionNo;

    /**
     * list of cart detail
     */
    List<CartInfoDetailResponse> details;
}
