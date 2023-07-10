package finalproject.group1.BE.web.dto.response;

import java.time.LocalDate;

/**
 * dto of response from get user list
 */

public interface UserListResponse {
    /**
     * id of user
     */
    Integer getId();

    /**
     * email(loginid) of user
     */
    String getEmail();

    /**
     * user name
     */
    String getUsername();

    /**
     * user birthday
     */
    LocalDate getBirthday();

    /**
     * sum price of all user order
     */
    Float getTotalPrice();


}
