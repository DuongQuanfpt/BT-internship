package finalproject.group1.BE.web.dto.request.user;

import finalproject.group1.BE.web.annotation.UppercaseAndNumber;
import finalproject.group1.BE.web.annotation.ValidDateFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * dto for user registration
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {
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


    /**
     * user name
     */
    @NotEmpty
    @Size(min = 8, max = 255)
    String userName;

    /**
     * user birthday
     */
    @NotNull
    @ValidDateFormat(message = "invalid date format")
    String birthDay;
}
