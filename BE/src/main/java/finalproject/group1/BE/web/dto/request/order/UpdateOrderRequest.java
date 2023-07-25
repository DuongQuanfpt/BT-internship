package finalproject.group1.BE.web.dto.request.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderRequest {
    /**
     * order id
     */
    private int id;
    /**
     * order displayId
     */
    private String displayId;
    /**
     * order status
     */
    private String status;
}
