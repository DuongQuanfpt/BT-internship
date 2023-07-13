package finalproject.group1.BE.web.dto.request.user;

import finalproject.group1.BE.web.annotation.ValidDateFormat;
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
    private String userName;

    /**
     * user email (loginId) to search
     */
    private String loginId;

    /**
     * date range to search by user birthday
     */
    @ValidDateFormat(message = "invalid date format")
    private String startBirthDay;

    @ValidDateFormat(message = "invalid date format")
    private String endBirthDay ;


    /**
     * the minimum total price of user orders
     */
    private Float totalPrice;
}
