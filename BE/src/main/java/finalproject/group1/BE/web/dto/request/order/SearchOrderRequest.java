package finalproject.group1.BE.web.dto.request.order;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * dto for order search
 */
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchOrderRequest {
    /**
     * user name
     */
    String userName;
    /**
     * order id
     */
    String orderId;

    /**
     * order date
     */
    String orderDate;

    /**
     * product code
     */
    String sku;

    /**
     * product name
     */
    String productName;

    /**
     * order status
     */
    String status;

    public String getUserName() {
        if (userName != null && userName.isEmpty()) {
            return null;
        }
        return userName;
    }

    public String getOrderId() {
        if (orderId != null && orderId.isEmpty()) {
            return null;
        }
        return orderId;
    }

    public String getOrderDate() {
        if (orderDate != null && orderDate.isEmpty()) {
            return null;
        }
        return orderDate;
    }

    public String getSku() {
        if (sku != null && sku.isEmpty()) {
            return null;
        }
        return sku;
    }

    public String getProductName() {
        if (productName != null && productName.isEmpty()) {
            return null;
        }
        return productName;
    }

    public String getStatus() {
        if (status != null && status.isEmpty()) {
            return null;
        }
        return status;
    }
}
