package finalproject.group1.BE.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * dto , response from user login request
 */
@Getter
@Setter
@AllArgsConstructor
public class UserLoginResponse {

    /**
     * jwt token
     */
    String token;
}
