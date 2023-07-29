package finalproject.group1.BE.web.dto.response.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import finalproject.group1.BE.commons.Constants;
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
    private String loginId;

    /**
     * user name
     */
    private String userName;

    /**
     * user birthday
     */
    @JsonFormat(pattern= Constants.VALID_DATE_FORMAT)
    private LocalDate birthDay;
}
