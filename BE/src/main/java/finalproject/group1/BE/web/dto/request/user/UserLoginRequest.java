package finalproject.group1.BE.web.dto.request.user;

import finalproject.group1.BE.web.annotation.UppercaseAndNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * dto for login user
 */
@Getter
@Setter
@AllArgsConstructor
public class UserLoginRequest {
    /**
     * user email
     */
    @NotEmpty
    @Email
    @Size(max = 255)
    private String loginId;

    /**
     * user password
     */
    @NotEmpty
    @Size(min = 8, max = 32)
    @UppercaseAndNumber(message = "Must contain an uppercase and a number")
    private String password;
}
