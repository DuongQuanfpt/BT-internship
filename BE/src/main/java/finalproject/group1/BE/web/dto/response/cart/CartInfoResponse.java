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
    Integer id;

    Float totalPrice;

    Integer versionNo;

    List<CartInfoDetailResponse> details;
}
