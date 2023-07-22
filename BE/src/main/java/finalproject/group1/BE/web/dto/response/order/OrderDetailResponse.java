package finalproject.group1.BE.web.dto.response.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * dto response order detail
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponse {
    /**
     * order detail id
     */
    int id;

    /**
     * product id
     */
    int productId;

    /**
     * product name
     */
    String productName;

    /**
     * thumbnail image path
     */
    String imagePath;

    /**
     * thumbnail image name
     */
    String imageName;

    /**
     * product quantity
     */
    int quantity;

    /**
     * product price
     */
    float price;

    /**
     * detail total price
     */
    float totalPrice;

    /**
     * product status
     */
    String status;
}
