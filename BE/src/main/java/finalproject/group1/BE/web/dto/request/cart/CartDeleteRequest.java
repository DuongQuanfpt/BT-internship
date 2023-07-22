package finalproject.group1.BE.web.dto.request.cart;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDeleteRequest {
    /**
     * cart token
     */
    @Size(min = 20 , max = 20)
    private String token;
    /**
     * cartDetail id
     */
    private int clearCart;
    /**
     * cartDetail quantity (updated)
     */
    private int detailId;
    /**
     * cart versionNo
     */
    private int versionNo;
}
