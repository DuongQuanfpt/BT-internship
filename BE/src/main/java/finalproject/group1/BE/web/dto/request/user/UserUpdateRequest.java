package finalproject.group1.BE.web.dto.request.user;

import finalproject.group1.BE.web.annotation.UppercaseAndNumber;
import finalproject.group1.BE.web.annotation.ValidDateFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * dto request user update
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
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
    String user_name;

    /**
     * user birthday
     */
    @NotNull
    @ValidDateFormat(message = "invalid date format")
    String birthDay;
}
