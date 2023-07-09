package finalproject.group1.BE.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * dto of response from get user list
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserListResponse {
    /**
     * id of user
     */
    Integer id;

    /**
     * email(loginid) of user
     */
    String email;

    /**
     * user name
     */
    String username;

    /**
     * user birthday
     */
    LocalDate birthday;

    /**
     * sum price of all user order
     */
    Float totalPrice;
}
