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
    private String username;

    /**
     * user email (loginId) to search
     */
    private String email;

    /**
     * date range to search by user birthday
     */
    private String startDate;
    private String endDate;


    /**
     * the minimum total price of user orders
     */
    private Float totalPrice;
}
