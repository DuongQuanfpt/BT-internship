package finalproject.group1.BE.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
