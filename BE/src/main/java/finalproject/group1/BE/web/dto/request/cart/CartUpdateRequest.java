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
public class CartUpdateRequest {
    /**
     * cart token
     */
    @Size(max = 20)
    private String token;
    /**
     * cartDetail id
     */
    private int id;
    /**
     * cartDetail quantity (updated)
     */
    private int quantity;
    /**
     * cart versionNo
     */
    private int versionNo;
}
