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
    @Size(max = 20)
    private String token;
    /**
     * clear cart number
     */
    private int clearCart;
    /**
     * cartDetail id
     */
    private int detailId;
    /**
     * cart versionNo
     */
    private int versionNo;
}
