package finalproject.group1.BE.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * dto of response from get user detail
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailResponse {
    /**
     * email(loginId) of user
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
}
