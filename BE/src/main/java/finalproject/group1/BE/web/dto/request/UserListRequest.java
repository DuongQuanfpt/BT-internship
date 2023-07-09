package finalproject.group1.BE.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * dto for searching user list
 */
@Getter
@Setter
@AllArgsConstructor
public class UserListRequest {
    /**
     * username to search
     */
    String username;

    /**
     * user email (loginId) to search
     */
    String email;

    /**
     * date range to search by user birthday
     */
    String fromDate;
    String toDate;


    /**
     * the minimum total price of user orders
     */
    Float totalPrice;
}
