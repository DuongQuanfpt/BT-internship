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
    private String email;

    /**
     * user name
     */
    private String username;

    /**
     * user birthday
     */
    private LocalDate birthday;
}
