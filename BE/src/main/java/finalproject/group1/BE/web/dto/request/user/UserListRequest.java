package finalproject.group1.BE.web.dto.request.user;

import finalproject.group1.BE.web.annotation.ValidDateFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * dto for searching user list
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserListRequest {
    /**
     * username to search
     */
    private String userName;

    /**
     * user email (loginId) to search
     */
    private String loginId;

    /**
     * date range to search by user birthday
     */

    private String startBirthDay;
    private String endBirthDay ;

    /**
     * the minimum total price of user orders
     */
    private Float totalPrice;
}
