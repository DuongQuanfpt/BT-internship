package finalproject.group1.BE.web.dto.response.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import finalproject.group1.BE.commons.Constants;

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
    String getLoginId();

    /**
     * user name
     */
    String getUserName();

    /**
     * user birthday
     */
    @JsonFormat(pattern= Constants.VALID_DATE_FORMAT)
    LocalDate getBirthDay();

    /**
     * sum price of all user order
     */
    Float getTotalPrice();


}
